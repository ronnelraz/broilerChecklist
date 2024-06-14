package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_Header;
import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;

import java.util.List;

public class API_Response_header {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_Header> checklistHeaders;

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

    public List<Checklist_Header> getChecklistHeaders() {
        return checklistHeaders;
    }

    public void setChecklistHeaders(List<Checklist_Header> checklistHeaders) {
        this.checklistHeaders = checklistHeaders;
    }
}
