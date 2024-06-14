package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_FHF;
import com.ronnelrazo.broilerchecklist.model.Checklist_address;

import java.util.List;

public class API_Response_FHF {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_FHF> checklistFhfs;


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

    public List<Checklist_FHF> getChecklistFhfs() {
        return checklistFhfs;
    }

    public void setChecklistFhfs(List<Checklist_FHF> checklistFhfs) {
        this.checklistFhfs = checklistFhfs;
    }
}
