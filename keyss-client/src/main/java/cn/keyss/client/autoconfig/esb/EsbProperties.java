package cn.keyss.client.autoconfig.esb;

import org.springframework.boot.context.properties.ConfigurationProperties;

/***
 * 服务总线配置
 */
@ConfigurationProperties("keyss.esb")
public class EsbProperties {
    private int application;

    /***
     * 服务总线应用
     * @return
     */
    public int getApplication() {
        return application;
    }

    public void setApplication(int application) {
        this.application = application;
    }

    private String server;

    /***
     * 服务总线地址
     * @return
     */
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    private String tags;

    /***
     * 缺省标签
     * @return
     */
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
