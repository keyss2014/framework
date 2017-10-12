package cn.keyss.client.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KeyssAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -8335190245163403984L;

    private String authType;
    private   Object principal;
    private Object credentials;
    private Map<String, String> extraInfos = new HashMap<>();

    public Map<String, String> getExtraInfos() {
        return extraInfos;
    }

    public KeyssAuthentication(String authType) {
        super(null);
        this.authType = authType;
        setAuthenticated(false);
    }

    public KeyssAuthentication(Object principal, Object credentials,
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

    public String getAuthType() {
        return authType;
    }
}
