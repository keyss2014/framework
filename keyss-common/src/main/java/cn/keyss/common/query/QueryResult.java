package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 查询结果
 */
public class QueryResult<TData> {

    //region private fields
    private long total;
    private List<TData> records;
    //endregion
    @ApiModelProperty(value = "记录总数", required = true)
    @JsonProperty(required = true)
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @ApiModelProperty(value = "记录")
    @JsonProperty(value="records")
    public List<TData> getRecords() {
        return records;
    }

    public void setRecords(List<TData> records) {
        this.records = records;
    }
}
