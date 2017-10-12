package cn.keyss.client.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keyss")
public class ApplicationProperties {
   private int applicationId;

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    private String configServer;

    public String getConfigServer() {
        return configServer;
    }

    public void setConfigServer(String configServer) {
        this.configServer = configServer;
    }

    private String esbServer;

    public String getEsbServer() {
        return esbServer;
    }

    public void setEsbServer(String esbServer) {
        this.esbServer = esbServer;
    }
}
