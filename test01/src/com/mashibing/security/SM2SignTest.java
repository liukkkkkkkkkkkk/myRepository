package com.mashibing.security;

import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SM2SignTest {
    public static void main(String[] args) throws Exception {
        // 获取SM2椭圆曲线的参数
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
        // 获取一个椭圆曲线类型的密钥对生成器
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
        // 使用SM2参数初始化生成器
        kpg.initialize(sm2Spec);
// 使用SM2的算法区域初始化密钥生成器
        kpg.initialize(sm2Spec, new SecureRandom());
        // 获取密钥对
        KeyPair keyPair = kpg.generateKeyPair();
        /*
获取公私钥
 */

        /*
        * 测试密钥：
签名私钥
00DB19F00E09E65D045FA8C23FF6C53007A6023B7AA74DC2E6702CA962B2618958
验签密钥：
04A648DDD70E3697E2F7C8375389F67EC07E673EC5CC5CFCAB64CCA632676CF938F428B30261689CBA06DBC3D1B9098F7B9732B412D82532F9943DDA4406716AFD

        * */
        //产生了密钥对之后，就可以使用JAVA security 提供的一些标准化的接口来完成签名验签操作。
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
/*        String privateKeyStr="00DB19F00E09E65D045FA8C23FF6C53007A6023B7AA74DC2E6702CA962B2618958";
        String publicKeyStr= "04A648DDD70E3697E2F7C8375389F67EC07E673EC5CC5CFCAB64CCA632676CF938F428B30261689CBA06DBC3D1B9098F7B9732B412D82532F9943DDA4406716AFD";
         publicKey = getPublicKey(publicKeyStr);
         privateKey =getPrivateKey(privateKeyStr);   //有问题？？？？？？？？*/
// 生成SM2sign with sm3 签名验签算法实例
        Signature signature = Signature.getInstance(
                GMObjectIdentifiers.sm2sign_with_sm3.toString()
                , new BouncyCastleProvider());
        /*
         * 签名
         */
// 签名需要使用私钥，使用私钥 初始化签名实例
        signature.initSign(privateKey);
// 签名原文
        byte[] plainText = "Hello world".getBytes(StandardCharsets.UTF_8);
// 写入签名原文到算法中
        signature.update(plainText);
// 计算签名值
        byte[] signatureValue = signature.sign();
        System.out.println("signature: \n" + Hex.toHexString(signatureValue));
        /*
         * 验签
         */
// 签名需要使用公钥，使用公钥设置验证
        signature.initVerify(publicKey);
// 写入待验签的签名原文到算法中
        signature.update(plainText);
// 验签
        System.out.println("Signature verify result: " + signature.verify(signatureValue));
    }




//获取公钥PublicKey
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = key.getBytes();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    //获取私钥PrivateKey
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = key.getBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
}
