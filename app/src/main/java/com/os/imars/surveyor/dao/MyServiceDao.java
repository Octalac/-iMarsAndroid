package com.os.imars.surveyor.dao;

public class MyServiceDao {

    private String name="";
    private String cost_of_survey="";

    public MyServiceDao(String name, String cost_of_survey,String status) {
        this.name = name;
        this.cost_of_survey = cost_of_survey;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getCost_of_survey() {
        return cost_of_survey;
    }

    public void setCost_of_survey(String cost_of_survey) {
        this.cost_of_survey = cost_of_survey;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status="";
}
