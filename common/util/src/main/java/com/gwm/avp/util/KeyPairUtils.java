package com.gwm.avp.util;

import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyPairUtils {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairUtils utils = new KeyPairUtils();
        String[] keys = utils.generatorKey();
        FileWriter writer1 = new FileWriter("D:/public.txt");
        writer1.write(keys[0]);
        writer1.flush();
        writer1.close();
        FileWriter writer2 = new FileWriter("D:/private.txt");
        writer2.write(keys[1]);
        writer2.flush();
        writer2.close();
    }
    private String[] generatorKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);

        KeyPair kp = kpg.genKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
        byte[] publicKeyBytes = publicKey.getEncoded();
        Base64.Encoder encoder = Base64.getMimeEncoder();
        String publicKeyBase64 = encoder.encodeToString(publicKeyBytes);
        byte[] privateKeyBytes = privateKey.getEncoded();
        String privateKeyBase64 = encoder.encodeToString(privateKeyBytes);
        return new String[]{publicKeyBase64,privateKeyBase64};
    }

    /**
     * 字符串转换成PublicKey对象
     * @param publicKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(String publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = (Base64.getMimeDecoder()).decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 字符串转换成PrivateKey对象
     * @param privateKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey getPrivateKey(String privateKey)throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
        byte[] keyBytes = (Base64.getMimeDecoder()).decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }
}
