package com.os.imars.operator.dao;

public class MySurveyDao {

    private String name="";
    private String city_name="";

    public MySurveyDao(String name, String city_name, String date, String status) {
        this.name = name;
        this.city_name = city_name;
        this.date = date;
        this.status = status;
    }

    private String date="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status="";
}
