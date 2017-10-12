package cn.keyss.client.config;

import cn.keyss.client.config.contract.ConfigService;
import cn.keyss.client.config.contract.datacontract.Config;
import cn.keyss.client.config.contract.datacontract.QueryApplicationConfigsRequest;
import cn.keyss.client.config.contract.datacontract.QueryApplicationConfigsResponse;
import cn.keyss.common.rpc.RpcProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用配置
 */
public class ConfigProperties {

    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ConfigProperties.class);

    /**
     * 应用编号
     */
    private int application;

    /**
     * 配置
     */
    private Map<String, Object> properties;

    /**
     * 配置服务
     */
    private RpcProxy<ConfigService> configService;

    /**
     * 构造方法
     *
     * @param configServerUrl 远程配置服务地址
     * @param application     应用ID
     */
    public ConfigProperties(String configServerUrl, int application) {
        this.application = application;
        this.configService = new RpcProxy<ConfigService>(ConfigService.class,configServerUrl, new RestTemplate());
        this.properties = this.loadProperties();
    }

    /**
     * 加载远程配置
     *
     * @return
     */
    private Map<String, Object> loadProperties() {
        try {
            QueryApplicationConfigsRequest request = new QueryApplicationConfigsRequest();
            request.setApplication(this.application);
            QueryApplicationConfigsResponse  response = configService.getTransparentProxy().queryApplicationConfigs(request );

            if (response.getResultCode() != 0) {
                throw new ConfigException(String.format("获取应用配置信息失败，失败代码:%s,失败原因：%s", response.getResultCode(), response.getResultMessage()));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            if (response.getConfigs() != null && response.getConfigs().size() > 0) {
                for (Config config : response.getConfigs()) {
                    hashMap.put(config.getName(),config.getValue());
                }
            }
            return hashMap;
        } catch (ConfigException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigException("查询应用配置信息发生异常！", e);
        }
    }

    /**
     * 获取所有配置
     *
     * @return
     */
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    /**
     * 获取指定名称属性
     *
     * @param name
     * @return
     */
    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    /**
     * 全局配置实例
     */
    private static ConfigProperties instance;

    /**
     * 设置全局实例
     *
     * @return
     */
    public static ConfigProperties getInstance() {
        return ConfigProperties.instance;
    }

    /**
     * 获取全局实例
     *
     * @param instance
     */
    public static void setInstance(ConfigProperties instance) {
        ConfigProperties.instance = instance;
    }
}
