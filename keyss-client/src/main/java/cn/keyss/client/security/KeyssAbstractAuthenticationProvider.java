package cn.keyss.client.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

/**
 * 基于KeyssUserDetailsService的自定义验证器
 */
public abstract class KeyssAbstractAuthenticationProvider implements
        AuthenticationProvider, InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());

    //region init
    private UserCache userCache = new NullUserCache();
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new  DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private UserDetailsService userDetailsService;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.userDetailsService, "A user details service must be set");
    }

    //endregion

    //region set properties
    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException("User account is locked");
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(
                        "User is disabled");
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(
                        "User account has expired");
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                logger.debug("User account credentials have expired");

                throw new CredentialsExpiredException(
                        "User credentials have expired");
            }
        }
    }
    //endregion

    //region for override

    /**
     * 是否支持该令牌凭证
     *
     * @param authentication
     * @return
     */
    public abstract boolean supports(Class<?> authentication);

    /**
     * 从凭证中获取用户名
     *
     * @param accessTokenAuthentication
     * @return 用户名
     * @throws AuthenticationException
     */
    protected abstract String retriveUsername(Authentication accessTokenAuthentication) throws AuthenticationException;

    /**
     * 创建已认证的令牌
     *
     * @param user          用户详情
     * @param authentication 原始令牌
     * @return
     */
    protected abstract Authentication createSuccessAuthentication(UserDetails user, Authentication authentication);
    //endregion

    /**
     * 主体认证过程
     *
     * @param authentication 令牌
     * @return 经过认证过程后的令牌
     * @throws AuthenticationException
     */
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        //获取用户名
        String username = retriveUsername(authentication);

        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null) {
            cacheWasUsed = false;

            try {
                user = retrieveUser(username);
            } catch (UsernameNotFoundException notFound) {
                logger.debug("User '" + username + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(
                            "Bad credentials");
                } else {
                    throw notFound;
                }
            }

            Assert.notNull(user,
                    "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            preAuthenticationChecks.check(user);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                cacheWasUsed = false;
                user = retrieveUser(username);
                preAuthenticationChecks.check(user);
            } else {
                throw exception;
            }
        }

        postAuthenticationChecks.check(user);

        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }
        return createSuccessAuthentication(user, authentication);
    }

    protected final UserDetails retrieveUser(String username)
            throws AuthenticationException {
        UserDetails loadedUser;

        try {
            loadedUser = this.userDetailsService.loadUserByUsername(username);
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }
}
