package cn.keyss.gateway.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Administrator on 2017/4/7.
 */
public class QueryGatewayApplicationsResponse {
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

    //region services
    private Service[] services;

    @ApiModelProperty(value = "服务列表", required = true)
    @JsonProperty
    public Service[] getServices() {
        return services;
    }

    public void setServices(Service[] services) {
        this.services = services;
    }
    //endregion
}
