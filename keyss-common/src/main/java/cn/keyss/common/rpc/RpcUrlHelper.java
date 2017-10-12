package cn.keyss.common.rpc;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程调用Url帮助器
 * 根据服务器地址及RequestMapping组装远程服务调用地址
 */
abstract class RpcUrlHelper {

    /**
     * 缓存锁
     */
    private static Object syncLock = new Object();
    /**
     * 方法请求缓存
     */
    private static ConcurrentHashMap<Method, String> requestMaps = new ConcurrentHashMap<>();

    /**
     * 获取请求路径
     *
     * @param requestMapping
     * @return
     */
    private static String getRequestPath(RequestMapping requestMapping) {
        if (requestMapping != null) {
            String[] values = requestMapping.value();
            if (values.length > 0) {
                return values[0];
            }
            String[] paths = requestMapping.path();
            if (paths.length > 0) {
                return paths[0];
            }
        }
        return "";
    }

    /**
     * 获取方法请求路径
     *
     * @param method
     * @return
     */
    private static String getMethodRequestPath(Method method) {
        if (requestMaps.containsKey(method))
            return requestMaps.get(method);

        RequestMapping classRequestMapping = method.getDeclaringClass().getAnnotation(RequestMapping.class);
        String servicePath = getRequestPath(classRequestMapping);

        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        String methodPath = getRequestPath(methodRequestMapping);

        String fullPath = servicePath + methodPath;

        if (!requestMaps.containsKey(method)) {
            synchronized (syncLock) {
                if (!requestMaps.containsKey(method))
                    requestMaps.put(method, fullPath);
            }
        }

        return fullPath;
    }

    /**
     * 解析服务方法请求地址
     *
     * @param serverUrl 服务器地址
     * @param method    服务方法
     * @return 服务方法请求地址
     */
    public static String parseInvokeUrl(String serverUrl, Method method) {
        if (serverUrl == null)
            serverUrl = "";

        if (method == null)
            return serverUrl;

        String methodRequestUrl = getMethodRequestPath(method);
        return serverUrl + methodRequestUrl;
    }
}
