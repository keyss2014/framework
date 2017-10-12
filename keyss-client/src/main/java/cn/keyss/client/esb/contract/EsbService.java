package cn.keyss.client.esb.contract;

import cn.keyss.client.esb.contract.datacontract.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Esb服务
 */
@Api(value = "EsbService", description = "Esb服务")
@RequestMapping("/api/EsbService")
public interface EsbService {

    /**
     * 查询应用可调用服务
     * @param request
     * @return
     */
    @ApiOperation(value = "queryApplicationServices", notes = "查询应用可调用服务")
    @RequestMapping(path = "/queryApplicationServices", method = RequestMethod.POST)
    QueryApplicationServicesResponse queryApplicationServices(@RequestBody @Valid QueryApplicationServicesRequest request);

    /**
     * 查询应用可触发事件
     * @param request
     * @return
     */
    @ApiOperation(value = "queryApplicationEvents", notes = "查询应用可触发事件")
    @RequestMapping(path = "/queryApplicationEvents", method = RequestMethod.POST)
    QueryApplicationEventsResponse queryApplicationEvents(@RequestBody @Valid QueryApplicationEventsRequest request);
}
