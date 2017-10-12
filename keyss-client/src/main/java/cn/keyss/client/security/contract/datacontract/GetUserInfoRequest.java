package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "获取用户信息请求")
public class GetUserInfoRequest {
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

    @ApiModelProperty(value = "用户名", required = true)
    @JsonProperty(required = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
