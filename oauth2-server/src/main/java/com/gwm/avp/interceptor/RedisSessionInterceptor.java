package com.gwm.avp.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gwm.avpdatacloud.basicpackages.log.GDCLog;
import com.gwm.avpdatacloud.basicpackages.utils.ResultData;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class RedisSessionInterceptor implements HandlerInterceptor {
    @Value("#{${expire}}")
    private Long expire;
    @Autowired
    StringRedisTemplate redisTemplate;

    String publicKey = null;

    //前面请求前进行一系列的操作,鉴权服务验证角色和（权限地址），网关token的正确性（之前是在网关验证，现在挪到鉴权2）
    //但是在鉴权2中我们在鉴权2服务中，进行jwt的验证和用户角色权限的验证
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        //验证当前用户是否已经登录
        if (StringUtils.isNotEmpty(authorization)) {
            //权限验证
            String realToken = authorization.replace("Bearer ", "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            JSONObject userJsonObject = JSONObject.parseObject(userStr);
            String userName = (String) userJsonObject.get("user_name");
            String sessionStr = redisTemplate.opsForValue().get(userName);
            if (StringUtils.isEmpty(sessionStr) || !realToken.equals(sessionStr)) {
                response401(response, null);
                return false;
            }
            redisTemplate.expire(userName, expire, TimeUnit.MINUTES);
            JSONArray roles = userJsonObject.getJSONArray("authorities");
            String roleName = roles.getString(0);
        }
            //1、jwt有效性验证
            if(StringUtils.isEmpty(authorization)||!verify(authorization.replace("Bearer ",""))){
                return true;
            }
        response401(response, null);
        return false;
    }

    private boolean verify(String token) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException, JOSEException {
       String  publicKey = System.getenv("PUBLICKEY");
        if(publicKey==null || publicKey.isEmpty()){
            InputStream in = this.getClass().getResourceAsStream("/public.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String tmp = null;
            while((tmp = reader.readLine())!=null){
                sb.append(tmp);
            }
            reader.close();
            publicKey = sb.toString();
        }
        byte[] keyBytes = (Base64.getMimeDecoder()).decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        JWSVerifier verifier = new RSASSAVerifier(key);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Long exp = signedJWT.getPayload().toJSONObject().getAsNumber("exp").longValue();
        Long now = System.currentTimeMillis() / 1000;
        return signedJWT.verify(verifier) && now < exp;
    }
    public void response401(HttpServletResponse response, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter writer = response.getWriter();) {
            if (message == null) {
                writer.print(JSON.toJSON(ResultData.Unauthorized()));
            } else {
                writer.print(JSON.toJSON(ResultData.FAILL(message)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
