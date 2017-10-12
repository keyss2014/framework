package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 列子过滤器
 */
public class DbSubFilter implements Serializable {
    //region private fields
    private static final long serialVersionUID = -8791660709689448946L;
    private DbLogic logic;
    private DbComparison comparison;
    private Object value;
    //endregion

    /**
     * 子语句与或逻辑
     *
     * @return
     */
    public DbLogic getLogic() {
        return logic;
    }

    public void setLogic(DbLogic logic) {
        this.logic = logic;
    }

    /**
     * 比较符
     *
     * @return
     */
    public DbComparison getComparison() {
        return comparison;
    }

    public void setComparison(DbComparison comparison) {
        this.comparison = comparison;
    }

    /**
     * 比较值
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
