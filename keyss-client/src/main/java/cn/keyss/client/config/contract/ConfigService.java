package cn.keyss.client.config.contract;

import cn.keyss.client.config.contract.datacontract.QueryApplicationConfigsRequest;
import cn.keyss.client.config.contract.datacontract.QueryApplicationConfigsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * 配置服务
 */
@Api(value = "ConfigService", description = "应用配置服务")
@RequestMapping("/api/ConfigService")
public interface ConfigService {

    /**
     * 查询应用配置
     * @param request
     * @return
     */
    @ApiOperation(value = "queryApplicationConfigs", notes = "查询应用配置")
    @RequestMapping(path = "/queryApplicationConfigs", method = RequestMethod.POST)
    QueryApplicationConfigsResponse queryApplicationConfigs(@RequestBody @Valid QueryApplicationConfigsRequest request);
}
