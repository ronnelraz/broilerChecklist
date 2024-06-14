package com.ronnelrazo.broilerchecklist.model;

import com.google.gson.annotations.SerializedName;

public class Checklist_address {


    @SerializedName("org_code")
    private String org_code;

    @SerializedName("reg_office_1")
    private String reg_office_1;

    @SerializedName("reg_office_2")
    private String reg_office_2;

    @SerializedName("tel_no")
    private String tel_no;

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getReg_office_1() {
        return reg_office_1;
    }

    public void setReg_office_1(String reg_office_1) {
        this.reg_office_1 = reg_office_1;
    }

    public String getReg_office_2() {
        return reg_office_2;
    }

    public void setReg_office_2(String reg_office_2) {
        this.reg_office_2 = reg_office_2;
    }

    public String getTel_no() {
        return tel_no;
    }

    public void setTel_no(String tel_no) {
        this.tel_no = tel_no;
    }
}
