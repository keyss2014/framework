package cn.keyss.client.esb.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import cn.keyss.client.esb.EsbException;
import cn.keyss.client.esb.contract.EventService;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedRequest;
import cn.keyss.client.esb.contract.datacontract.OnEventReceivedResponse;
import cn.keyss.common.rpc.GetNextRpcInterceptionHandlerDelegate;
import cn.keyss.common.rpc.RpcInterceptionHandler;
import cn.keyss.common.rpc.RpcProxy;
import cn.keyss.common.utilities.SerializeHelper;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class EventProxy<T> extends RpcProxy<T> {

    private static Method eventMethod;

    static {
        try {
            eventMethod = EventService.class.getMethod("onEventReceived");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private List<String> tags;


    public EventProxy(Class<T> contractClazz, String serverUrl,List<String> tags ) {
        super(contractClazz, serverUrl );
        this.tags = tags;
        this.defaultHandler = new RpcInterceptionHandler() {
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

    private RpcInterceptionHandler defaultHandler;

    @Override
    protected RpcInterceptionHandler getDefaultHandler() {
        return defaultHandler;
    }

}
