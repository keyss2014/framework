package cn.keyss.client.security.accesstoken;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AccessTokenFilter extends GenericFilterBean {

    private AuthenticationManager authenticationManager;
    private long refreshPeriod = 1000 * 60;

    public AccessTokenFilter(AuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated() ) {
              if(authentication.getDetails()==null || !KeyssAuthenticationDetails.class
                      .isAssignableFrom(authentication.getDetails().getClass()))
                  throw new BadCredentialsException("Accesstoken lost!");
              KeyssAuthenticationDetails details = (KeyssAuthenticationDetails)authentication.getDetails();


            if (details.getAccessTokenTime() + this.getRefreshPeriod() < System.currentTimeMillis()) {
                try {
                     //validate token;
                    details.refreshTokenTime();
                } catch (AuthenticationException authenticationException) {
                    SecurityContextHolder.clearContext();
                }
            }
        }
        chain.doFilter(request, response);
    }

    public long getRefreshPeriod() {
        return refreshPeriod;
    }

    public void setRefreshPeriod(long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }
}
