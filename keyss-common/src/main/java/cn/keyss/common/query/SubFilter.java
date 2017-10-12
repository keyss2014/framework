package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 子过滤器
 */
@ApiModel(value = "子过滤器")
public class SubFilter {

    //region private fields
    private Logic logic;

    private Comparison comparison;

    private Object value;
    //endregion

    @ApiModelProperty(value = "过滤逻辑" )
    @JsonProperty
    public Logic getLogic() {
        return logic;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    @ApiModelProperty(value = "过滤操作" )
    @JsonProperty
    public Comparison getComparison() {
        return comparison;
    }

    public void setComparison(Comparison comparison) {
        this.comparison = comparison;
    }

    @ApiModelProperty(value = "过滤值" )
    @JsonProperty
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
