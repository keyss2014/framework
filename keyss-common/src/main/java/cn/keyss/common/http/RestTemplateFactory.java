package cn.keyss.common.http;

import java.util.List;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * 缺省RestTemplate构造工厂
 */
public class RestTemplateFactory {

    /**
     * 创建缺省同步RestTemplate
     * 连接超时1秒，访问超时6秒
     *
     * @return
     */
    public static RestTemplate createDefaultSync() {
        return createSync(1 * 1000, 6 * 1000);
    }

    /**
     * 创建缺省异步RestTemplate
     * 连接超时1秒，访问超时6秒
     *
     * @return
     */
    public static AsyncRestTemplate createDefaultAsync() {
        return createAsync(1 * 1000, 6 * 1000);
    }

    /**
     * 创建同步RestTemplate
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static RestTemplate createSync(int connectTimeout, int readTimeout) {
        RestTemplate template = new RestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) template.getRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        //设置转化器
        mappingJackson2HttpMessageConverter(template.getMessageConverters());
        return template;
    }

    /**
     * 创建异步RestTemplate
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static AsyncRestTemplate createAsync(int connectTimeout, int readTimeout) {
        AsyncRestTemplate template = new AsyncRestTemplate();
        SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) template.getAsyncRequestFactory();
        factory.setReadTimeout(readTimeout);
        factory.setConnectTimeout(connectTimeout);
        //设置转化器
        mappingJackson2HttpMessageConverter(template.getMessageConverters());
        return template;
    }

    /**
     * 引入Jackson关注Rest和Json转换
     * @param messageConverters
     */
    private static void mappingJackson2HttpMessageConverter(List<HttpMessageConverter<?>> messageConverters) {
        for (HttpMessageConverter converter : messageConverters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                //设置null值不参与序列化(字段不被显示)
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
                // 禁用空对象转换json校验
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                ((MappingJackson2HttpMessageConverter) converter).getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            }
        }
    }
}