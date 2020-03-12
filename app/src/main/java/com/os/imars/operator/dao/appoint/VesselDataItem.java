package com.os.imars.operator.dao.appoint;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VesselDataItem implements Serializable {

    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImo_number() {
        return imo_number;
    }

    public void setImo_number(String imo_number) {
        this.imo_number = imo_number;
    }

    @SerializedName("name")
    private String name;

    @SerializedName("imo_number")
    private String imo_number;

}