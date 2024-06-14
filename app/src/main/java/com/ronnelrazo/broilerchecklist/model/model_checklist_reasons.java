package com.ronnelrazo.broilerchecklist.model;

public class model_checklist_reasons {

    public int position;
    public long birds;
    public String reason_id;
    public String reason;
    public int ID;


    public model_checklist_reasons(int position, long birds, String reason_id, String reason, int ID) {
        this.position = position;
        this.birds = birds;
        this.reason_id = reason_id;
        this.reason = reason;
        this.ID = ID;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getBirds() {
        return birds;
    }

    public void setBirds(long birds) {
        this.birds = birds;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
