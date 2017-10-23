package cn.keyss.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

/**
 * 应用配置Spring容器侦听器
 * <p>
 * 当容器启动时，当环境准备好时，加载远程的配置信息
 */
public class ConfigApplicationListener implements GenericApplicationListener {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ConfigApplicationListener.class);

    /**
     * 事件类型
     */
    private static Class<?>[] EVENT_TYPES = {ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class};

    /**
     * 源类型
     */
    private static Class<?>[] SOURCE_TYPES = {SpringApplication.class, ApplicationContext.class};

    /**
     * 远程配置源名称
     */
    private static final String CONFIG_SOURCE_NAME = "keyss.config";
    /**
     * 配置服务器地址属性名称
     */
    private static final String CONFIG_ENABLE = "keyss.config.enable";
    /**
     * 配置服务器地址属性名称
     */
    private static final String CONFIG_SERVER = "keyss.config.server";

    /**
     * 应用属性名称
     */
    private static final String APPLICATION = "keyss.config.application";

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent) {
            ApplicationEnvironmentPreparedEvent event = (ApplicationEnvironmentPreparedEvent) applicationEvent;

            ConfigurableEnvironment envi = event.getEnvironment();
            String enable = envi.getProperty(CONFIG_ENABLE);
            if(enable == null || enable.toUpperCase()!="TRUE" ){
                logger.warn("远程应用配置未激活");
                return;
            }

            String configServerUrl = envi.getProperty(CONFIG_SERVER);
            if (configServerUrl == null) {
                throw new ConfigException("远程配置服务器地址未配置！");
            }
            String application = envi.getProperty(APPLICATION);
            if (application == null || application.trim().length() == 0) {
                throw new ConfigException("远程配置应用未配置！");
            }
            ConfigProperties configProperties = new ConfigProperties(configServerUrl, Integer.parseInt(application));
            //设置全局属性实例，供手工调用
            ConfigProperties.setInstance(configProperties);
            MutablePropertySources mps = envi.getPropertySources();
            MapPropertySource mapPropertySource = new MapPropertySource(CONFIG_SOURCE_NAME, configProperties.getProperties());

            //解析
            mps.addAfter(ConfigFileApplicationListener.APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME, mapPropertySource);
        } else if (applicationEvent instanceof ApplicationPreparedEvent) {
            ConfigProperties configProperties = ConfigProperties.getInstance();
            if (configProperties != null) {
                for (Map.Entry<String, Object> entry : configProperties.getProperties().entrySet()) {
                    logger.info("远程应用配置:{}={}", entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    public int getOrder() {
        // 设置加载优先级，在日志之前加载
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }

    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }
}