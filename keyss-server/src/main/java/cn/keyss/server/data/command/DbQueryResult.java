package cn.keyss.server.data.command;

import java.util.List;

/**
 * 查询结果实体集
 */
public class DbQueryResult<TEntity> {
    //region private fields
    private long total;
    private List<TEntity> records;
    //endregion

    /**
     * 记录数
     *
     * @return
     */
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 记录集
     *
     * @return
     */
    public List<TEntity> getRecords() {
        return records;
    }

    public void setRecords(List<TEntity> records) {
        this.records = records;
    }
}
