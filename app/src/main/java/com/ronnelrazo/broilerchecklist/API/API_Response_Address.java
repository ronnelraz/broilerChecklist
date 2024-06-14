package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_address;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;

import java.util.List;

public class API_Response_Address {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_address> checklistAddresses;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Checklist_address> getChecklistAddresses() {
        return checklistAddresses;
    }

    public void setChecklistAddresses(List<Checklist_address> checklistAddresses) {
        this.checklistAddresses = checklistAddresses;
    }
}
