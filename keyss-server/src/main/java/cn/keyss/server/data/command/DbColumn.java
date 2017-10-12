package cn.keyss.server.data.command;

/**
 * 列
 */
public interface DbColumn {

    /**
     * 表
     *
     * @return
     */
    public String getTable();

    /**
     * 列
     *
     * @return
     */
    public String getName();
}
