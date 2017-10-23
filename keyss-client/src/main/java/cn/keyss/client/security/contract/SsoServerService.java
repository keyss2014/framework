package cn.keyss.client.security.contract;


import cn.keyss.client.security.contract.datacontract.CreateSsoTokenRequest;
import cn.keyss.client.security.contract.datacontract.CreateSsoTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(value = "SsoServerService", description = "Sso服务端服务")
@RequestMapping("/api/SsoServerService")
public interface SsoServerService {

    @ApiOperation(value = "createSsoToken", notes = "创建SSO令牌")
    @RequestMapping(path = "/createSsoToken", method = RequestMethod.POST)
    CreateSsoTokenResponse createSsoToken(CreateSsoTokenRequest request);
}
