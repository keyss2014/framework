package cn.keyss.client.esb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 标签帮助器
 */
public class TagsHelper {

    /***
     * 解析标签字符串
     * @param tagsString
     * @return
     */
    public static Map<String,String > parseTagsString(String tagsString){
        Map<String,String> result = new HashMap<>();

        if (tagsString != null && tagsString.length() != 0) {
            String[] tags = tagsString.split(";");
            for (String tag : tags) {
                if (tag!=null && tag.length() != 0) {
                    String[] keyValuePair = tag.split(":");
                    if(keyValuePair.length == 2
                            && keyValuePair[0]!=null && keyValuePair[0].length() != 0
                            && keyValuePair[1]!=null && keyValuePair[1].length() != 0)
                        result.put(keyValuePair[0], keyValuePair[1]);
                }
            }
        }
        return result;
    }


    /***
     * 将标签转换为标签字符串
     * @param tags
     * @return
     */
    public static String convertToTagString(List<String> tags){
        StringBuilder stringBuilder = new StringBuilder();
        if(tags!=null && tags.size() !=0) {
            for(int i =0; i< tags.size(); i++) {
                stringBuilder.append(tags.get(i));
                if(i!=tags.size() -1)
                    stringBuilder.append(";");
            }
        }
        return stringBuilder.toString();
    }
    /***
     * 合并并转换标签
     * @param srcTags 源标签
     * @param defaultTags 缺省标签
     * @return 合并并转换后的标签
     */
    public static List<String> mergeTags(Map<String, String> srcTags, Map<String, String> defaultTags) {
        if(srcTags == null )
            srcTags = new HashMap<>();

        List<String> replaceTags = new ArrayList<>();
        if (defaultTags != null && defaultTags.size() > 0) {
            for (String key : defaultTags.keySet()) {
                //将没有被覆盖的标签写入
                if(!srcTags.containsKey(key))
                    replaceTags.add(key + ":" + defaultTags.get(key));
            }
        }
        for(String key : srcTags.keySet()){
            replaceTags.add(key + ":" + srcTags.get(key));
        }
        return replaceTags;
    }

    /***
     * 标签是否包含
     * @param dstTags
     * @param srcTags
     * @return
     */
    public static boolean contains(List<String> dstTags, List<String> srcTags) {
        if (dstTags == null || dstTags.size() == 0)
            return true;

        if (srcTags == null || srcTags.size() == 0)
            return false;

        for (String t : dstTags) {
            if (!srcTags.contains(t)) {
                return false;
            }
        }
        return true;
    }
}
