package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Checklist_Reason;
import com.ronnelrazo.broilerchecklist.model.transaction_data;

import java.util.List;

public class API_TRANSACTION {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<transaction_data> transactionDataList;

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

    public List<transaction_data> getTransactionDataList() {
        return transactionDataList;
    }

    public void setTransactionDataList(List<transaction_data> transactionDataList) {
        this.transactionDataList = transactionDataList;
    }
}
