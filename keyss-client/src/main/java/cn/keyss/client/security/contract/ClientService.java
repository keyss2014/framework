package cn.keyss.client.security.contract;


import cn.keyss.client.security.contract.datacontract.GetClientInfoRequest;
import cn.keyss.client.security.contract.datacontract.GetClientInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "ClientService", description = "客户端信息服务")
@RequestMapping("/api/ClientService")
public interface ClientService {

    @ApiOperation(value = "getClientInfo", notes = "获取客户信息")
    @RequestMapping(path = "/getClientInfo", method = RequestMethod.POST)
    GetClientInfoResponse getClientInfo(@RequestBody @Valid GetClientInfoRequest request);
}
