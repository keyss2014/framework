package cn.keyss.client.security;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/2.
 */
public class KeyssCredentialRetriver implements CredentialRetriver {
    @Override
    public boolean support(String authenticationType) {
        if("default".equals(authenticationType)) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> retrieve(HttpServletRequest request) {
        HashMap<String,String> paras = new HashMap<>();
        paras.put("UserName", request.getParameter("UserName"));
        paras.put("Password",request.getParameter("Password"));
        paras.put("RemberMe", request.getParameter("RemberMe"));
        paras.put("AuthCode",request.getParameter("Authcode"));
        String kaptchaExpected = (String) request.getSession().getAttribute( com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        paras.put("OldAuthCode", kaptchaExpected);

        return paras;
    }
}
