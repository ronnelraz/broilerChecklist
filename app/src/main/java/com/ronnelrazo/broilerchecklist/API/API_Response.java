package com.ronnelrazo.broilerchecklist.API;

import com.google.gson.annotations.SerializedName;
import com.ronnelrazo.broilerchecklist.model.Login_data;

public class API_Response {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Login_data login_data;


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

    public Login_data getLogin_data() {
        return login_data;
    }

    public void setLogin_data(Login_data login_data) {
        this.login_data = login_data;
    }
}
