package cn.keyss.server.data.command;

/**
 * 表类型
 */
public enum DbTableType {
    /**
     * 正常表
     */
    DEFAULT,
    /**
     * SQL表
     */
    SQL,
    /**
     * 变量表
     */
    VARIABLE;

    /**
     * 解析器
     *
     * @param key
     * @return
     */
    public static DbTableType tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (DbTableType item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
