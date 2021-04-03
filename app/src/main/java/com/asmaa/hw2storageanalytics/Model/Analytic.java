package com.asmaa.hw2storageanalytics.Model;

public class Analytic {
   private String time;
   private String pageName;
   private String userId;

    public Analytic(String time, String pageName, String userId) {
        this.time = time;
        this.pageName = pageName;
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
