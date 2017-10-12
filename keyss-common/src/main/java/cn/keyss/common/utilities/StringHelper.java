package cn.keyss.common.utilities;

/**
 * 字符串帮助类
 */
public abstract class StringHelper {

    /**
     * 两个字符串是否相等
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean areEqual(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 两个字符串是否相等，忽略大小写
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean areEqualIgnoreCase(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        } else {
            if (str2 == null)
                return false;
            else
                return str1.toLowerCase().equals(str2.toLowerCase());
        }
    }
}
