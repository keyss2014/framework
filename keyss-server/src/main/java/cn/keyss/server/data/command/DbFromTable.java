package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 数据库表
 */
public class DbFromTable implements DbTable, Serializable {

    //region private fields
    private static final long serialVersionUID = -5491875610330655500L;
    private DbTableType tableType;
    private String schema;
    private String name;
    private String alias;
    //endregion

    /**
     * 数据表类型
     *
     * @return
     */
    @Override
    public DbTableType getTableType() {
        return tableType;
    }

    public void setTableType(DbTableType tableType) {
        this.tableType = tableType;
    }

    /**
     * 架构
     *
     * @return
     */
    @Override
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 表
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
     * 表别名
     *
     * @return
     */
    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
