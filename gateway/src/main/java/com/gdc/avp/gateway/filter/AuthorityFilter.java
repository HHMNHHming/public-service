package com.gdc.avp.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.gdc.avp.gateway.client.AuthClient;
import com.gwm.avpdatacloud.basicpackages.log.GDCLog;
import com.gdc.avp.gateway.util.KeyPairUtils;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthorityFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthClient authClient;

    private String publicKey = null;
    @PostConstruct
    public void init() throws IOException {
        GDCLog.info("System","com.gdc.avp.gateway.filter.AuthorityFilter.init","加载公钥...");
        publicKey = System.getenv("PUBLICKEY");
        if(publicKey==null || publicKey.isEmpty()){
            GDCLog.info("System","com.gdc.avp.gateway.filter.AuthorityFilter.init","获取环境变量PUBLICKEY失败，读取资源文件public.txt");
            InputStream in = this.getClass().getResourceAsStream("/public.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String tmp = null;
            while((tmp = reader.readLine())!=null){
                sb.append(tmp);
            }
            reader.close();
            publicKey = sb.toString();
        }else{
            GDCLog.info("System","com.gdc.avp.gateway.filter.AuthorityFilter.init","获取环境变量PUBLICKEY成功");
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        // Allow 允许放行，不进行验证；
        if(headers.containsKey("Allow")){
            return chain.filter(exchange);
        }
        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        try {
            //1、jwt有效性验证
            if(StringUtils.isEmpty(authorization)||!verify(authorization.replace("Bearer ",""))){
                return writeTo(exchange,ResultData.Unauthorized().setMsg("暂未登录或Token已经过期"));
            }
        } catch (Exception e) {
            GDCLog.error("System","com.gdc.avp.gateway.filter.AuthorityFilter.filter()",e.getMessage());
            return writeTo(exchange,ResultData.ERROR("Token不是有效值"));
        }
        //2、访问地址权限验证
        //String via = request.getHeaders().getFirst(HttpHeaders.VIA);
        //GDCLog.info("System","com.gdc.avp.gateway.filter.AuthorityFilter.filter():via",via);
        //String otherAuth = String.valueOf(request.getHeaders().get("Other-Auth"));
        //System.out.println(otherAuth); // 注意获取请求头的结果的小区别哦！
        String otherAuth = request.getHeaders().getFirst("Other-Auth");
        System.out.println(otherAuth);
        try{
            Set<URI> requestUrls;
            try {
                requestUrls = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
            } catch (Exception e) {
                requestUrls = Collections.singleton(exchange.getRequest().getURI());
            }
            List<URI> urls = requestUrls.stream().limit(1).collect(Collectors.toList());
            ResultData result = authClient.check(authorization,otherAuth,urls.get(0).getPath());
            if(result.getCode() != 200){
                return writeTo(exchange,result);
            }
            return chain.filter(exchange);
        }catch (Exception e){
            e.printStackTrace();
            GDCLog.error(null,"System","com.gdc.avp.gateway.filter.AuthorityFilter.filter()"+e.getMessage());
            return writeTo(exchange,ResultData.ERROR(e.getMessage()));
        }
    }

    @Override
    public int getOrder() {
        return 3;
    }

    private boolean verify(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException, JOSEException {
        RSAPublicKey key = (RSAPublicKey) KeyPairUtils.getPublicKey(publicKey);
        JWSVerifier verifier = new RSASSAVerifier(key);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Long exp = signedJWT.getPayload().toJSONObject().getAsNumber("exp").longValue();
        Long now = System.currentTimeMillis()/1000;
        return signedJWT.verify(verifier) && now<exp;
    }

    private Mono<Void> writeTo(ServerWebExchange exchange,ResultData result){
        ServerHttpResponse response = exchange.getResponse();
        response.beforeCommit(()->{
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return Mono.empty();
        });
        response.setStatusCode(HttpStatus.OK);
        DataBuffer wrap = response.bufferFactory().wrap(JSONObject.toJSON(result).toString().getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(wrap));
    }
}
