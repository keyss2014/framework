package cn.keyss.common.secret;


import java.security.MessageDigest;
import java.util.Base64;

/**
 * MD5验签帮助器
 */
public class Md5HashHelper {
    /**
     * 计算哈希
     *
     * @param data 原始字符串
     * @param salt 盐
     * @return 哈希值
     * @throws Exception
     */
    public static String computeHash(String data, String salt) throws Exception {
        String resultString = data + salt;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] result = md.digest(resultString.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 验证哈希值
     *
     * @param data 原始字符串
     * @param salt 盐
     * @param hash 预测哈希值
     * @return 是否验证成功
     * @throws Exception
     */
    public static boolean compareHash(String data, String salt, String hash) throws Exception {
        String hash1 = computeHash(data, salt);
        return hash1.equals(hash);
    }

    public static String computeHash(String msg) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] inputByteArray = msg.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }
}