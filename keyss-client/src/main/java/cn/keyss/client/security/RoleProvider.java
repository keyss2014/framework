package cn.keyss.client.security;


/**
 * 用户角色提供者
 */
public interface RoleProvider {

    /**
     * 获取用户角色
     * @param userName 用户名
     * @return 角色
     */
    String[] queryUserRoles(String userName);
}
