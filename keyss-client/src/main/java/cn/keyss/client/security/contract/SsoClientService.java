package cn.keyss.client.security.contract;

import cn.keyss.client.security.contract.datacontract.ValidateSsoTokenRequest;
import cn.keyss.client.security.contract.datacontract.ValidateSsoTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

    @Api(value = "SsoClientService", description = "Sso客户端服务")
    @RequestMapping("/api/SsoClientService")
    public interface SsoClientService {

        @ApiOperation(value = "validateSsoToken", notes = "校验SSO令牌")
        @RequestMapping(path = "/validateSsoToken", method = RequestMethod.POST)
        ValidateSsoTokenResponse validateSsoToken(ValidateSsoTokenRequest request);
    }