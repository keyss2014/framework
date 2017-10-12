package cn.keyss.common.rpc.handers;

import cn.keyss.common.context.ApplicationContext;
import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * 上下文处理器
 */
public class ApplicationContextHandler implements RpcInterceptionHandler {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ResponseEntity<?> invoke(Object proxy, Method method, HttpEntity<?> requestEntity, Class<?> responseClass, GetNextRpcInterceptionHandlerDelegate getNextRpcInterceptionHandlerDelegate) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(requestEntity.getHeaders());

        ApplicationContext context = ApplicationContext.getCurrent();

        Enumeration<String> items = context.getKeys();
        while (items.hasMoreElements()){
            String key = items.nextElement();
            if(key.startsWith("x-keyss-")){
                headers.add(key, context.get(key));
            }
        }
        requestEntity = new HttpEntity(requestEntity.getBody(), headers);
        return getNextRpcInterceptionHandlerDelegate.getNextRpcInterceptionHandler().invoke(proxy, method,
                requestEntity, responseClass, getNextRpcInterceptionHandlerDelegate);
    }
}
