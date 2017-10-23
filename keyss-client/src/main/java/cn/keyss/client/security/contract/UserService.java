package cn.keyss.client.security.contract;

import cn.keyss.client.security.contract.datacontract.GetUserInfoRequest;
import cn.keyss.client.security.contract.datacontract.GetUserInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "UserService", description = "用户信息服务")
@RequestMapping("/api/UserService")
public interface UserService {
    @ApiOperation(value = "getUserInfo", notes = "获取用户信息")
    @RequestMapping(path = "/getUserInfo", method = RequestMethod.POST)
    GetUserInfoResponse getUserInfo(@RequestBody @Valid GetUserInfoRequest request);
}
