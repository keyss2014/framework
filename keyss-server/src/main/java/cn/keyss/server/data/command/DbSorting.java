package cn.keyss.server.data.command;

/**
 * 列排序
 */
public enum DbSorting {
    /**
     * 升序
     */
    ASCEDING,

    /**
     * 降序
     */
    DESCEDING;

    public static DbSorting tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (DbSorting item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
