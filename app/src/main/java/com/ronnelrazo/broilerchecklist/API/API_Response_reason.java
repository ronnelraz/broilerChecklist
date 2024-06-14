package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;
import com.ronnelrazo.broilerchecklist.model.Checklist_address;

import java.util.List;

public class API_Response_reason {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_Reason> checklistReasons;

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

    public List<Checklist_Reason> getChecklistReasons() {
        return checklistReasons;
    }

    public void setChecklistReasons(List<Checklist_Reason> checklistReasons) {
        this.checklistReasons = checklistReasons;
    }
}
