package com.ronnelrazo.broilerchecklist.model;

public class model_signatures {
    public int position;
    public String signature;
    public String username;

    public model_signatures(int position, String signature, String username) {
        this.position = position;
        this.signature = signature;
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
