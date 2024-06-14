package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Login_data {

    @SerializedName("org_wip")
    private String orgWip;
    @SerializedName("status")
    private String status;
    @SerializedName("user")
    private String user;


    public String getOrgWip() {
        return orgWip;
    }

    public void setOrgWip(String orgWip) {
        this.orgWip = orgWip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
