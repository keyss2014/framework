package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "获取用户信息响应")
public class GetUserInfoResponse {
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

    private UserInfo userInfo;

    @ApiModelProperty(value = "用户信息")
    @JsonProperty
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
