package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "校验访问令牌请求")
public class ValidateAccessTokenRequest {
    private String callSource;

    @ApiModelProperty(value = "调用来源", required = true)
    @JsonProperty(required = true)
    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    private String accessToken;

    @ApiModelProperty(value = "访问令牌", required = true)
    @JsonProperty(required = true)
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String device;

    @ApiModelProperty(value = "设备标识", required = true)
    @JsonProperty(required = true)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
