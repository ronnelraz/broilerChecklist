package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class transaction_data_offline {

    @SerializedName("DOCUMENT_NO")
    private String DOCUMENT_NO;

    @SerializedName("AUDIT_DATE")
    private String AUDIT_DATE;

    @SerializedName("FARM_ORG")
    private String FARM_ORG;

    @SerializedName("FARM_NAME")
    private  String FARM_NAME;

    @SerializedName("HOUSE_FLOCK")
    private String HOUSE_FLOCK;

    @SerializedName("CANCEL_FLAG")
    private String CANCEL_FLAG;

    @SerializedName("APPROVE_FLAG")
    private  String APPROVE_FLAG;

    @SerializedName("APPROVE_BY")
    private  String APPROVE_BY;


    @SerializedName("CHECKED")
    private  boolean CHECKED;


    public transaction_data_offline(String DOCUMENT_NO, String AUDIT_DATE, String FARM_ORG, String FARM_NAME, String HOUSE_FLOCK, String CANCEL_FLAG, String APPROVE_FLAG, String APPROVE_BY, boolean CHECKED) {
        this.DOCUMENT_NO = DOCUMENT_NO;
        this.AUDIT_DATE = AUDIT_DATE;
        this.FARM_ORG = FARM_ORG;
        this.FARM_NAME = FARM_NAME;
        this.HOUSE_FLOCK = HOUSE_FLOCK;
        this.CANCEL_FLAG = CANCEL_FLAG;
        this.APPROVE_FLAG = APPROVE_FLAG;
        this.APPROVE_BY = APPROVE_BY;
        this.CHECKED = CHECKED;
    }

    public String getDOCUMENT_NO() {
        return DOCUMENT_NO;
    }

    public void setDOCUMENT_NO(String DOCUMENT_NO) {
        this.DOCUMENT_NO = DOCUMENT_NO;
    }

    public String getAUDIT_DATE() {
        return AUDIT_DATE;
    }

    public void setAUDIT_DATE(String AUDIT_DATE) {
        this.AUDIT_DATE = AUDIT_DATE;
    }

    public String getFARM_ORG() {
        return FARM_ORG;
    }

    public void setFARM_ORG(String FARM_ORG) {
        this.FARM_ORG = FARM_ORG;
    }

    public String getFARM_NAME() {
        return FARM_NAME;
    }

    public void setFARM_NAME(String FARM_NAME) {
        this.FARM_NAME = FARM_NAME;
    }

    public String getHOUSE_FLOCK() {
        return HOUSE_FLOCK;
    }

    public void setHOUSE_FLOCK(String HOUSE_FLOCK) {
        this.HOUSE_FLOCK = HOUSE_FLOCK;
    }

    public String getCANCEL_FLAG() {
        return CANCEL_FLAG;
    }

    public void setCANCEL_FLAG(String CANCEL_FLAG) {
        this.CANCEL_FLAG = CANCEL_FLAG;
    }

    public String getAPPROVE_FLAG() {
        return APPROVE_FLAG;
    }

    public void setAPPROVE_FLAG(String APPROVE_FLAG) {
        this.APPROVE_FLAG = APPROVE_FLAG;
    }

    public String getAPPROVE_BY() {
        return APPROVE_BY;
    }

    public void setAPPROVE_BY(String APPROVE_BY) {
        this.APPROVE_BY = APPROVE_BY;
    }

    public boolean isCHECKED() {
        return CHECKED;
    }

    public void setCHECKED(boolean CHECKED) {
        this.CHECKED = CHECKED;
    }
}
