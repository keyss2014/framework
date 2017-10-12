package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "创建交换令牌请求")
public class CreateExchangeTokenRequest {
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

    private String accessToken;

    @ApiModelProperty(value = "原始访问令牌", required = true)
    @JsonProperty(required = true)
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
