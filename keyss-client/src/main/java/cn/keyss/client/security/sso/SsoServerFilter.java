package cn.keyss.client.security.sso;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
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

public class SsoServerFilter extends GenericFilterBean {
    private RequestMatcher ssoRequestMatcher =   new AntPathRequestMatcher( "/sso/**");
    private AuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/login");

    private static AntPathRequestMatcher signInMatcher = new AntPathRequestMatcher("/sso/signin?app={app}&backurl={backurl}&sign={sign}");
    private static AntPathRequestMatcher signOutMatcher = new AntPathRequestMatcher("/sso/signout");

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final boolean debug = this.logger.isDebugEnabled();
        HttpServletRequest  request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;
        if(!ssoRequestMatcher.matches(request)) {
             chain.doFilter(req, res);
             return;
        }

        if(signInMatcher.matches(request)){

            SecurityContext context = SecurityContextHolder.getContext();
            if(context!=null){
                Authentication authentication = context.getAuthentication();
                if(authentication != null && authentication.isAuthenticated()){
                    Map<String,String> tokens =  signInMatcher.extractUriTemplateVariables(request);
                    //check sign
                    String backUrl = tokens.get("BackUrl");
                    //create token and return;
                    String token = "";
                    if(backUrl.contains("?")) {
                        backUrl = backUrl + "?token=" + token;
                    }else{
                        backUrl = backUrl + "&token=" + token;
                    }
                    response.sendRedirect(backUrl);
                    return;
                }
            }

            request.getSession().setAttribute("BackUrl", request.getRequestURI());
            this.authenticationEntryPoint.commence(request,response, null);
            return;
        }
        else if(signOutMatcher.matches(request)){
            SecurityContextHolder.clearContext();
            this.authenticationEntryPoint.commence(request, response, null);
            return;
        }
        else
        {
            logger.warn("no action detected!");
        }
    }
}
