package cn.keyss.common.rpc;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

/**
 * 远程调用注入处理器
 */
public interface RpcInterceptionHandler {

    /**
     * 注入方法
     *
     * @param proxy                                 代理
     * @param method                                方法
     * @param requestEntity                         请求实体
     * @param responseClass                         响应Body类型
     * @param getNextRpcInterceptionHandlerDelegate 获取下一个代理链
     * @return 响应实体
     */
    ResponseEntity<?> invoke(Object proxy, Method method, HttpEntity<?> requestEntity, Class<?> responseClass, GetNextRpcInterceptionHandlerDelegate getNextRpcInterceptionHandlerDelegate);
}
