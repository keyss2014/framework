package cn.keyss.server.data.command;

import java.io.Serializable;

/**
 * 连接表.
 */
public class DbJoinTable implements DbTable, Serializable {
    //region private fields
    private static final long serialVersionUID = 5579281770243899226L;
    private DbJoinType type;
    private DbTableType tableType;
    private String schema;
    private String name;
    private String alias;
    private DbJoinCondition[] conditions;
    //endregion

    /**
     * 连接类型
     *
     * @return
     */
    public DbJoinType getType() {
        return type;
    }

    public void setType(DbJoinType type) {
        this.type = type;
    }

    /**
     * 表类型
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
     * 名称
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
     * 别名
     * @return
     */
    @Override
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 连接条件
     *
     * @return
     */
    public DbJoinCondition[] getConditions() {
        return conditions;
    }

    public void setConditions(DbJoinCondition[] conditions) {
        this.conditions = conditions;
    }
}
