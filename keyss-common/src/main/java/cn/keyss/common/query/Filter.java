package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 属性过滤器
 */
@ApiModel(value = "过滤器")
public class Filter {

    //region private fields
    private String property;

    private Comparison comparison;

    private Object value;

    private List<SubFilter> subFilters;
    //endregion

    @ApiModelProperty(value = "属性", required = true)
    @JsonProperty(required = true)
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @ApiModelProperty(value = "过滤操作")
    @JsonProperty
    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    @ApiModelProperty(value = "过滤值")
    @JsonProperty
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @ApiModelProperty(value = "子过滤器")
    @JsonProperty(value = "subFilters")
    public List<SubFilter> getSubFilters() {
        return subFilters;
    }

    public void setSubFilters(List<SubFilter> subFilters) {
        this.subFilters = subFilters;
    }
}
