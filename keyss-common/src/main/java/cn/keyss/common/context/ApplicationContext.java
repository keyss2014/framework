package cn.keyss.common.context;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用上下文
 */
public class ApplicationContext {


    public Enumeration<String> getKeys(){
        return contexts.keys();
    }

    //region 初始化及单例

    /**
     * 上下文
     */
    private ConcurrentHashMap<String, String> contexts;

    /**
     * 私有化构造
     */
    private ApplicationContext() {
        contexts = new ConcurrentHashMap<String, String>();
    }

    /**
     * 缺省初始化
     */
    private static ThreadLocal<ApplicationContext> applicationContextThreadLocal = new ThreadLocal<ApplicationContext>() {
        public ApplicationContext initialValue() {
            return new ApplicationContext();
        }
    };

    /**
     * 获取当前上下文
     *
     * @return
     */
    public static ApplicationContext getCurrent() {
        return applicationContextThreadLocal.get();
    }
    //endregion

    //region 字符串常量
    public static final String UserKey = "x-keyss-user";
    public static final String ApplicationKey = "x-keyss-app";
    public static final String ApplicationVersionKey = "x-keyss-appver";
    public static final String DeviceKey = "x-keyss-device";
    public static final String IpAddressKey = "x-keyss-ipaddress";
    public static final String UserAgentKey = "x-keyss-useragent";
    //endregion

    //region 属性

    /**
     * 当前用户
     * @return
     */
    public String getUser() {
        return this.get(UserKey);
    }

    public void setUser(String name) {
        this.set(UserKey, name);
    }

    /**
     * 当前应用
     * @return
     */
    public String getApplication() {
        return this.get(ApplicationKey);
    }

    public void setApplication(String application) {
        this.set(ApplicationKey, application);
    }

    /**
     * 当前应用版本
     * @return
     */
    public String getApplicationVersion() {
        return this.get(ApplicationVersionKey);
    }

    public void setApplicationVersion(String applicationVersion) {
        this.set(ApplicationVersionKey, applicationVersion);
    }

    /**
     * 当前设备
     * @return
     */
    public String getDevice() {
        return this.get(DeviceKey);
    }

    public void setDevice(String device) {
        this.set(DeviceKey, device);
    }

    /**
     * 当前IP地址
     * @return
     */
    public String getIpAddress() {
        return this.get(IpAddressKey);
    }

    public void setIpAddress(String ipAddress) {
        this.set(IpAddressKey, ipAddress);
    }

    /**
     * 用户浏览器
     * @return
     */
    public String getUserAgent() {
        return this.get(UserAgentKey);
    }

    public void setUserAgent(String userAgent) {
        this.set(UserAgentKey, userAgent);
    }

    //endregion

    //region 上下文内容获取或设置

    /**
     * 获取应用上下文
     *
     * @param name 上下文名称
     * @return 上下文值
     */
    public String get(String name) {
        return this.contexts.get(name);
    }

    /**
     * 设置应用上下文
     *
     * @param name  上下文名称
     * @param value 上下文值
     */
    public void set(String name, String value) {
        this.contexts.put(name, value);
    }
    //endregion
}
