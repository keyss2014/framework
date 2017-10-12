package cn.keyss.common.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.util.Base64;

public class TripleDesCryptoHelper {
    /**
     * 加密算法
     */
    public static final String KEY_ALGORITHM = "DESede";

    /**
     * 签名算法
     */
    public static final String KEY_ALGORITHM_SETTING = "DESede/CBC/PKCS5Padding";

    /**
     * 加密
     *
     * @param stringKey 密码
     * @param stringIV  密码向量
     * @param data      数据
     * @return 加密后数据
     * @throws Exception
     */
    public static byte[] encrypt(String stringKey, String stringIV, byte[] data)
            throws Exception {
        byte[] key = Base64.getDecoder().decode(stringKey);
        byte[] iv = Base64.getDecoder().decode(stringIV);

        DESedeKeySpec keySpec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        Key k = keyfactory.generateSecret(keySpec);
        IvParameterSpec ips = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_SETTING);
        cipher.init(Cipher.ENCRYPT_MODE, k, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 加密方法
     *
     * @param stringKey  加密Key
     * @param stringIV   加密IV
     * @param stringData 要加密的内容
     * @return 加密后的内容
     * @throws Exception
     */
    public static String encrypt(String stringKey, String stringIV, String stringData)
            throws Exception {
        byte[] data = stringData.getBytes("UTF-8");
        byte[] bOut = encrypt(stringKey, stringIV, data);
        return Base64.getEncoder().encodeToString(bOut);
    }

    /**
     * 解密数据
     *
     * @param stringKey 密码
     * @param stringIV  密码向量
     * @param data      数据
     * @return 加密后数据
     * @throws Exception
     */
    public static byte[] decrypt(String stringKey, String stringIV, byte[] data)
            throws Exception {
        byte[] key = Base64.getDecoder().decode(stringKey);
        byte[] iv = Base64.getDecoder().decode(stringIV);

        //准备密钥及向量
        DESedeKeySpec keySpec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        Key k = keyfactory.generateSecret(keySpec);
        IvParameterSpec ips = new IvParameterSpec(iv);

        //解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_SETTING);
        cipher.init(Cipher.DECRYPT_MODE, k, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 解密
     *
     * @param stringKey  加密Key
     * @param stringIV   加密IV
     * @param stringData 要解密的内容
     * @return 解密后的内容
     * @throws Exception
     */
    public static String decrypt(String stringKey, String stringIV, String stringData)
            throws Exception {
        byte[] data = Base64.getDecoder().decode(stringData);
        byte[] bOut = decrypt(stringKey, stringIV, data);
        return new String(bOut, "UTF-8");
    }
}
