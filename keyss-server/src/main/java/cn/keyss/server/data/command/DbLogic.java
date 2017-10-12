package cn.keyss.server.data.command;

/**
 * 过滤关联逻辑.
 */
public enum DbLogic {
    /**
     * 逻辑与
     */
    AND,
    /**
     * 逻辑或
     */
    OR;

    public static DbLogic tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (DbLogic item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
