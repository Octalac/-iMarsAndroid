package com.os.imars.operator.dao.agent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AgentData implements Serializable {

        @SerializedName("id")
        private String id;

        @SerializedName("first_name")
        private String first_name;

        @SerializedName("last_name")
        private String last_name;

        @SerializedName("user_id")
        private String user_id;

        @SerializedName("mobile")
        private String mobile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @SerializedName("email")
        private String email;

        @SerializedName("image")
        private String image;


    }

