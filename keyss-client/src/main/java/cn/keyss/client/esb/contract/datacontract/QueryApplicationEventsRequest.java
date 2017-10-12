package cn.keyss.client.esb.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "查询应用可触发事件")
public class QueryApplicationEventsRequest {
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
}
