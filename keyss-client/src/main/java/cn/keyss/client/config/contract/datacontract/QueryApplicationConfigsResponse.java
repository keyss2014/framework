package cn.keyss.client.config.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 查询应用配置响应
 */
@ApiModel(value = "查询应用配置信息响应")
public class QueryApplicationConfigsResponse {

    //region result code
    private int resultCode;

    @ApiModelProperty(value = "结果编码", required = true)
    @JsonProperty(required = true)
    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    //endregion

    //region result message
    private String resultMessage;

    @ApiModelProperty(value = "结果描述", required = true)
    @JsonProperty(required = true)
    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
    //endregion

    //region configs
    private List<Config> configs;

    @ApiModelProperty(value = "配置" )
    @JsonProperty
    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }
    //endregion
}
