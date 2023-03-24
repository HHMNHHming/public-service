package com.gdc.avp.component;

import com.gwm.avp.util.KeyPairUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
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

@Component
public class JwtGenerator {

    private String privateKey = null;

    @Value("${session-exp.jwt:60}")
    private int expMin;

    @PostConstruct
    private void init() throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/private.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        while((tmp = reader.readLine())!=null){
            sb.append(tmp);
        }
        reader.close();
        privateKey = sb.toString();
    }

    public String generator(Map<String,Object> claims) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        RSAPrivateKey key = (RSAPrivateKey) KeyPairUtils.getPrivateKey(privateKey);
        JWSSigner signer = new RSASSASigner(key);
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.subject("GDC")
                .issuer("智能驾驶平台开发科").jwtID(UUID.randomUUID().toString());
        Calendar calendar = Calendar.getInstance();
        builder.issueTime(calendar.getTime());
        calendar.add(Calendar.MINUTE,expMin);
        builder.expirationTime(calendar.getTime());
        claims.forEach((k,value)->{
            builder.claim(k,value);
        });
        JWTClaimsSet claimsSet = builder.build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
    public int getExpMin(){
        return expMin*60;
    }
}
