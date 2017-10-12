package cn.keyss.client.security.sso;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 单点登录令牌
 */
public class SsoAuthentication  extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -8335190245163403984L;

    private String exchangeToken;
    private   Object principal;
    private Object credentials;

    public SsoAuthentication(String exchangeToken) {
        super(null);
        this.exchangeToken =  exchangeToken;
        setAuthenticated(false);
    }

    public SsoAuthentication(Object principal, Object credentials,
                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    public String getExchangeToken() {
        return exchangeToken;
    }
}
