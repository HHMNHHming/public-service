package com.gwm.avp.component;

import com.gwm.avp.util.KeyPairUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtGenerator {

    private String privateKey = null;

    @Value("${session-exp.jwt:60}")
    private int expMin;

    public String generator(Map<String,Object> claims) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        //java读文件，构造成私钥字符串
        InputStream in = this.getClass().getResourceAsStream("/private.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder strBuild = new StringBuilder();
        String temp = null;
        while((temp = reader.readLine())!=null) {
            strBuild.append(temp);
        }
        reader.close();
        privateKey = strBuild.toString();

        //使用jwt，根据私钥生成token令牌
        RSAPrivateKey key = (RSAPrivateKey) KeyPairUtils.getPrivateKey(privateKey);
        JWSSigner jwssigner = new RSASSASigner(key);
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        Calendar calendar = Calendar.getInstance();
        builder.issueTime(calendar.getTime());
        calendar.add(Calendar.MINUTE,expMin);
        builder.expirationTime(calendar.getTime());
        claims.forEach((k,value)->{
            builder.claim(k,value);
        });
        //Payload的构建:将 过期的时间和具体的实际传递的参数的内容 封装到集合
        JWTClaimsSet claimsSet = builder.build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(jwssigner);
        return signedJWT.serialize();
    }
    public int getExpMin(){
        return expMin*60;
    }
}
