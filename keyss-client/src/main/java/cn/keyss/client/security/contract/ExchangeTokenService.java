package cn.keyss.client.security.contract;

import cn.keyss.client.security.contract.datacontract.CreateExchangeTokenRequest;
import cn.keyss.client.security.contract.datacontract.CreateExchangeTokenResponse;
import cn.keyss.client.security.contract.datacontract.ValidateExchangeTokenRequest;
import cn.keyss.client.security.contract.datacontract.ValidateExchangeTokenResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * 单点登录服务
 */
@Api(value = "ExchangeTokenService", description = "交换令牌服务")
@RequestMapping("/api/ExchangeTokenService")
public interface ExchangeTokenService {

    /**
     * 创建交互令牌
     * <p>
     * 在登录成功后，且将当前站点用户信息转称至其他站点时需要创建交互令牌
     * 在创建交互令牌时仅需提供用户、父令牌、有效期等信息，设备信息并不提供
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "createExchangeToken", notes = "创建交换令牌")
    @RequestMapping(path = "/createExchangeToken", method = RequestMethod.POST)
    CreateExchangeTokenResponse createExchangeToken(@RequestBody @Valid CreateExchangeTokenRequest request);

    /**
     * 验证交互令牌
     *
     * 通常交换令牌只能使用1次，且有效时间在10秒之内，通过校验交换令牌获取父访问令牌信息
     *
     * 系统在后台会创建访问令牌并指定父令牌、用户、设备、有效期等信息
     * @param request
     * @return
     */
    @ApiOperation(value = "validateExchangeToken", notes = "验证交换令牌")
    @RequestMapping(path = "/validateExchangeToken", method = RequestMethod.POST)
    ValidateExchangeTokenResponse validateExchangeToken(@RequestBody @Valid ValidateExchangeTokenRequest request);
}
