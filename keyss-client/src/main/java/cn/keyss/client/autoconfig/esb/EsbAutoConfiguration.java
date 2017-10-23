package cn.keyss.client.autoconfig.esb;

import cn.keyss.client.esb.ServiceBuilder;
import cn.keyss.client.esb.ServiceBuilderImpl;
import cn.keyss.common.rpc.handers.ApplicationContextHandler;
import cn.keyss.common.rpc.handers.JsonHeaderHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({EsbProperties.class})
public class EsbAutoConfiguration {

    private EsbProperties esbProperties;

    public EsbAutoConfiguration(EsbProperties esbProperties) {
        this.esbProperties = this.esbProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name="keyss.esb.enable", havingValue="true")
    public ServiceBuilder createServiceBuilder() {
        return new ServiceBuilderImpl(this.esbProperties.getApplication(), this.esbProperties.getServer(), this.esbProperties.getTags());
    }
}
