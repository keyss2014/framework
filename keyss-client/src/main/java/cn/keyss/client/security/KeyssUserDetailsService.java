package cn.keyss.client.security;

import cn.keyss.client.esb.ServiceBuilder;
import cn.keyss.client.security.contract.AuthenticationService;
import cn.keyss.client.security.contract.datacontract.GetUserInfoRequest;
import cn.keyss.client.security.contract.datacontract.GetUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyssUserDetailsService implements UserDetailsService {

    private final static Logger logger = LoggerFactory.getLogger(KeyssUserDetailsService.class);

    private AuthenticationService authenticationService;
    private Map<String, RoleProvider> roleProviders;

    public KeyssUserDetailsService(AuthenticationService authenticationService, Map<String, RoleProvider> roleProviders) {
        this.authenticationService = authenticationService;
        if (roleProviders == null || roleProviders.size() == 0) {
            this.roleProviders = new HashMap<>();
            this.roleProviders.put("NULL", new RoleProvider() {
                @Override
                public String[] queryUserRoles(String userName) {
                    return new String[0];
                }
            });
        } else {
            this.roleProviders = roleProviders;
        }
    }

    /**
     * 加载用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        GetUserInfoRequest request = new GetUserInfoRequest();
        request.setCallSource("KeyssUserDetailsService");
        request.setUserName(username);

        GetUserInfoResponse response = authenticationService.getUserInfo(request);

        if (response.getResultCode() != 0)
            throw new UsernameNotFoundException(username);

        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for (RoleProvider provider : this.roleProviders.values()) {
            String[] roles = provider.queryUserRoles(username);
            if (roles != null) {
                for (String role : roles) {
                    auths.add(new SimpleGrantedAuthority(role));
                }
            }
        }

        User user = new User(username.toLowerCase(), response.getUserInfo().getPassword(), true, true, true, !response.getUserInfo().isLocked(), auths);
        return user;
    }
}
