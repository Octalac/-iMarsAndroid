package com.os.imars.operator.dao.surveyor;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SurveyorUserData implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("pricing")
    private String pricing;

    @SerializedName("rating")
    private String rating;

    @SerializedName("average_response_time")
    private String averageResponseTime;

    @SerializedName("percentage_job_acceptance")
    private String percentageJobAcceptance;

    @SerializedName("email")
    private String email;

    @SerializedName("image")
    private String image;

    private boolean isChecked;


    public String getSurveyorType() {
        return surveyorType;
    }

    public void setSurveyorType(String surveyorType) {
        this.surveyorType = surveyorType;
    }

    private String surveyorType = "";

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    private String isStatus ="0";

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public String getPricing() {
        return pricing;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setAverageResponseTime(String averageResponseTime) {
        this.averageResponseTime = averageResponseTime;
    }

    public String getAverageResponseTime() {
        return averageResponseTime;
    }

    public void setPercentageJobAcceptance(String percentageJobAcceptance) {
        this.percentageJobAcceptance = percentageJobAcceptance;
    }

    public String getPercentageJobAcceptance() {
        return percentageJobAcceptance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

}