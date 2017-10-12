package cn.keyss.client.security.sso;

import cn.keyss.client.autoconfig.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/8.
 */
public class SsoClientFilter extends GenericFilterBean {

    public SsoClientFilter(String serverUrl){
        this.serverUrl = serverUrl;
    }

    private RequestMatcher ssoRequestMatcher = new AntPathRequestMatcher("/sso/**");
    private String serverUrl;

    private static AntPathRequestMatcher signInMatcher = new AntPathRequestMatcher("/sso/signin?backurl={backurl}&sign={sign}&token={token}");
    private static AntPathRequestMatcher signOutMatcher = new AntPathRequestMatcher("/sso/signout");
    private static AntPathRequestMatcher loginMatcher = new AntPathRequestMatcher("/sso/login");

    private AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final boolean debug = this.logger.isDebugEnabled();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (!ssoRequestMatcher.matches(request)) {
            chain.doFilter(req, res);
            return;
        }
        if(loginMatcher.matches(request)){
            //
            String serverSignInUrl = serverUrl + "/sso/signin";
            response.sendRedirect(serverSignInUrl);
            return;

        }
        else if (signInMatcher.matches(request)) {

           String token = request.getParameter("token");
           SsoAuthentication authentication = new SsoAuthentication(token);
            Authentication auth = this.authenticationManager.authenticate(authentication);
            if(auth.isAuthenticated()){
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } else if (signOutMatcher.matches(request)) {
            SecurityContextHolder.clearContext();
            String serverSignInUrl = serverUrl + "/sso/signout";
            response.sendRedirect(serverSignInUrl);
            return;
        }

    }
}
