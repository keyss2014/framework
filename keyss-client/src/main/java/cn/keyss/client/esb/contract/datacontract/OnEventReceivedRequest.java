package cn.keyss.client.esb.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * 触发事件请求
 */
@ApiModel(value = "触发事件请求")
public class OnEventReceivedRequest {
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
    private int application;

    @ApiModelProperty(value = "应用", required = true)
    @JsonProperty(required = true)
    public int getApplication() {
        return application;
    }

    public void setApplication(int application) {
        this.application = application;
    }
    //endregion

    //region event id
    private UUID eventID;

    @ApiModelProperty(value = "事件标识", required = true)
    @JsonProperty(required = true)
    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }
    //endregion

    //region contract
    private String contract;

    @ApiModelProperty(value = "事件契约", required = true)
    @JsonProperty(required = true)
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
    //endregion

    //region method
    private String method;

    @ApiModelProperty(value = "方法", required = true)
    @JsonProperty(required = true)
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    //endregion

    //region tags
    private List<String> tags;

    @ApiModelProperty(value = "标签")
    @JsonProperty
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    //endregion

    //region body
    private byte[] body;

    @ApiModelProperty(value = "消息内容")
    @JsonProperty(required = true)
    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
    //endregion
}
