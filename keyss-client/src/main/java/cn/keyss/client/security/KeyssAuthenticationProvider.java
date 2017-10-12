package cn.keyss.client.security;

import cn.keyss.client.esb.ServiceBuilder;
import cn.keyss.client.security.contract.AuthenticationService;
import cn.keyss.client.security.contract.datacontract.LoginRequest;
import cn.keyss.client.security.contract.datacontract.LoginResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * 正常登录验证器
 */
public class KeyssAuthenticationProvider extends KeyssAbstractAuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());
    private AuthenticationService authenticationService;

    public KeyssAuthenticationProvider(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Autowired
    private ServiceBuilder serviceBuilder;

    @Override
    public boolean supports(Class<?> authentication) {
        return (KeyssAuthentication.class
                .isAssignableFrom(authentication));
    }

    @Override
    protected String retriveUsername(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(KeyssAuthentication.class, authentication,
                "Only support keyss authentication");

        KeyssAuthentication keyssAuthentication = (KeyssAuthentication) authentication;

        LoginRequest request = new LoginRequest();
        request.setCallSource("Application:");
        request.setAuthType(keyssAuthentication.getAuthType());
        request.setExtraInfos(keyssAuthentication.getExtraInfos());
        LoginResponse response = authenticationService.login(request);

        if (response.getResultCode() != 0)
            throw new BadCredentialsException("认证失败！");

        return response.getUserName();
    }

    @Override
    protected Authentication createSuccessAuthentication(UserDetails user, Authentication authentication) {
        return new KeyssAuthentication(user.getUsername(), null, user.getAuthorities());
    }
}
