package cn.keyss.client.security;

import cn.keyss.client.security.contract.AuthenticationService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KeyssAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String authTypeParameter = "auth_type";
    private Map<String, CredentialRetriver> credentialRetrivers;

    public KeyssAuthenticationFilter(Map<String ,CredentialRetriver> credentialRetrivers) {
        super(new AntPathRequestMatcher("/account/login", "POST"));
        if(credentialRetrivers == null || credentialRetrivers.size() == 0){
           this.credentialRetrivers = new HashMap<>();
           this.credentialRetrivers.put("NULL", new CredentialRetriver() {
               @Override
               public boolean support(String authenticationType) {
                   return true;
               }
               @Override
               public Map<String, String> retrieve(HttpServletRequest request) {
                   return new HashMap<>();
               }
           });
        }else {
            this.credentialRetrivers = credentialRetrivers;
        }
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(this.credentialRetrivers, "A credentialRetrivers must be set");
    }

    protected void setDetails(HttpServletRequest request,
                              KeyssAuthentication authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String authType = request.getParameter(this.authTypeParameter);
        if (authType == null)
           authType = "default";

        Map<String, String> parameters = null;
        for (CredentialRetriver retriver : this.credentialRetrivers.values()) {
            if (retriver.support(authType)) {
                parameters = retriver.retrieve(request);
                break;
            }
        }

        if (parameters == null)
            throw new AuthenticationCredentialsNotFoundException("没有支持的认证参数获取器！");

        KeyssAuthentication authRequest = new KeyssAuthentication(authType);
        authRequest.getExtraInfos().putAll(parameters);
        this.setDetails(request, authRequest);
        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

        return authentication;
    }
}
