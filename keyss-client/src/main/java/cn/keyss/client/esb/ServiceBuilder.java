package cn.keyss.client.esb;

import cn.keyss.client.esb.contract.datacontract.Event;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.client.esb.loader.ApplicationRouteInfoLoader;
import cn.keyss.client.esb.proxy.EventProxy;
import cn.keyss.client.esb.proxy.ServiceProxy;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 服务工厂对象
 */
public class ServiceBuilder implements EsbServiceBuilder {
    private final List<RpcInterceptionHandler> handlers;

    private final ApplicationRouteInfoLoader routeInfoLoader;

    public ServiceBuilder(int application, String esbServerUrl) {
        this.handlers = new ArrayList<>();
        this.routeInfoLoader = new ApplicationRouteInfoLoader(application, esbServerUrl);
    }

    private static List<String> mapTags(Map<String, String> tags) {
        List<String> replaceTags = new ArrayList<>();
        if(tags!=null && tags.size() > 0) {
            for (String key : tags.keySet()) {
                replaceTags.add(key + ":" + tags.get(key));
            }
        }
        return replaceTags ;
    }

    public void addHandler(RpcInterceptionHandler handler){
        this.handlers.add(handler);
    }

    public <T> T buildService(Class<T> contractClazz, Map<String, String> tags) {
        List<String> packageTags = mapTags(tags);
        Service serviceInfo = this.routeInfoLoader.findService(contractClazz, packageTags);
        if (serviceInfo == null)
            throw new EsbException("服务" + contractClazz.getName() + "注册中心中不存在或无权访问！");
        if (serviceInfo.getUrl() == null || "".equals(serviceInfo.getUrl()))
            throw new EsbException("服务" + contractClazz.getName() + "在注册中心中调用地址为空！");
        ServiceProxy<T> serviceProxy = new ServiceProxy<T>(contractClazz, packageTags, this.routeInfoLoader);
        serviceProxy.getInterceptionHandlers().addAll(handlers);
        return serviceProxy.getTransparentProxy();
    }

    public <T> T buildEvent(Class<T> contractClazz, Map<String, String> tags) {
        Event eventInfo = this.routeInfoLoader.findEvent(contractClazz);
        if (eventInfo == null) {
            throw new EsbException("事件" + contractClazz.getName() + "注册中心中不存在或无权访问！");
        }
        if (eventInfo.getUrl() == null || "".equals(eventInfo.getUrl())) {
            throw new EsbException("事件" + contractClazz.getName() + "在注册中心中分发地址为空！");
        }
        List<String> packageTags = mapTags(tags);
        T result = new EventProxy<T>(contractClazz, eventInfo.getUrl(), packageTags).getTransparentProxy();
        return result;
    }
}
