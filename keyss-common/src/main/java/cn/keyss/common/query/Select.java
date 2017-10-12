package cn.keyss.common.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 选择列
 */
@ApiModel(value = "选择列")
public class Select {
    //region pivate fields
    private String property;
    private String alias;
    //endregion

    @ApiModelProperty(value = "属性", required = true)
    @JsonProperty(required = true)
    public String getProperty() {
        return this.property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @ApiModelProperty(value = "别名")
    @JsonProperty
    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
