package cn.keyss.client.esb.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "查询应用事件响应")
public class QueryApplicationEventsResponse {
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

    //region events
    private List<Event> events;

    @ApiModelProperty(value = "事件列表", required = true)
    @JsonProperty
    public  List<Event>  getEvents() {
        return events;
    }

    public void setEvents( List<Event>  events) {
        this.events = events;
    }
    //endregion
}
