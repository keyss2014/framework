package cn.keyss.client.esb;

import java.util.Map;

public interface EsbServiceBuilder {
     <T> T buildService(Class<T> contractClazz, Map<String, String> tags) ;
     <T> T buildEvent(Class<T> contractClazz, Map<String, String> tags);
}
