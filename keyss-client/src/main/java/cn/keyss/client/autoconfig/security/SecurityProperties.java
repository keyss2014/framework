package cn.keyss.client.autoconfig.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("keyss.security")
public class SecurityProperties {

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private Sso sso;

    private AccessToken accessToken;

    public SecurityProperties(){
        this.enable = true;
        this.sso = new Sso();
        this.accessToken = new AccessToken();
    }

    public Sso getSso() {
        return sso;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public static class Sso{
        private boolean enable;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String server;

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }
    }

    public static class AccessToken{
        private boolean enable;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        private long validatePeriod;

        public long getValidatePeriod() {
            return validatePeriod;
        }

        public void setValidatePeriod(long validatePeriod) {
            this.validatePeriod = validatePeriod;
        }
    }
}
