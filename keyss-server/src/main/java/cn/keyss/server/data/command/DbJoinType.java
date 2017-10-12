package cn.keyss.server.data.command;

/**
 * 连接类型
 */
public enum DbJoinType {
    /**
     * 内连接
     */
    INNER_JOIN,
    /**
     * 外连接
     */
    OUTTER_JOIN,
    /**
     * 左连接
     */
    LEFT_JOIN,
    /**
     * 右连接
     */
    RIGHT_JOIN;


    public static DbJoinType tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (DbJoinType item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
