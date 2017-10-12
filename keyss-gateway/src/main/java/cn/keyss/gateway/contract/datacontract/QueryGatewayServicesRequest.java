package cn.keyss.gateway.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "查询应用可通过网关访问服务")
public class QueryGatewayServicesRequest {
    //region call source
    private String callSource;

    @ApiModelProperty(value = "调用来源", required = true)
    @JsonProperty(required = true)
    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }
    //endregion

    //region application
    private String application;

    @ApiModelProperty(value = "应用", required = true)
    @JsonProperty(required = true)
    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    //endregion
}
