package com.os.imars.operator.dao.surveyor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyorData implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("port")
    private String port;

        @SerializedName("surveytype_name")
    private String surveycateName;

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @SerializedName("username")
    private String username;

    @SerializedName("operator_id")
    private String operatorId;

    @SerializedName("surveyors_name")
    private String surveyorsName;

    @SerializedName("agent_name")
    private String agentName;

    @SerializedName("instruction")
    private String instruction;

    @SerializedName("status")
    private String status;

    @SerializedName("last_status")
    private String lastStatus;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    public String getSurveyor_id() {
        return surveyor_id;
    }

    public void setSurveyor_id(String surveyor_id) {
        this.surveyor_id = surveyor_id;
    }

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("surveyor_id")
    private String surveyor_id;


    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setPort(String port){
        this.port = port;
    }

    public String getPort(){
        return port;
    }

    public void setSurveycateName(String surveycateName){
        this.surveycateName = surveycateName;
    }

    public String getSurveycateName(){
        return surveycateName;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }

    public void setSurveyorsName(String surveyorsName){
        this.surveyorsName = surveyorsName;
    }

    public String getSurveyorsName(){
        return surveyorsName;
    }

    public void setAgentName(String agentName){
        this.agentName = agentName;
    }

    public String getAgentName(){
        return agentName;
    }

    public void setInstruction(String instruction){
        this.instruction = instruction;
    }

    public String getInstruction(){
        return instruction;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public void setLastStatus(String lastStatus){
        this.lastStatus = lastStatus;
    }

    public String getLastStatus(){
        return lastStatus;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    public String getCreatedAt(){
        return createdAt;
    }
}
