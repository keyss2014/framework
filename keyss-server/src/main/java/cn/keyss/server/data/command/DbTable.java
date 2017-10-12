package cn.keyss.server.data.command;

/**
 * 表
 */
public interface DbTable {
    /**
     * 表类型
     *
     * @return
     */
    public DbTableType getTableType();

    /**
     * 架构
     *
     * @return
     */
    public String getSchema();

    /**
     * 名称
     *
     * @return
     */
    public String getName();

    /**
     * 别名
     *
     * @return
     */
    public String getAlias();
}
