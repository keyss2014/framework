package cn.keyss.client.esb.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件
 * 通常事件先发送至消息中间件，之后再由消息中间件分发给订阅者
 * 相同类型事件通常选择相同的消息中间件，Url为消息中间件地址
 * 初期先不考虑事务消息
 */
@ApiModel(value = "事件")
public class Event {
    //region contract
    private String contract;

    @ApiModelProperty(value = "契约", required = true)
    @JsonProperty(required = true)
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }
    //endregion

    //region url
    private String url;

    @ApiModelProperty(value = "事件分发中间件地址", required = true)
    @JsonProperty(required = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    //endregion
}
