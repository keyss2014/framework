package cn.keyss.gateway.contract.datacontract;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 服务
 *
 * 每个服务通常会有多个服务提供者，每个提供者的标签值不同，客户端通常设置标签来选择合适的服务地址
 */
@ApiModel(value = "服务")
public class Service {
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

    @ApiModelProperty(value = "事件地址", required = true)
    @JsonProperty(required = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    //endregion

    //region tags
    private String[] tags;

    @ApiModelProperty(value = "标签")
    @JsonProperty
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    //endregion
}
