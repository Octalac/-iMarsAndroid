package com.os.imars.dao.mySurveyors;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PastItem implements Serializable {

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("surveyors_name")
	private String surveyorsName;

	@SerializedName("agent_name")
	private String agentName;

	@SerializedName("operator_id")
	private String operatorId;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("last_status")
	private int lastStatus;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("surveyor_id")
	private String surveyorId;

	@SerializedName("port")
	private String port;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("vesselsname")
	private String vesselsname;

	@SerializedName("surveytype_name")
	private String surveytypeName;

	@SerializedName("id")
	private String id;


	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	@SerializedName("device_id")
	private String device_id;

	@SerializedName("survey_number")
	private String surveyNumber;

	@SerializedName("username")
	private String username;

	@SerializedName("status")
	private String status;

	@SerializedName("start_date")
	private String startDate;

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
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

	public void setOperatorId(String operatorId){
		this.operatorId = operatorId;
	}

	public String getOperatorId(){
		return operatorId;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setLastStatus(int lastStatus){
		this.lastStatus = lastStatus;
	}

	public int getLastStatus(){
		return lastStatus;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setSurveyorId(String surveyorId){
		this.surveyorId = surveyorId;
	}

	public String getSurveyorId(){
		return surveyorId;
	}

	public void setPort(String port){
		this.port = port;
	}

	public String getPort(){
		return port;
	}

	public void setInstruction(String instruction){
		this.instruction = instruction;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setVesselsname(String vesselsname){
		this.vesselsname = vesselsname;
	}

	public String getVesselsname(){
		return vesselsname;
	}

	public void setSurveytypeName(String surveytypeName){
		this.surveytypeName = surveytypeName;
	}

	public String getSurveytypeName(){
		return surveytypeName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSurveyNumber(String surveyNumber){
		this.surveyNumber = surveyNumber;
	}

	public String getSurveyNumber(){
		return surveyNumber;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
 	public String toString(){
		return 
			"PastItem{" + 
			"end_date = '" + endDate + '\'' + 
			",surveyors_name = '" + surveyorsName + '\'' + 
			",agent_name = '" + agentName + '\'' + 
			",operator_id = '" + operatorId + '\'' + 
			",image_url = '" + imageUrl + '\'' + 
			",last_status = '" + lastStatus + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",surveyor_id = '" + surveyorId + '\'' + 
			",port = '" + port + '\'' + 
			",instruction = '" + instruction + '\'' + 
			",vesselsname = '" + vesselsname + '\'' + 
			",surveytype_name = '" + surveytypeName + '\'' + 
			",id = '" + id + '\'' + 
			",survey_number = '" + surveyNumber + '\'' + 
			",username = '" + username + '\'' + 
			",status = '" + status + '\'' + 
			",start_date = '" + startDate + '\'' + 
			"}";
		}
}