package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Checklist_farm_data {

    @SerializedName("farm_code")
    private String farm_code;

    @SerializedName("farm_name")
    private String farm_name;

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }
}
