package cn.keyss.client.security.contract;

import cn.keyss.client.security.contract.datacontract.CreateAccessTokenRequest;
import cn.keyss.client.security.contract.datacontract.CreateAccessTokenResponse;
import cn.keyss.client.security.contract.datacontract.ValidateAccessTokenRequest;
import cn.keyss.client.security.contract.datacontract.ValidateAccessTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "AccessTokenService", description = "访问令牌服务")
@RequestMapping("/api/AccessTokenService")
public interface AccessTokenService {

    @ApiOperation(value = "createAccessToken", notes = "创建访问令牌")
    @RequestMapping(path = "/createAccessToken", method = RequestMethod.POST)
    CreateAccessTokenResponse createAccessToken(CreateAccessTokenRequest request);

    @ApiOperation(value = "validateAccessToken", notes = "校验访问令牌")
    @RequestMapping(path = "/validateAccessToken", method = RequestMethod.POST)
    ValidateAccessTokenResponse validateAccessToken(ValidateAccessTokenRequest request);
}
