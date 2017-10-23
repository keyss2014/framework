package cn.keyss.client.esb;

import cn.keyss.client.esb.contract.EventService;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedRequest;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedResponse;
import cn.keyss.common.context.ApplicationContext;
import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import cn.keyss.common.rpc.RpcProxy;
import cn.keyss.common.utilities.SerializeHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.List;

/***
 * 事件引发器
 * @param <T>
 */
public class EventProxy<T> extends RpcProxy<T> {
    // region event method
    private static Method eventMethod;
    static {
        try {
            eventMethod = EventService.class.getMethod("onEventReceived");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    //endregion

    private List<String> tags;
    private RpcInterceptionHandler defaultHandler;
    /***
     * 构造器
     * @param contractClazz 契约
     * @param serverUrl 服务地址
     * @param tags 标签
     */
    public EventProxy(Class<T> contractClazz, String serverUrl, List<String> tags, List<RpcInterceptionHandler> handlers) {
        super(contractClazz, serverUrl );
        this.tags = tags;
        this.getInterceptionHandlers().addAll(handlers);
    }

    @Override
    protected RpcInterceptionHandler getDefaultHandler() {
        if(defaultHandler == null){
            defaultHandler = new RpcInterceptionHandler() {
                @Override
                public ResponseEntity<?> invoke(Object proxy, Method method, HttpEntity<?> requestEntity,
                                                Class<?> responseClass,
                                                GetNextRpcInterceptionHandlerDelegate getNextRpcInterceptionHandlerDelegate) {
                    RpcInterceptionHandler handler = EventProxy.super.getDefaultHandler();
                    OnEventReceivedRequest eventReceivedRequest = new OnEventReceivedRequest();
                    byte[] body = SerializeHelper.serialize(requestEntity.getBody());
                    eventReceivedRequest.setBody(body);
                    eventReceivedRequest.setTags(tags);
                    HttpEntity<OnEventReceivedRequest> request = new HttpEntity<>(eventReceivedRequest);
                    ResponseEntity<?> responseEntity = handler.invoke(proxy, eventMethod, request,
                            OnEventReceivedResponse.class, null);
                    return responseEntity;
                }
            };
        }
        return defaultHandler;
    }
}
