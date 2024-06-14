package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_FHF;

import java.util.List;

public class API_TRN_HEADER {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("document_no")
    private String document_no;

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

    public String getDocument_no() {
        return document_no;
    }

    public void setDocument_no(String document_no) {
        this.document_no = document_no;
    }
}
