package com.gwm;

import org.junit.jupiter.api.Test;
import sun.misc.BASE64Encoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class GeneratorKeys {

    @Test
    public void genKeys() throws NoSuchAlgorithmException {
        //java8基于RSA算法生成一对RSA公钥和私钥
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        KeyPair keyPair =  keyPairGen.generateKeyPair();

        //公钥和私钥类型对象
        RSAPublicKey  publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //转化为加密字符串类型
        String publicKeyString = (new BASE64Encoder()).encodeBuffer(publicKey.getEncoded());
        String privateKeyString =(new BASE64Encoder()).encodeBuffer(privateKey.getEncoded());

        System.out.println("公钥:"+publicKeyString);
        System.out.println("私钥:"+privateKeyString);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
}
