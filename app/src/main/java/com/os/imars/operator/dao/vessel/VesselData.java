package com.os.imars.operator.dao.vessel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VesselData implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("imo_number")
    private String imo_number;
    @SerializedName("company")
    private String company;
    @SerializedName("address")
    private String address;

    @SerializedName("email")
    private String email;

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    @SerializedName("image")
    private String image;
    @SerializedName("is_favourite")
    private String isFavourite;

    public String getSame_as_company() {
        return same_as_company;
    }

    public void setSame_as_company(String same_as_company) {
        this.same_as_company = same_as_company;
    }

    public String getSame_as_company_address() {
        return same_as_company_address;
    }

    public void setSame_as_company_address(String same_as_company_address) {
        this.same_as_company_address = same_as_company_address;
    }

    @SerializedName("same_as_company")
    private String same_as_company;

    @SerializedName("same_as_company_address")
    private String same_as_company_address;


    public String getAdditionalEmail() {
        return additionalEmail;
    }

    public void setAdditionalEmail(String additionEmail) {
        this.additionalEmail = additionEmail;
    }

    @SerializedName("additional_email")
    private String additionalEmail;

    @SerializedName("name")
    private String name;


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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


    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @SerializedName("pincode")
    private String pincode;


}
