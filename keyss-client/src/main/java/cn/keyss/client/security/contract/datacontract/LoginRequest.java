package cn.keyss.client.security.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * 验证请求
 */
@ApiModel(value = "认证用户请求")
public class LoginRequest {
    private String callSource;

    @ApiModelProperty(value = "调用来源", required = true)
    @JsonProperty(required = true)
    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    private String authType;

    @ApiModelProperty(value = "认证方式", required = true)
    @JsonProperty(required = true)
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    private Map<String, String> extraInfos;

    @ApiModelProperty(value = "认证信息", required = true)
    @JsonProperty(required = true)
    public Map<String, String> getExtraInfos() {
        return extraInfos;
    }

    public void setExtraInfos(Map<String, String> extraInfos) {
        this.extraInfos = extraInfos;
    }
}
