package cn.keyss.client.security.sso;


import cn.keyss.client.esb.ServiceBuilder;
import cn.keyss.client.security.KeyssAbstractAuthenticationProvider;
import cn.keyss.client.security.contract.ExchangeTokenService;
import cn.keyss.client.security.contract.datacontract.ValidateExchangeTokenRequest;
import cn.keyss.client.security.contract.datacontract.ValidateExchangeTokenResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


public class SsoAuthenticationProvider extends KeyssAbstractAuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private ServiceBuilder serviceBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SsoAuthentication.class.isAssignableFrom(authentication));
    }

    @Override
    protected String retriveUsername(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(SsoAuthentication.class, authentication,
                "Only support access token authentication");

        SsoAuthentication keyssAuthentication = (SsoAuthentication) authentication;

        ValidateExchangeTokenRequest request = new ValidateExchangeTokenRequest();
        request.setCallSource("Application:");
        ExchangeTokenService exchangeTokenService = this.serviceBuilder.buildService(ExchangeTokenService.class, null);
        ValidateExchangeTokenResponse response = exchangeTokenService.validateExchangeToken(request);

        if (response.getResultCode() != 0)
            throw new BadCredentialsException("认证失败！");

        return response.getUserName();
    }

    @Override
    protected Authentication createSuccessAuthentication(UserDetails user, Authentication authentication) {
        return new SsoAuthentication(user.getUsername(), null, user.getAuthorities());
    }
}
