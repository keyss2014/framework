package cn.keyss.common.rpc.handers;

import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;


public class JsonHeaderHandler implements RpcInterceptionHandler {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ResponseEntity<?> invoke(Object proxy, Method method, HttpEntity<?> requestEntity, Class<?> responseClass, GetNextRpcInterceptionHandlerDelegate getNextRpcInterceptionHandlerDelegate) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(requestEntity.getHeaders());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        requestEntity = new HttpEntity(requestEntity.getBody(), headers);
        return getNextRpcInterceptionHandlerDelegate.getNextRpcInterceptionHandler().invoke(proxy, method,
                requestEntity, responseClass, getNextRpcInterceptionHandlerDelegate);
    }
}
