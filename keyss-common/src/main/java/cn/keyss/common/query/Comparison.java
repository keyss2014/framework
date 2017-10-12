package cn.keyss.common.query;

/**
 * 比较符
 */
public enum Comparison {

    /**
     * 相等
     */
    EQUALS(0),
    /**
     * 不相等
     */
    NOT_EQUALS(1),
    /**
     * 相似
     */
    LIKE(2),
    /**
     * 不相似
     */
    NOT_LIKE(3),
    /**
     * 大于
     */
    GREATER_THAN(4),
    /**
     * 大于等于
     */
    GREATER_OR_EQUALS(5),
    /**
     * 小于
     */
    LESS_THAN(6),
    /**
     * 小于等于
     */
    LESS_OR_EQUALS(7),
    /**
     * 在...之内
     */
    IN(8),
    /**
     * 不在...之内
     */
    NOT_IN(9),
    /**
     * 在两者之间
     */
    BETWEEN(10),
    /**
     * 不在两者之间
     */
    NOT_BETWEEN(11);

    private int value;

    Comparison(int value) {
        this.value = value;
    }

    public static Comparison valueOf(int value) {
        switch (value) {
            case 0:
                return EQUALS;
            case 1:
                return NOT_EQUALS;
            case 2:
                return LIKE;
            case 3:
                return NOT_LIKE;
            case 4:
                return GREATER_THAN;
            case 5:
                return GREATER_OR_EQUALS;
            case 6:
                return LESS_THAN;
            case 7:
                return LESS_OR_EQUALS;
            case 8:
                return IN;
            case 9:
                return NOT_IN;
            case 10:
                return BETWEEN;
            case 11:
                return NOT_BETWEEN;
            default:
                return null;
        }
    }

    public static Comparison tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (Comparison item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
