

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Created by xyn on 2020/10/23 10:12
 */
public final class AesUtils {
    private static final String CHARSET_NAME = "UTF-8";
    private static final String AES_NAME = "AES";
    public static final String ALGORITHM = "AES/CBC/PKCS7Padding";
    //    public static final String ALGORITHM = "AES/CBC/ZerosPadding";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NoPadding";// 默认的加密算法


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param content
     * @param key
     * @return
     */
    public static String encrypt( String content,  String key) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, paramSpec);
            result = cipher.doFinal(content.getBytes(CHARSET_NAME));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Base64.encodeBase64String(result);
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String decrypt( String content,  String key) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET_NAME), AES_NAME);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(subBytes(key.getBytes(CHARSET_NAME)));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, paramSpec);
            return new String(cipher.doFinal(Base64.decodeBase64(content)), CHARSET_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * content: 加密内容
     * slatKey: 加密的盐，16位字符串
     * vectorKey: 加密的向量，16位字符串
     * （1）新建Cipher对象时需要传入一个参数"AES/CBC/PKCS5Padding"
     * <p>
     * （2）cipher对象使用之前还需要初始化，共三个参数("加密模式或者解密模式","密匙","向量")
     * <p>
     * （3）调用数据转换：cipher.doFinal(content)，其中content是一个byte数组
     * <p>
     * 实际上Cipher类实现了多种加密算法，在创建Cipher对象时，传入不同的参数就可以进行不同的加密算法。而这些算法不同的地方只是创建密匙的方法不同而已。
     * <p>
     * 如传入“AES/CBC/NoPadding”可进行AES加密，传入"DESede/CBC/NoPadding"可进行DES3加密。具体的后面会介绍到。
     * (1)加密算法有：AES，DES，DESede(DES3)和RSA 四种
     * (2) 模式有CBC(有向量模式)和ECB(无向量模式)，向量模式可以简单理解为偏移量，使用CBC模式需要定义一个IvParameterSpec对象
     * (3) 填充模式:
     * * NoPadding: 加密内容不足8位用0补足8位, Cipher类不提供补位功能，需自己实现代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     * * PKCS5Padding: 加密内容不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
     *
     * @date 2020-12-04
     */
    public static String encrypt(String content, String slatKey, String vectorKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(CHARSET_NAME), AES_NAME);//创建密匙主要使用SecretKeySpec、KeyGenerator和KeyPairGenerator三个类来创建密匙。
        IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes(CHARSET_NAME));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes(CHARSET_NAME));
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * content: 解密内容(base64编码格式)
     * slatKey: 加密时使用的盐，16位字符串
     * vectorKey: 加密时使用的向量，16位字符串
     *
     * @date 2020-12-04
     */
    public static String decrypt(String base64Content, String slatKey, String vectorKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(CHARSET_NAME), AES_NAME);
        IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes(CHARSET_NAME));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] content = Base64.decodeBase64(base64Content.getBytes(CHARSET_NAME));
        byte[] encrypted = cipher.doFinal(content);
        return new String(encrypted, CHARSET_NAME);
    }

    /**
     * 从一个byte[]数组中截取一部分
     *
     * @param src
     * @return
     */
    public static byte[] subBytes(byte[] src) {
        if (src.length < 16) {
            throw new RuntimeException("无法从Key中获取偏移量!");
        }
        byte[] bs = new byte[16];
        for (int i = 0; i < 16; i++) {
            bs[i] = src[i];
        }
        return bs;
    }

    //==============================================================================================
    // private static final String KEY_ALGORITHM = "AES";


    /**
     * AES 加密操作(ZeroPadding填充)(2021-01-14)
     *
     * @param content
     * @param slatKey
     * @param vectorKey
     * @return
     */
    public static String encryptAesCbcZeroPadding(String content, String slatKey, String vectorKey) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(CHARSET_NAME), AES_NAME);//创建密匙主要使用SecretKeySpec、KeyGenerator和KeyPairGenerator三个类来创建密匙。
            IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes(CHARSET_NAME));

            int blockSize = cipher.getBlockSize();//加密块大小
            System.out.println("blockSize:"+blockSize);
            byte[] byteContent = content.getBytes();//加密字符串的字节大小
            int plaintextLength = byteContent.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(byteContent, 0, plaintext, 0, byteContent.length);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);// 初始化为加密模式的密码器
            byte[] encrypted = cipher.doFinal(plaintext);// 加密
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        encryptAesCbcZeroPadding("sdafdasfddddddd","11","vvvvvvvvvvv");
    }
    /**
     * AES 解密操作(ZeroPadding填充)(2021-01-14)
     *
     * @param base64Content
     * @param slatKey
     * @param vectorKey
     * @return
     */
    public static String decryptAesCbcZeroPadding(String base64Content, String slatKey, String vectorKey) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(slatKey.getBytes(CHARSET_NAME), AES_NAME);
            IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes(CHARSET_NAME));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] content = Base64.decodeBase64(base64Content.getBytes(CHARSET_NAME));
            byte[] encrypted = cipher.doFinal(content);
            int index = 0;
            for (int i = 0; i < encrypted.length; i++) {
                int k = encrypted[i];
                if (k == 0) {
                    index = i;
                    break;
                }
            }
            byte[] arr = new byte[index];
            System.arraycopy(encrypted, 0, arr, 0, index);
            return new String(arr, CHARSET_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        int len = password.getBytes().length;
        if (len % 16 != 0) {
            len = len + (16 - (len % 16));
        }
        byte[] newpass = new byte[len];
        System.arraycopy(password.getBytes(), 0, newpass, 0, password.getBytes().length);
        SecretKeySpec keySpec = new SecretKeySpec(newpass, "AES");
        return keySpec;
    }
}
