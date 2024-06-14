package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Checklist_Header {

    @SerializedName("FLOCK")
    private String FLOCK;

    @SerializedName("DATE_IN")
    private String DATE_IN;

    @SerializedName("CHICK_AGE")
    private String CHICK_AGE;

    @SerializedName("CURRENT_BALANCE")
    private String CURRENT_BALANCE;

    public String getFLOCK() {
        return FLOCK;
    }

    public void setFLOCK(String FLOCK) {
        this.FLOCK = FLOCK;
    }

    public String getDATE_IN() {
        return DATE_IN;
    }

    public void setDATE_IN(String DATE_IN) {
        this.DATE_IN = DATE_IN;
    }

    public String getCHICK_AGE() {
        return CHICK_AGE;
    }

    public void setCHICK_AGE(String CHICK_AGE) {
        this.CHICK_AGE = CHICK_AGE;
    }

    public String getCURRENT_BALANCE() {
        return CURRENT_BALANCE;
    }

    public void setCURRENT_BALANCE(String CURRENT_BALANCE) {
        this.CURRENT_BALANCE = CURRENT_BALANCE;
    }
}
