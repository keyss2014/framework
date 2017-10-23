package cn.keyss.client.esb;

import cn.keyss.client.esb.EsbException;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.client.esb.EsbInfoLoader;
import cn.keyss.common.context.ApplicationContext;
import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import cn.keyss.common.rpc.RpcProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/***
 * 服务代理
 * @param <T>
 */
public class ServiceProxy<T> extends RpcProxy<T> {
    private static String TagsHeader = "x-keyss-tags";
    private String tagsString;

    public ServiceProxy(Class<T> contractClazz, String url, List<String> tags, List<RpcInterceptionHandler> handlers) {
        super(contractClazz, url);
        this.tagsString = TagsHelper.convertToTagString(tags);
        this.getInterceptionHandlers().addAll(handlers);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ApplicationContext applicationContext = ApplicationContext.getCurrent();
        String oldTagString = applicationContext.get(TagsHeader);
        applicationContext.set(TagsHeader, tagsString);
        Object result = super.invoke(proxy, method, args);
        applicationContext.set(TagsHeader, oldTagString);
        return result;
    }
}