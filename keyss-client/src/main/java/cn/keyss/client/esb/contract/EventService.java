package cn.keyss.client.esb.contract;

import cn.keyss.client.esb.contract.datacontract.OnEventReceivedRequest;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * 事件服务
 */
@Api(value = "EventService", description = "Esb服务")
@RequestMapping("/api/EventService")
public interface EventService {

    /**
     * 触发事件
     * @param request
     * @return
     */
    @ApiOperation(value = "onEventReceived", notes = "触发事件到达消息")
    @RequestMapping(path = "/onEventReceived", method = RequestMethod.POST)
    OnEventReceivedResponse onEventReceived(@RequestBody @Valid OnEventReceivedRequest request);
}
