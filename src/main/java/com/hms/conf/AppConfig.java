package com.hms.conf;

/**
 * Created by rajive on 4/24/15.
 */
public class AppConfig {

    private String url = "http://127.0.0.1:7000/ussd/send";
    private String password = "password";
    private String applicationId = "App_0001";
    private boolean isBackButtonEnable = true;
    private String backButtonCode = "999";


    public String getUrl() {
        return url;
    }

    public AppConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AppConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public AppConfig setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public boolean isBackButtonEnable() {
        return isBackButtonEnable;
    }

    public AppConfig setBackButtonEnable(boolean isBackButtonEnable) {
        this.isBackButtonEnable = isBackButtonEnable;
        return this;
    }

    public String getBackButtonCode() {
        return backButtonCode;
    }

    public AppConfig setBackButtonCode(String backButtonCode) {
        this.backButtonCode = backButtonCode;
        return this;
    }
}
