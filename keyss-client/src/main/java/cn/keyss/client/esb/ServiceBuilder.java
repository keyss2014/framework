package cn.keyss.client.esb;

import java.util.Map;

/***
 * 服务构建器
 */
public interface ServiceBuilder {
     /**
      * 构造服务
      * @param contractClazz 服务契约
      * @param tags 标签
      * @param <T> 服务类型
      * @return 服务代理
      */
     <T> T buildService(Class<T> contractClazz, Map<String, String> tags) ;

     /***
      * 构造事件
      * @param contractClazz 事件契约
      * @param tags 标签
      * @param <T> 事件类型
      * @return 事件代理
      */
     <T> T buildEvent(Class<T> contractClazz, Map<String, String> tags);
}
