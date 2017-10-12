package cn.keyss.client.autoconfig.security;

import cn.keyss.client.autoconfig.ApplicationProperties;
import cn.keyss.client.autoconfig.esb.EsbAutoConfiguration;
import cn.keyss.client.esb.EsbServiceBuilder;
import cn.keyss.client.security.*;
import cn.keyss.client.security.contract.AuthenticationService;
import cn.keyss.client.security.sso.SsoAuthenticationProvider;
import cn.keyss.client.security.sso.SsoClientFilter;
import cn.keyss.client.security.sso.SsoServerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnWebApplication
@EnableWebSecurity
@ConditionalOnProperty(
        prefix = "keyss.security",
        name = {"enable"},
        havingValue = "true",
        matchIfMissing = true
)
@AutoConfigureAfter({EsbAutoConfiguration.class})
public class SecurityAutoConfiguration {
    //region role

    /**
     * 授权配置
     */
    @Configuration
    @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
    public static class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
        @Override
        protected AccessDecisionManager accessDecisionManager() {
            List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
            RoleVoter roleVoter = new RoleVoter();
            roleVoter.setRolePrefix("");
            decisionVoters.add(roleVoter);
            return new AffirmativeBased(decisionVoters);
        }
    }

    //endregion

    //region authentication
    @Configuration
    @EnableConfigurationProperties({ApplicationProperties.class, SecurityProperties.class})
    @AutoConfigureAfter({EsbAutoConfiguration.class})
    public static class ApplicationClientSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        //region init
        private ApplicationProperties applicationProperties;
        private SecurityProperties securityProperties;

        protected ApplicationClientSecurityConfigurerAdapter(ApplicationProperties applicationProperties, SecurityProperties securityProperties) {
            this.applicationProperties = applicationProperties;
            this.securityProperties = securityProperties;
        }

        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/css/**","/img/**","/font/**","/js/**","/global/**", "/wx/**", "/areas/**", "/api/**", "/api*");
        }
        //endregion

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired(required = false)
        private EsbServiceBuilder serviceBuilder;


        @Bean
        @ConditionalOnMissingBean
        public UserDetailsService getKeyssUserDetailsService() {
            return new KeyssUserDetailsService(serviceBuilder.buildService(AuthenticationService.class, null), applicationContext.getBeansOfType(RoleProvider.class));
        }


        @Bean
        public AuthenticationProvider getKeyssAuthenticationProvider() {
            KeyssAuthenticationProvider authenticationProvider = new KeyssAuthenticationProvider(serviceBuilder.buildService(AuthenticationService.class, null));
            authenticationProvider.setUserDetailsService(getKeyssUserDetailsService());
            return authenticationProvider;
        }

        @Bean
        public AuthenticationProvider getRememberMeProvider() {
            RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider("keyss");
            return rememberMeAuthenticationProvider;
        }


        @Bean
        public RememberMeServices getRemeberMeService() {
            TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("keyss", getKeyssUserDetailsService());
            rememberMeServices.setCookieName("keyss");
            rememberMeServices.setParameter("RemberMe");
            rememberMeServices.setTokenValiditySeconds(864000);
            return rememberMeServices;
        }

        @Bean
        public CredentialRetriver keyssCredentialRetriver() {
            return new KeyssCredentialRetriver();
        }

        //endregion

        //region sso bean
        private SsoClientFilter ssoClientFilter;

        public SsoClientFilter getSsoClientFilter() {
            if (ssoClientFilter == null) {
                ssoClientFilter = new SsoClientFilter(securityProperties.getSso().getServer());
            }
            return ssoClientFilter;
        }

        private SsoServerFilter ssoServerFilter;

        public SsoServerFilter getSsoServerFilter() {
            if (ssoServerFilter == null) {
                ssoServerFilter = new SsoServerFilter();
            }
            return ssoServerFilter;
        }

        private SsoAuthenticationProvider ssoAuthenticationProvider;

        public SsoAuthenticationProvider getSsoAuthenticationProvider() {
            if (ssoAuthenticationProvider == null) {
                ssoAuthenticationProvider = new SsoAuthenticationProvider();
            }
            return ssoAuthenticationProvider;
        }
        //endregion


        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/account/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage("/account/login").permitAll()
            ;

            http.headers().frameOptions().disable();

            KeyssAuthenticationFilter keyssAuthenticationFilter = new KeyssAuthenticationFilter(applicationContext.getBeansOfType(CredentialRetriver.class));
            keyssAuthenticationFilter.setAuthenticationDetailsSource(new WebAuthenticationDetailsSource());
            keyssAuthenticationFilter.setAuthenticationManager(authenticationManager());
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setDefaultTargetUrl("/account/success");
            successHandler.setAlwaysUseDefaultTargetUrl(true);
            keyssAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
            SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/account/failure");
            keyssAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

            keyssAuthenticationFilter.setRememberMeServices(this.getRemeberMeService());
            http.addFilterAt(keyssAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            http.rememberMe().key("keyss").rememberMeParameter("RemberMe").rememberMeCookieName("keyss").tokenValiditySeconds(864000).userDetailsService(getKeyssUserDetailsService());
            http.logout().deleteCookies("keyss");

        }

        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(getKeyssAuthenticationProvider()).authenticationProvider(getRememberMeProvider()).eraseCredentials(false);
        }
    }
    //endregion
}
