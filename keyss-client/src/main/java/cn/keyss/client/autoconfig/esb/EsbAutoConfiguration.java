package cn.keyss.client.autoconfig.esb;

import cn.keyss.client.autoconfig.ApplicationProperties;
import cn.keyss.client.esb.EsbServiceBuilder;
import cn.keyss.client.esb.ServiceBuilder;
import cn.keyss.common.rpc.handers.ApplicationContextHandler;
import cn.keyss.common.rpc.handers.JsonHeaderHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
public class EsbAutoConfiguration {

    private ApplicationProperties applicationProperties;
    public EsbAutoConfiguration(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public EsbServiceBuilder createServiceBuilder() {
        ServiceBuilder builder =  new ServiceBuilder( this.applicationProperties.getApplicationId(), this.applicationProperties.getEsbServer());
        builder.addHandler(new JsonHeaderHandler());
        builder.addHandler(new ApplicationContextHandler());
        return builder;
    }
}
