package cn.keyss.client.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KeyssAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String SPRING_SECURITY_FORM_AUTHCODE_KEY = "authcode";

    /***
     * 增加验证码校验
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String authCode = request.getParameter(SPRING_SECURITY_FORM_AUTHCODE_KEY);

        if (authCode == null) {
            authCode = "";
        }

        String sessionAuthCode = (String)request.getSession().getAttribute(SPRING_SECURITY_FORM_AUTHCODE_KEY);
        if(!authCode.equals(sessionAuthCode)) {
            throw new BadCredentialsException("Auth code无效！");
        }
        return super.attemptAuthentication(request, response);
    }
}
