package cn.keyss.client.security;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 认证凭据获取器
 */
public interface CredentialRetriver {

    /**
     * 是否支持认证类型
     *
     * @param authenticationType
     * @return
     */
    boolean support(String authenticationType);

    /**
     * 获取认证凭证
     *
     * @param request
     * @return
     */
    Map<String, String> retrieve(HttpServletRequest request);
}
