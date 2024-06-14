package com.ronnelrazo.broilerchecklist.model;

public class model_farm {

    public String farm_code,farm_name;

    public model_farm(String farm_code, String farm_name) {
        this.farm_code = farm_code;
        this.farm_name = farm_name;
    }

    public String getFarm_code() {
        return farm_code;
    }

    public void setFarm_code(String farm_code) {
        this.farm_code = farm_code;
    }

    public String getFarm_name() {
        return farm_name;
    }

    public void setFarm_name(String farm_name) {
        this.farm_name = farm_name;
    }
}
