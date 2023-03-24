import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class JWTTest {

    private final static String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkOi1hzHgIO+QPBTioKvZKDsTSatQXr6C9lUIy46m74odUgwE/EUlmxizc1KvyX43iP60bvO28WYBbuHCBDPtHY8+MFXrI7f7lWv7Vu7+NiO8XCZOJF3OPAgpF9LKXaeAQ50ukLZ0763t2V9xmKiDb87snLyXNMApFocwlcK4YFQom6ykCnuWi6XI0Ds8xSGYFNmg/n2RExZCK30hoMITWOqEuB28iopHV3EMkzOsEPInh3ZZ8iO62JOGNlHPUfi1IS3IzVhd39Vgt2ghyIPdWYa2cxS3DcczYH6WNBEUz6leVuYwJJfCCRQHekdLIV87o5rbMudbcmHxwizhWJLq/QIDAQAB";

    public static void main(String[] args) throws Exception {
        InputStream in = JWTTest.class.getResourceAsStream("/public.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        while((tmp = reader.readLine())!=null){
            sb.append(tmp);
        }
        reader.close();
        verify("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NDY4ODY3NTIsInVzZXJfbmFtZSI6IkdEQ-i-m-mHjiIsImF1dGhvcml0aWVzIjpbIkFETUlOIl0sImp0aSI6ImNmNmU5NzY0LWVhODktNDRlYy1iMzNjLTJhOTEyNzVlYTA2MiIsImNsaWVudF9pZCI6ImNsaWVudC1hcHAiLCJzY29wZSI6WyJhbGwiXX0.BgD6_GuUh-0LYB2hQO9HY-ypVB9-ARGg8FzP66fRvaNqLjuAKYDW6wCjFfNTdDsBvoHJrHNB_h8fI4alW6Zgfjs3PIiuwYDgIyzacE6NaRRcTiOZM6V3uSjamFYw4t_qi42GonToYXX3WnnRhEz-aOkJPgoUGKn_7M4my-6r-Fr7i-uRjwb6ip1M3rM-n-6ImNc5c4xwRPIvEWE85q4gRU-VUWbOjpn-KsJL8AnPHXS06yIzrqlNlyIZ4e_USUqvKqzm8u3FkkApwph9P86DXSXVYyJTjdFA0TJap0fsT7Tu7v3wc76AMpJELA9rh_ZkqwD8tFOZilTw5xXdRpayhA;1646886751398",sb.toString());
    }

    public static void verify1() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhbGljZSIsInNjb3BlIjoib3BlbmlkIiwiaXNzIjoiaHR0cHM6XC9cL2MyaWQuY29tIiwiaWF0IjoxMjN9.OwJ68odfqZc-a6fykEnfTCOEkVNZC3lC-YMZlenN3qpslS7cAMMgFCGk_O7J810bmVc_R4sEeXJ2MJnyrnOBI7yIjrJVJ4SYSEEoiO5YuZ7XQxOBsdkpUxKZrJudo1pGOmptjNDdwF1VXTA3KnI92MMIXaU2iOYEgYR4IS1rYWLDAkj-oCHxKUjHPYTvCkpkQtf4wYgwOIMGj4UxX4NXCpTfNXZrnggpKBhw2rihDhrcLXe2yDamIeUDbyqUMsbCb3B75cQs841BZi0XgFXU2vVD03JSY2cnUA6N1tpFU107IlMcp2BOKGQyDvuF9tikY8zqax8U-JHoQBY4DhKsOw";
        RSAPublicKey publicKey = getPublicKey(key);
        SignedJWT signedJWT = SignedJWT.parse(token);
        System.out.println(signedJWT.getPayload().toJSONObject());
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        System.out.println(signedJWT.verify(verifier));
    }

    public static String[] generateToken() throws JOSEException, NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair kp = kpg.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        JWSSigner signer = new RSASSASigner(privateKey);
        JWTClaimsSet claimsSetTwo = new JWTClaimsSet.Builder()
                .subject("alice")
                .issuer("https://c2id.com")
                .issueTime(new Date(123000L))
                .expirationTime(new Date())
                .claim("name", "openid")
                .claim("role","1")
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSetTwo);
        signedJWT.sign(signer);
        return new String[]{signedJWT.serialize(),publicKeyBase64};
    }

    public static void verify(String token,String key) throws Exception {
        RSAPublicKey publicKey = getPublicKey(key);
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        SignedJWT signedJWT = SignedJWT.parse(token);
        System.out.println(signedJWT.getPayload().toJSONObject());
        System.out.println(signedJWT.verify(verifier));
    }

    public static RSAPublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = (Base64.getDecoder()).decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        return publicKey;
    }
}
