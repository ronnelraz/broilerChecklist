package com.ronnelrazo.broilerchecklist.model;

public class model_confirm_list {

    public int id;
    public String audit_date;
    public String farm_name;
    public String house_flock;

    public String broiler_count;
    public String broiler_wgh;
    public String broiler_reject_count;
    public String broiler_reject_wgh;

    public boolean auto_count;

    public boolean Checked;


    public model_confirm_list(int id, String audit_date, String farm_name, String house_flock, String broiler_count, String broiler_wgh, String broiler_reject_count, String broiler_reject_wgh, boolean auto_count, boolean checked) {
        this.id = id;
        this.audit_date = audit_date;
        this.farm_name = farm_name;
        this.house_flock = house_flock;
        this.broiler_count = broiler_count;
        this.broiler_wgh = broiler_wgh;
        this.broiler_reject_count = broiler_reject_count;
        this.broiler_reject_wgh = broiler_reject_wgh;
        this.auto_count = auto_count;
        Checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudit_date() {
        return audit_date;
    }

    public void setAudit_date(String audit_date) {
        this.audit_date = audit_date;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }

    public String getHouse_flock() {
        return house_flock;
    }

    public void setHouse_flock(String house_flock) {
        this.house_flock = house_flock;
    }

    public String getBroiler_count() {
        return broiler_count;
    }

    public void setBroiler_count(String broiler_count) {
        this.broiler_count = broiler_count;
    }

    public String getBroiler_wgh() {
        return broiler_wgh;
    }

    public void setBroiler_wgh(String broiler_wgh) {
        this.broiler_wgh = broiler_wgh;
    }

    public String getBroiler_reject_count() {
        return broiler_reject_count;
    }

    public void setBroiler_reject_count(String broiler_reject_count) {
        this.broiler_reject_count = broiler_reject_count;
    }

    public String getBroiler_reject_wgh() {
        return broiler_reject_wgh;
    }

    public void setBroiler_reject_wgh(String broiler_reject_wgh) {
        this.broiler_reject_wgh = broiler_reject_wgh;
    }

    public boolean isAuto_count() {
        return auto_count;
    }

    public void setAuto_count(boolean auto_count) {
        this.auto_count = auto_count;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        Checked = checked;
    }
}
