package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 选择列
 */
public class DbSelect implements DbColumn, Serializable {

    //region private fields
    private static final long serialVersionUID = 6254815123896285390L;
    private String table;
    private String name;
    private String property;
    private String alias;
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
     * 列
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
     * 获取别名
     *
     * @return
     */
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
