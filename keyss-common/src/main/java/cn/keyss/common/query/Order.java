package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 排序属性
 */
@ApiModel(value = "排序")
public class Order {

    //region private fields
    private String property;
    private Sorting sorting;
    //endregion

    @ApiModelProperty(value = "排序属性", required = true)
    @JsonProperty(required = true)
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @ApiModelProperty(value = "排序方向")
    @JsonProperty
    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }
}
