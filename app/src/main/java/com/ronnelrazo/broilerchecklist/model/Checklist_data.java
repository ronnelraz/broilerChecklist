package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;


import java.io.Serial;
import java.util.List;

public class Checklist_data {

    @SerializedName("org_wip")
    private String org_wip;

    @SerializedName("branch_name")
    private String branch_name;

    @SerializedName("farm_code")
    private String farm_code;

    @SerializedName("farm_house_flock")
    private  String farm_house_flock;

    @SerializedName("farm_name")
    private String farm_name;

    @SerializedName("date_in")
    private String date_in;

    @SerializedName("chick_age")
    private  String chick_age;

    @SerializedName("current_balance")
    private  String current_balance;


    public String getOrg_wip() {
        return org_wip;
    }

    public void setOrg_wip(String org_wip) {
        this.org_wip = org_wip;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getFarm_house_flock() {
        return farm_house_flock;
    }

    public void setFarm_house_flock(String farm_house_flock) {
        this.farm_house_flock = farm_house_flock;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    public String getChick_age() {
        return chick_age;
    }

    public void setChick_age(String chick_age) {
        this.chick_age = chick_age;
    }

    public String getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }
}
