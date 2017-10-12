package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 条件列
 */
public class DbConditionColumn implements DbColumn, Serializable {
    //region private fields
    private String table;
    private String name;
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
}
