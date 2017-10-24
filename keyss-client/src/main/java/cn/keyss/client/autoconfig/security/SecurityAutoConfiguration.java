package cn.keyss.client.autoconfig.security;

import cn.keyss.client.security.KeyssUserDetailService;
import cn.keyss.client.security.RoleProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {
//    @Bean
//    @ConditionalOnMissingBean
//    public RoleProvider createRoleProvider() {
//        return new RoleProvider() {
//            @Override
//            public String[] queryUserRoles(String username) {
//                return new String[0];
//            }
//        };
//    }
//
//    @Bean
//    @ConditionalOnProperty(prefix = "keyss.security.type", havingValue = "server")
//    public UserDetailsService createUserDetailsService() {
//        return null;
//    }
}
