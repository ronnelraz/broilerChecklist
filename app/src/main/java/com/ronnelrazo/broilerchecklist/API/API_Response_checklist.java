package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_data;


import java.util.List;

public class API_Response_checklist {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Checklist_data> checklistItems;

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

    public List<Checklist_data> getChecklistItems() { // Change the getter method name
        return checklistItems;
    }

    public void setChecklistItems(List<Checklist_data> checklistItems) { // Change the setter method name
        this.checklistItems = checklistItems;
    }
}
