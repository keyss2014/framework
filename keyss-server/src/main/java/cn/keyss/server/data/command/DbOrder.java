package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 排序列.
 */
public class DbOrder implements DbColumn, Serializable {
    //region private fields
    private static final long serialVersionUID = 8779897448237348938L;
    private String table;
    private String name;
    private String property;
    private DbSorting sorting;
    //endregion

    /**
     * 表
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
     * 名
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
     * @return
     */
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * 排序规则
     *
     * @return
     */
    public DbSorting getSorting() {
        return sorting;
    }

    public void setSorting(DbSorting sorting) {
        this.sorting = sorting;
    }
}

