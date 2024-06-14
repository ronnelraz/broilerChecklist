package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Checklist_FHF {


    @SerializedName("FARM_HOUSE_FLOCK")
    private String FARM_HOUSE_FLOCK;

    @SerializedName("FARM_NAME")
    private String FARM_NAME;

    public String getFARM_HOUSE_FLOCK() {
        return FARM_HOUSE_FLOCK;
    }

    public void setFARM_HOUSE_FLOCK(String FARM_HOUSE_FLOCK) {
        this.FARM_HOUSE_FLOCK = FARM_HOUSE_FLOCK;
    }

    public String getFARM_NAME() {
        return FARM_NAME;
    }

    public void setFARM_NAME(String FARM_NAME) {
        this.FARM_NAME = FARM_NAME;
    }
}
