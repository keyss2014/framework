package cn.keyss.client.config.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 查询应用配置
 */
@ApiModel(value = "查询应用配置信息请求")
public class QueryApplicationConfigsRequest {

    private String callSource;

    @ApiModelProperty(value = "调用来源", required = true)
    @JsonProperty(required = true)
    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    private int application;

    @ApiModelProperty(value = "应用", required = true)
    @JsonProperty(required = true)
    public int getApplication() {
        return application;
    }

    public void setApplication(int application) {
        this.application = application;
    }
}
