package cn.keyss.common.rpc.filters;

import cn.keyss.common.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ApplicationContextFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        ApplicationContext applicationContext = ApplicationContext.getCurrent();
        Enumeration<String> enu=request.getHeaderNames();
        while(enu.hasMoreElements()) {
            String headerName =  enu.nextElement();
            if(headerName.startsWith("x-keyss-")){
                applicationContext.set(headerName, request.getHeader(headerName));
            }
        }
        chain.doFilter(req, res);
    }
}
