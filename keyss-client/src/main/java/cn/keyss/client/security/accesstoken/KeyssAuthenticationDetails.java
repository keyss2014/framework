package cn.keyss.client.security.accesstoken;

import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/27.
 */
public class KeyssAuthenticationDetails implements Serializable {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    // ~ Instance fields
    // ================================================================================================

    private final String remoteAddress;
    private final String sessionId;
    private final String accessToken;
    private long accessTokenTime;


    // ~ Constructors
    // ===================================================================================================

    /**
     * Records the remote address and will also set the session Id if a session already
     * exists (it won't create one).
     *
     * @param request that the authentication request was received from
     */
    public KeyssAuthenticationDetails(HttpServletRequest request) {
        this.remoteAddress = request.getRemoteAddr();

        HttpSession session = request.getSession(false);
        this.sessionId = (session != null) ? session.getId() : null;
        this.accessToken = (String)session.getAttribute("token");
        this.accessTokenTime = System.currentTimeMillis();
    }

    // ~ Methods
    // ========================================================================================================

    public boolean equals(Object obj) {
        if (obj instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails rhs = (WebAuthenticationDetails) obj;

            if ((remoteAddress == null) && (rhs.getRemoteAddress() != null)) {
                return false;
            }

            if ((remoteAddress != null) && (rhs.getRemoteAddress() == null)) {
                return false;
            }

            if (remoteAddress != null) {
                if (!remoteAddress.equals(rhs.getRemoteAddress())) {
                    return false;
                }
            }

            if ((sessionId == null) && (rhs.getSessionId() != null)) {
                return false;
            }

            if ((sessionId != null) && (rhs.getSessionId() == null)) {
                return false;
            }

            if (sessionId != null) {
                if (!sessionId.equals(rhs.getSessionId())) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Indicates the TCP/IP address the authentication request was received from.
     *
     * @return the address
     */
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Indicates the <code>HttpSession</code> id the authentication request was received
     * from.
     *
     * @return the session ID
     */
    public String getSessionId() {
        return sessionId;
    }

    public int hashCode() {
        int code = 7654;

        if (this.remoteAddress != null) {
            code = code * (this.remoteAddress.hashCode() % 7);
        }

        if (this.sessionId != null) {
            code = code * (this.sessionId.hashCode() % 7);
        }

        return code;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("RemoteIpAddress: ").append(this.getRemoteAddress()).append("; ");
        sb.append("SessionId: ").append(this.getSessionId());

        return sb.toString();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getAccessTokenTime() {
        return accessTokenTime;
    }

    public void refreshTokenTime() {

        this.accessTokenTime = System.currentTimeMillis();
    }
}
