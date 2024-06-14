package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Checklist_Reason {

    @SerializedName("REASON_ID")
    private String REASON_ID;

    @SerializedName("REASON_NAME")
    private String REASON_NAME;

    public String getREASON_ID() {
        return REASON_ID;
    }

    public void setREASON_ID(String REASON_ID) {
        this.REASON_ID = REASON_ID;
    }

    public String getREASON_NAME() {
        return REASON_NAME;
    }

    public void setREASON_NAME(String REASON_NAME) {
        this.REASON_NAME = REASON_NAME;
    }
}
