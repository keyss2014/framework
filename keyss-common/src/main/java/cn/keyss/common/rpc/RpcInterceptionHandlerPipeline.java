package cn.keyss.common.rpc;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rpc注入处理器职责链
 */
public class RpcInterceptionHandlerPipeline {

    /**
     * 切面处理器集合
     */
    private List<RpcInterceptionHandler> handlers;

    /**
     * 缺省处理器
     */
    private RpcInterceptionHandler defaultHandler;

    /**
     * 注册切面处理器
     *
     * @param handler
     */
    public void addHandler(RpcInterceptionHandler handler) {
        handlers.add(handler);
    }

    /**
     * 构造方法
     *
     * @param defaultInnerHandler
     * @param handlers
     */
    public RpcInterceptionHandlerPipeline(RpcInterceptionHandler defaultInnerHandler, Collection<RpcInterceptionHandler> handlers) {
        if (handlers == null)
            this.handlers = new ArrayList<RpcInterceptionHandler>();
        else
            this.handlers = new ArrayList<RpcInterceptionHandler>(handlers);
        this.defaultHandler = defaultInnerHandler;
    }

    /**
     * 引发方法
     *
     * @param proxy  代理对象
     * @param method 方法
     * @param args   参数
     * @return 结果
     * @throws Throwable
     */
    public Object invoke(RpcProxy proxy, Method method, Object[] args) throws Throwable {
        Assert.notNull(proxy, "参数proxy不能为空!");
        Assert.notNull(method, "参数method不能为空!");

        if (args == null || args.length != 1)
            throw new IllegalArgumentException("args参数无效！");

        HttpEntity<?> requestEntity = new HttpEntity(args[0]);

        ResponseEntity<?> responseEntity;
        if (handlers.size() == 0) {
            responseEntity = this.defaultHandler.invoke(proxy, method, requestEntity, method.getReturnType(), null);
        } else {
            final AtomicInteger handlerIndex = new AtomicInteger(0);
            responseEntity = handlers.get(0).invoke(proxy, method, requestEntity, method.getReturnType(), new GetNextRpcInterceptionHandlerDelegate() {
                @Override
                public RpcInterceptionHandler getNextRpcInterceptionHandler() {
                    int index = handlerIndex.incrementAndGet();
                    if (index < handlers.size()) {
                        return handlers.get(index);
                    } else {
                        return defaultHandler;
                    }
                }
            });
        }
        return responseEntity.getBody();
    }
}