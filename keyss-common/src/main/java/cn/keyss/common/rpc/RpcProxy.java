package cn.keyss.common.rpc;

import cn.keyss.common.http.RestTemplateFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于Http协议的远程调用代理
 */
public class RpcProxy<T> implements InvocationHandler {

    //region private fileds
    /**
     * 服务器端地址
     */
    private  String serverUrl;
    protected String getServerUrl() {
        return this.serverUrl;
    }

    /**
     * Rest调用模板
     */
    private  RestTemplate restTemplate;
    protected RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    /**
     * 代理契约
     */
    private  Class<T> contractClazz;
    protected Class<T> getContractClazz(){
        return this.contractClazz;
    }

    /**
     * 获取缺省处理器
     */
    private  RpcInterceptionHandler defaultHandler;
    protected RpcInterceptionHandler getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * 获取注入链
     */
    private List<RpcInterceptionHandler> handlers = new ArrayList<>();
    protected  List<RpcInterceptionHandler> getInterceptionHandlers(){
        return handlers;
    }

    public RpcProxy(Class<T> contractClazz, String serverUrl){
        this(contractClazz, serverUrl, RestTemplateFactory.createDefaultSync());
    }
    /**
     * 构造方法
     *
     * @param contractClazz 契约
     * @param serverUrl     服务地址
     * @param restTemplate  rest调用模板
     */
    public RpcProxy(Class<T> contractClazz, String serverUrl, RestTemplate restTemplate) {
        this.contractClazz = contractClazz;
        this.serverUrl = serverUrl;
        this.restTemplate = restTemplate;
        this.defaultHandler = new RpcInterceptionHandler() {
            @Override
            public ResponseEntity<?> invoke(Object proxy, Method method, HttpEntity<?> requestEntity, Class<?> responseClass, GetNextRpcInterceptionHandlerDelegate getNextRpcInterceptionHandlerDelegate) {
                String url = RpcUrlHelper.parseInvokeUrl(getServerUrl(),method);
                return restTemplate.postForEntity(url, requestEntity, responseClass);
            }
        };
        this.handlers = new ArrayList<RpcInterceptionHandler>();
    }
    //endregion

    //region invoke
    /**
     * 引发方法
     *
     * @param proxy  代理
     * @param method 方法
     * @param args   参数
     * @return 返回值
     * @throws Throwable 异常
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        String methodName = method.getName();
//        Class<?>[] parameterTypes = method.getParameterTypes();
//        if (method.getDeclaringClass() == Object.class) {
//            return method.invoke(this, args);
//        }
//        if ("toString".equals(methodName) && parameterTypes.length == 0) {
//            return this.toString();
//        }
//        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
//            return this.hashCode();
//        }
//        if ("equals".equals(methodName) && parameterTypes.length == 1) {
//            return this.equals(args[0]);
//        }

        Assert.notNull(proxy, "参数proxy不能为空!");
        Assert.notNull(method, "参数method不能为空!");

        if (args == null || args.length != 1)
            throw new IllegalArgumentException("args参数无效，必须为长度为1的数组！");

        HttpEntity<?> requestEntity = new HttpEntity(args[0]);

        ResponseEntity<?> responseEntity;
        if (this.getInterceptionHandlers().size() == 0) {
            responseEntity = this.getDefaultHandler().invoke(proxy, method, requestEntity, method.getReturnType(), null);
        } else {
            final AtomicInteger handlerIndex = new AtomicInteger(0);
            responseEntity = this.getInterceptionHandlers().get(0).invoke(proxy, method, requestEntity, method.getReturnType(), new GetNextRpcInterceptionHandlerDelegate() {
                @Override
                public RpcInterceptionHandler getNextRpcInterceptionHandler() {
                    int index = handlerIndex.incrementAndGet();
                    if (index < getInterceptionHandlers().size()) {
                        return getInterceptionHandlers().get(index);
                    } else {
                        return getDefaultHandler();
                    }
                }
            });
        }
        return responseEntity.getBody();
    }

    /**
     * 获取透明代理
     *
     * @return
     */
    public T getTransparentProxy() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{this.contractClazz}, this);
    }
    //endregion
}
