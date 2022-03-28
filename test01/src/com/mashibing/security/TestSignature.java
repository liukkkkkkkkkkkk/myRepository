package com.mashibing.security;

import org.junit.Test;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.util.TreeSet;

//测试验签和加密本文介绍了非对称加密RSA算法及实现，应用场景包括于公钥加密私钥解密，或私钥签名公钥验证。
// 不同系统之间通过API调用时通常会使用私钥进行签名，接收方通过公钥进行验证，确保请求身份及内容完整。
public class TestSignature {


    static String  algorithm ="SHA256withRSA";
    //公钥和私钥两个密钥对
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        return pair;
    }
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("utf-8"));

        return Base64.getEncoder().encodeToString(cipherText);
    }


    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "utf-8");
    }


//签名 首先获得SHA256withRSA类型的Signature实例，使用私钥进行初始化，
// 使用消息中的所有字节更新它(也可以使用部分块(例如大型文件)来做这件事)，然后使用.sign()方法生成签名。最后返回base64编码字符串。
    public static  String sign(String plainText,PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature privateSignature = Signature.getInstance(algorithm);
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes("utf-8"));
        byte[] sign = privateSignature.sign();
        return  Base64.getEncoder().encodeToString(sign);
    }


    //验证消息方法 首先获得Signature实例，使用公钥设置验证，喂入消息文件字节数组然后使用签名字节数组看签名是否匹配。验证方法返回布尔类型表示签名是否有效
    public static boolean verify(String plainText,String signature,PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        Signature publicSignature = Signature.getInstance(algorithm);
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes("utf-8"));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        boolean verify = publicSignature.verify(signatureBytes);
        return verify;
    }



   @Test
   /*RSA加密和解密*/
    public void test1() throws Exception {
       //First generate a public/private key pair
       KeyPair pair = generateKeyPair();

//Our secret message
       String message = "the answer to life the universe and everything";

//Encrypt the message
       String cipherText = encrypt(message, pair.getPublic());
       System.out.println("加密后："+cipherText);

//Now decrypt it
       String decipheredMessage = decrypt(cipherText, pair.getPrivate());
       System.out.println("解密后:"+decipheredMessage);
   }

    @Test
    /*签名和验证*/
    public void test2() throws Exception {
        KeyPair keyPair = generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String message ="明天星期几？";
        String sign = sign(message, privateKey);
        System.out.println("sign:"+sign);
        boolean verify = verify(message, sign, publicKey);
        System.out.println("验证结果："+verify);
    }

    @Test
    //如果运行以下代码,您将获得Java安装支持的签名算法列表.
    public void test3() throws Exception {
        TreeSet<String> algorithms = new TreeSet<>();
        for (Provider provider : Security.getProviders())
            for (Provider.Service service : provider.getServices())
                if (service.getType().equals("Signature"))
                    algorithms.add(service.getAlgorithm());
        for (String algorithm : algorithms)
            System.out.println(algorithm);
    }


    }


