package hms.ussd.manager.conf;

/**
 * Created by rajive on 4/24/15.
 */
public class AppConfig {

    private final String url;
    private final String password;
    private final String applicationId;
    private final boolean isBackButtonEnable;
    private final String backButtonCode;

    private AppConfig(Builder builder) {
        this.url = builder.url;
        this.password = builder.password;
        this.applicationId = builder.applicationId;
        this.isBackButtonEnable = builder.isBackButtonEnable;
        this.backButtonCode = builder.backButtonCode;
    }

    public String getUrl() {
        return url;
    }


    public String getPassword() {
        return password;
    }

    public String getApplicationId() {
        return applicationId;
    }


    public boolean isBackButtonEnable() {
        return isBackButtonEnable;
    }

    public String getBackButtonCode() {
        return backButtonCode;
    }


    public static final class Builder {

        private String url = "http://127.0.0.1:7000/ussd/send";
        private String password = "password";
        private String applicationId = "App_0001";
        private boolean isBackButtonEnable = true;
        private String backButtonCode = "999";


        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder appId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder backEnabled(boolean isBackButtonEnable) {
            this.isBackButtonEnable = isBackButtonEnable;
            return this;
        }

        public Builder backButtonCode(String backButtonCode) {
            this.backButtonCode = backButtonCode;
            return this;
        }

        public AppConfig build() {
            return new AppConfig(this);
        }
    }

}