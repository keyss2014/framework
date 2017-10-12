package cn.keyss.server.data.command;

/**
 * 数据比较
 */
public enum DbComparison {
    EQUALS,//等于
    NOT_EQUALS,//不等于
    LIKE,//类似
    NOT_LIKE,//不类似
    GREATER_THAN,//大于
    GREATER_OR_EQUALS,//大于等于
    LESS_THAN,//小于
    LESS_OR_EQUALS,//小于等于
    IN,//在...之内
    NOT_IN,//不在...之内
    BETWEEN,//在...之间
    NOT_BETWEEN//不在...之间
    ;

    public static DbComparison tryGet(String key) {
        if (key != null && !"".equals(key.trim())) {
            for (DbComparison item : values()) {
                if (item.name().toLowerCase().equals(key.trim().toLowerCase()))
                    return item;
            }
        }
        return null;
    }
}
