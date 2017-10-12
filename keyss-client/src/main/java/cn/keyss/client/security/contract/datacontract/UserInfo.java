package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(value = "用户信息")
public class UserInfo {
    private String userName;

    @ApiModelProperty(value = "用户名", required = true)
    @JsonProperty(required = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String password;


    private boolean isLocked;

    @ApiModelProperty(value = "是否锁定", required = true)
    @JsonProperty(required = true)
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    private Map<String, String> extraInfos;

    @ApiModelProperty(value = "额外信息", required = true)
    @JsonProperty(required = true)
    public Map<String, String> getExtraInfos() {
        return this.extraInfos;
    }

    public void setExtraInfos(Map<String, String> extraInfos) {
        this.extraInfos = extraInfos;
    }

    @ApiModelProperty(value = "密码", required = true)
    @JsonProperty(required = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
