package com.ronnelrazo.broilerchecklist.model;

public class model_signature_list {

    private int trHeaderId;
    private String auditDate;
    private String farmOrg;
    private String houseFlock;
    private String prepared_sign;

    private String prepared_user;

    private String saleman_sign;

    private String saleman_user;

    private String farm_sign;

    private String farm_user;


    public model_signature_list(int trHeaderId, String auditDate, String farmOrg, String houseFlock, String prepared_sign, String prepared_user, String saleman_sign, String saleman_user, String farm_sign, String farm_user) {
        this.trHeaderId = trHeaderId;
        this.auditDate = auditDate;
        this.farmOrg = farmOrg;
        this.houseFlock = houseFlock;
        this.prepared_sign = prepared_sign;
        this.prepared_user = prepared_user;
        this.saleman_sign = saleman_sign;
        this.saleman_user = saleman_user;
        this.farm_sign = farm_sign;
        this.farm_user = farm_user;
    }


    public int getTrHeaderId() {
        return trHeaderId;
    }

    public void setTrHeaderId(int trHeaderId) {
        this.trHeaderId = trHeaderId;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getFarmOrg() {
        return farmOrg;
    }

    public void setFarmOrg(String farmOrg) {
        this.farmOrg = farmOrg;
    }

    public String getHouseFlock() {
        return houseFlock;
    }

    public void setHouseFlock(String houseFlock) {
        this.houseFlock = houseFlock;
    }

    public String getPrepared_sign() {
        return prepared_sign;
    }

    public void setPrepared_sign(String prepared_sign) {
        this.prepared_sign = prepared_sign;
    }

    public String getPrepared_user() {
        return prepared_user;
    }

    public void setPrepared_user(String prepared_user) {
        this.prepared_user = prepared_user;
    }

    public String getSaleman_sign() {
        return saleman_sign;
    }

    public void setSaleman_sign(String saleman_sign) {
        this.saleman_sign = saleman_sign;
    }

    public String getSaleman_user() {
        return saleman_user;
    }

    public void setSaleman_user(String saleman_user) {
        this.saleman_user = saleman_user;
    }

    public String getFarm_sign() {
        return farm_sign;
    }

    public void setFarm_sign(String farm_sign) {
        this.farm_sign = farm_sign;
    }

    public String getFarm_user() {
        return farm_user;
    }

    public void setFarm_user(String farm_user) {
        this.farm_user = farm_user;
    }
}
