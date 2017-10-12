package cn.keyss.client.esb.proxy;

import cn.keyss.client.esb.EsbException;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedRequest;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedResponse;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.client.esb.loader.ApplicationRouteInfoLoader;
import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import cn.keyss.common.rpc.RpcProxy;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ServiceProxy<T> extends RpcProxy<T> {
    private ApplicationRouteInfoLoader routeInfoLoader;
    private List<String> packageTags;
    private Class<T> contractClazz;

    public ServiceProxy(Class<T> contractClazz, List<String> packageTags, ApplicationRouteInfoLoader routeInfoLoader ){
        super(contractClazz, null);
        this.routeInfoLoader = routeInfoLoader;
        this.packageTags = packageTags;
        this.contractClazz=contractClazz;
    }

    @Override
    protected String getServerUrl() {
        Service service = routeInfoLoader.findService(contractClazz, packageTags);
        if (service == null)
            throw new EsbException("服务" + contractClazz.getName() + "注册中心中不存在或无权访问！");
        if (service.getUrl() == null || "".equals(service.getUrl()))
            throw new EsbException("服务" + contractClazz.getName() + "在注册中心中调用地址为空！");
        return service.getUrl();
    }

    @Override
    public List<RpcInterceptionHandler> getInterceptionHandlers() {
        return new ArrayList<>();
    }
}