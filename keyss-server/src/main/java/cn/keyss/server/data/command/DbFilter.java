package cn.keyss.server.data.command;

import java.io.Serializable;
import java.util.List;

/**
 * 列过滤器
 * <p>
 * 列之间的过滤器关系为与
 * 列及列子过滤器的关系为或
 */
public class DbFilter implements DbColumn, Serializable {
    //region private fields
    private static final long serialVersionUID = -2747538944953328936L;
    private String table;
    private String name;
    private String property;
    private DbComparison comparison;
    private Object value;
    private List<DbSubFilter> subFilters;
    //endregion

    /**
     * 表
     *
     * @return
     */
    @Override
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    /**
     * 列
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 属性
     *
     * @return
     */
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
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

    /**
     * 子过滤器
     *
     * @return
     */
    public List<DbSubFilter> getSubFilters() {
        return subFilters;
    }

    public void setSubFilters(List<DbSubFilter> subFilters) {
        this.subFilters = subFilters;
    }


}
