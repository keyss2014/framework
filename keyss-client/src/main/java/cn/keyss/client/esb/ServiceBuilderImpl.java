package cn.keyss.client.esb;

import cn.keyss.client.esb.contract.datacontract.Event;
import cn.keyss.client.esb.contract.datacontract.Service;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import cn.keyss.common.rpc.handers.ApplicationContextHandler;
import cn.keyss.common.rpc.handers.JsonHeaderHandler;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务工厂对象
 */
public class ServiceBuilderImpl implements ServiceBuilder {
    /***
     * 服务调用注入链
     */
    private final List<RpcInterceptionHandler> handlers;

    /***
     * 应用服务信息加载器
     */
    private final EsbInfoLoader esbInfoLoader;

    /***
     * 缺省标签
     */
    private final Map<String, String> defaultTags;

    /***
     * 构造器
     * @param application 应用
     * @param esbServerUrl 企业服务总线服务地址
     * @param defaultTags 缺省标签
     */
    public ServiceBuilderImpl(int application, String esbServerUrl, String defaultTags) {

        Assert.notNull(application, "参数application不能为空!");
        Assert.notNull(esbServerUrl, "参数esbServerUrl不能为空!");

        this.esbInfoLoader = new EsbInfoLoader(application, esbServerUrl);
        this.defaultTags = TagsHelper.parseTagsString(defaultTags);
        this.handlers = new ArrayList<>();
        //support json
        this.handlers.add(new JsonHeaderHandler());
        //suport context
        this.handlers.add(new ApplicationContextHandler());
    }

    /***
     * 构造器
     * @param application 应用
     * @param esbServerUrl 企业服务总线服务地址
     * @param defaultTags 缺省标签
     * @param handlers 注入链
     */
    public ServiceBuilderImpl(int application, String esbServerUrl, String defaultTags, List<RpcInterceptionHandler> handlers) {
        this.esbInfoLoader = new EsbInfoLoader(application, esbServerUrl);
        this.defaultTags = TagsHelper.parseTagsString(defaultTags);
        if(handlers == null)
            handlers = new ArrayList<>();
        this.handlers = handlers;
    }

    /***
     * 注册注入链，在配置器中或构造时调用，非线程安全
     * @param handler 引发调用器
     */
    public void addHandler(RpcInterceptionHandler handler) {
        this.handlers.add(handler);
    }

    /***
     * 构造服务
     * @param contractClazz 服务契约
     * @param tags 标签
     * @param <T> 服务类型
     * @return 服务
     */
    public <T> T buildService(Class<T> contractClazz, Map<String, String> tags) {
        List<String> packageTags = TagsHelper.mergeTags(tags, this.defaultTags);
        Service serviceInfo = this.esbInfoLoader.findService(contractClazz, packageTags);
        if (serviceInfo == null) {
            throw new EsbException("服务" + contractClazz.getName() + "未在注册中心注册或应用无权访问！");
        }
        ServiceProxy<T> serviceProxy = new ServiceProxy<T>(contractClazz, serviceInfo.getUrl(), packageTags, this.handlers);
        return serviceProxy.getTransparentProxy();
    }

    /***
     * 构造事件
     * @param contractClazz 事件契约
     * @param tags 标签
     * @param <T> 事件类型
     * @return 事件
     */
    public <T> T buildEvent(Class<T> contractClazz, Map<String, String> tags) {
        List<String> packageTags = TagsHelper.mergeTags(tags, this.defaultTags);
        Event eventInfo = this.esbInfoLoader.findEvent(contractClazz);
        if (eventInfo == null) {
            throw new EsbException("事件" + contractClazz.getName() + "未在注册中心注册或应用无权访问！");
        }
        EventProxy<T> result = new EventProxy<T>(contractClazz, eventInfo.getUrl(), packageTags,this.handlers);
        return result.getTransparentProxy();
    }
}
