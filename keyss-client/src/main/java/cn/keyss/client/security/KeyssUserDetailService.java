package cn.keyss.client.security;

import cn.keyss.client.security.contract.datacontract.GetUserInfoRequest;
import cn.keyss.client.security.contract.datacontract.GetUserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class KeyssUserDetailService  implements UserDetailsService {

    private Map<String, RoleProvider> roleProviders;

    public KeyssUserDetailService( Map<String, RoleProvider> roleProviders) {
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

        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for (RoleProvider provider : this.roleProviders.values()) {
            String[] roles = provider.queryUserRoles(username);
            if (roles != null) {
                for (String role : roles) {
                    auths.add(new SimpleGrantedAuthority(role));
                }
            }
        }

        User user = new User(username.toLowerCase(), "", true, true, true, false, auths);
        return user;
    }
}
