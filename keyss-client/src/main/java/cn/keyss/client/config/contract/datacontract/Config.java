package cn.keyss.client.config.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 配置信息
 */
@ApiModel(value = "配置")
public class Config {

    private String name;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @JsonProperty(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String value;

    /**
     * 值
     */
    @ApiModelProperty(value = "值", required = true)
    @JsonProperty(required = true)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
