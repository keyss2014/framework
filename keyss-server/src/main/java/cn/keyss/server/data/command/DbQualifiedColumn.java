package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 列
 */
public class DbQualifiedColumn implements DbColumn, Serializable {

    //region private fields
    private static final long serialVersionUID = 2170375483632099600L;
    private String table;
    private String name;
    private String property;
    //endregion

    /**
     * 表名
     * 通常只要表别名即可
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
     * 列名称
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
}
