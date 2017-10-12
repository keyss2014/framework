package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel(value = "查询条件")
public class QueryCriteria {

    //region private fields
    private int startIndex;

    private int maxRecordReturn;

    private List<Select> selects;

    private List<Order> orders;

    private Map<String,Object> parameters;

    private List<List<Filter>> filters;
    //endregion

    @ApiModelProperty(value = "开始索引")
    @JsonProperty
    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    @ApiModelProperty(value = "最大返回记录数")
    @JsonProperty
    public int getMaxRecordReturn() {
        return maxRecordReturn;
    }

    public void setMaxRecordReturn(int maxRecordReturn) {
        this.maxRecordReturn = maxRecordReturn;
    }

    @ApiModelProperty(value = "选择属性")
    @JsonProperty(value = "selects")
    public List<Select> getSelects() {
        return selects;
    }

    public void setSelects(List<Select> selects) {
        this.selects = selects;
    }

    @ApiModelProperty(value = "排序属性" )
    @JsonProperty(value = "orders")
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @ApiModelProperty(value = "参数" )
    @JsonProperty(value = "parameters")
    public Map<String,Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String,Object> parameters) {
        this.parameters = parameters;
    }

    @ApiModelProperty(value = "过滤条件" )
    @JsonProperty(value = "filters")
    public List<List<Filter>> getFilters() {
        return filters;
    }

    public void setFilters(List<List<Filter>> filters) {
        this.filters = filters;
    }
}
