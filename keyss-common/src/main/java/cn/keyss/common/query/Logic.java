package cn.keyss.common.query;

/**
 * 逻辑符
 */
public enum Logic {
    /**
     * 与
     */
    AND(0),
    /**
     * 或
     */
    OR(1);

    private int value;

    Logic(int value) {
        this.value = value;
    }

    public static Logic valueOf(int value) {
        switch (value) {
            case 0:
                return AND;
            case 1:
                return OR;
            default:
                return null;
        }
    }

    public static Logic tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (Logic item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
