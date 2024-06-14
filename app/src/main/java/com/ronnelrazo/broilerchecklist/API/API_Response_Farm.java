package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_data;
import com.ronnelrazo.broilerchecklist.model.Checklist_farm_data;

import java.util.List;

public class API_Response_Farm {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_farm_data> checklistFarmData;

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

    public List<Checklist_farm_data> getChecklistFarmData() {
        return checklistFarmData;
    }

    public void setChecklistFarmData(List<Checklist_farm_data> checklistFarmData) {
        this.checklistFarmData = checklistFarmData;
    }
}
