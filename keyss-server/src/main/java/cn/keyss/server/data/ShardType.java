package cn.keyss.server.data;

/**
 * 数据库类型
 */
public enum ShardType {
    /**
     * 主库
     */
    MASTER,

    /**
     * 从库
     */
    SLAVE;

    public static ShardType tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (ShardType item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
