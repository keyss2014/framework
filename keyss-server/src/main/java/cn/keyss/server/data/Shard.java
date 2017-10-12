package cn.keyss.server.data;

import javax.sql.DataSource;

/**
 * 数据库分片
 */
public class Shard {

    //region private fields
    private ShardType shardType = ShardType.MASTER;
    private String shard = "";
    private int queryTimeout = -1;
    private int ratio = 1;
    private DataSource dataSource;
    //endregion

    /**
     * 分区类型
     *
     * @return
     */
    public ShardType getShardType() {
        return shardType;
    }

    public void setShardType(ShardType shardType) {
        this.shardType = shardType;
    }

    /**
     * 分区
     *
     * @return
     */
    public String getShard() {
        return shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    /**
     * 数据源名称
     *
     * @return
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 权重
     *
     * @return
     */
    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    /**
     * 查询超时时长
     * @return
     */
    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }
}
