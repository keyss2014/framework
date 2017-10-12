package cn.keyss.common.secret;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密、签名及验签
 */
public class RsaCryptoHelper {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密码
     *
     * @return 公私钥对
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取私钥
     *
     * @param keyMap 密钥对
     * @return 私钥
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return 公钥
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 利用私钥加签
     *
     * @param privateKey 私钥
     * @param data       数据
     * @return 签名
     * @throws Exception
     */
    public static byte[] signByPrivateKey(String privateKey, byte[] data) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 利用私钥加签
     *
     * @param privateKey 私钥
     * @param stringData 数据字符串
     * @return 签名字符串
     * @throws Exception
     */
    public static String signByPrivateKey(String privateKey, String stringData) throws Exception {
        byte[] data = stringData.getBytes("UTF-8");
        byte[] sign = signByPrivateKey(privateKey, data);
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * 利用公钥验签
     *
     * @param publicKey 公钥
     * @param data      数据
     * @param sign      签名
     * @return 是否验签成功
     * @throws Exception
     */
    public static boolean verifyByPublicKey(String publicKey, byte[] data, byte[] sign) throws Exception {

        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 利用公钥验签
     *
     * @param publicKey  公钥
     * @param stringData 数据字符串
     * @param stringSign 签名
     * @return 是否验签成功
     * @throws Exception
     */
    public static boolean verifyByPublicKey(String publicKey, String stringData, String stringSign) throws Exception {
        byte[] sign = Base64.getDecoder().decode(stringSign);
        byte[] data = stringData.getBytes("UTF-8");
        return verifyByPublicKey(publicKey, data, sign);
    }

    /**
     * 私钥解密
     *
     * @param privateKey    私钥
     * @param encryptedData 加密数据
     * @return 解密数据
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String privateKey, byte[] encryptedData)
            throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 私钥解密
     *
     * @param privateKey        私钥
     * @param stringCryptedData 加密数据字符串
     * @return 解密数据字符串
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKey, String stringCryptedData) throws Exception {
        byte[] datas = Base64.getDecoder().decode(stringCryptedData);
        byte[] results = decryptByPrivateKey(privateKey, datas);
        return new String(results, "UTF-8");
    }

    /**
     * 利用公钥加密
     *
     * @param publicKey 公钥
     * @param data      数据
     * @return 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String publicKey, byte[] data)
            throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 利用公钥加密
     *
     * @param publicKey  公钥
     * @param stringData 数据字符串
     * @return 加密后的数据字符串
     * @throws Exception
     */
    public static String encryptByPublicKey(String publicKey, String stringData)
            throws Exception {
        byte[] data = Base64.getDecoder().decode(stringData);
        byte[] result = encryptByPublicKey(publicKey, data);
        return Base64.getEncoder().encodeToString(result);
    }
}
