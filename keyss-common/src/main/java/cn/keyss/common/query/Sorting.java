package cn.keyss.common.query;

/**
 * 排序
 */
public enum Sorting {
    /**
     * 升序
     */
    ASCEDING(0),
    /**
     * 降序
     */
    DESCEDING(1);

    private int value;

    Sorting(int value) {
        this.value = value;
    }

    public static Sorting valueOf(int value) {
        switch (value) {
            case 0:
                return ASCEDING;
            case 1:
                return DESCEDING;
            default:
                return null;
        }
    }

    public static Sorting tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (Sorting item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}

