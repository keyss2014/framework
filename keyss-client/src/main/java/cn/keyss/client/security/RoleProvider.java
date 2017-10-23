package cn.keyss.client.security;


public interface RoleProvider {
    String[] queryUserRoles(String username);
}
