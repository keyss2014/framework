package cn.keyss.client.security.contract;

import cn.keyss.client.security.contract.datacontract.GetUserInfoRequest;
import cn.keyss.client.security.contract.datacontract.GetUserInfoResponse;
import cn.keyss.client.security.contract.datacontract.LoginRequest;
import cn.keyss.client.security.contract.datacontract.LoginResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Api(value = "AuthenticationService", description = "认证服务")
@RequestMapping("/api/AuthenticationService")
public interface AuthenticationService {
    @ApiOperation(value = "login", notes = "登录")
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    LoginResponse login(@RequestBody @Valid LoginRequest request);

    @ApiOperation(value = "getUserInfo", notes = "获取用户信息")
    @RequestMapping(path = "/getUserInfo", method = RequestMethod.POST)
    GetUserInfoResponse getUserInfo(@RequestBody @Valid GetUserInfoRequest request);
}
