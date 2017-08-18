package com.proyecto.geobus.models;

/**
 * Created by alexis on 19/03/17.
 */
import com.google.gson.annotations.SerializedName;

public class RequestClientesDTO {
    @SerializedName("device_id")
    private String id;

    @SerializedName("action")
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
