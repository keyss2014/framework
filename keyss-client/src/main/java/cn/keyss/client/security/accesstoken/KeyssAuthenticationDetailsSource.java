package cn.keyss.client.security.accesstoken;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/4/27.
 */
public class KeyssAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, KeyssAuthenticationDetails> {

    // ~ Methods
    // ========================================================================================================

    /**
     * @param context the {@code HttpServletRequest} object.
     * @return the {@code WebAuthenticationDetails} containing information about the
     * current request
     */
    public KeyssAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new KeyssAuthenticationDetails(context);
    }
}
