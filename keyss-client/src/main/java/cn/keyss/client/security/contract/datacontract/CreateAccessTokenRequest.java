package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "创建访问令牌请求")
public class CreateAccessTokenRequest {
    private String callSource;

    @ApiModelProperty(value = "调用来源", required = true)
    @JsonProperty(required = true)
    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    private String userName;

    @ApiModelProperty(value = "用户名称", required = true)
    @JsonProperty(required = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String device;

    @ApiModelProperty(value = "绑定设备")
    @JsonProperty(required = true)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    private int validatePeriod;

    @ApiModelProperty(value = "有效时长秒为单位")
    @JsonProperty(required = true)
    public int getValidatePeriod() {
        return validatePeriod;
    }

    public void setValidatePeriod(int validatePeriod) {
        this.validatePeriod = validatePeriod;
    }

    private String parentAccessToken;

    @ApiModelProperty(value = "父访问令牌")
    @JsonProperty(required = true)
    public String getParentAccessToken() {
        return parentAccessToken;
    }

    public void setParentAccessToken(String parentAccessToken) {
        this.parentAccessToken = parentAccessToken;
    }
}
