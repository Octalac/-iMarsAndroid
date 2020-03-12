package com.os.imars.dao.mySurvey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyData implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("port")
	private String port;

	@SerializedName("surveytype_name")
	private String surveytypeName;

	@SerializedName("operator_id")
	private String operatorId;

	@SerializedName("username")
	private String username;

	@SerializedName("surveyor_id")
	private String surveyorId;

	@SerializedName("surveyors_name")
	private String surveyorsName;

	@SerializedName("agent_name")
	private String agentName;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("status")
	private String status;

	@SerializedName("last_status")
	private int lastStatus;

	@SerializedName("start_date")
	private String startDate;

	public String getSurvey_category_type() {
		return survey_category_type;
	}

	public void setSurvey_category_type(String survey_category_type) {
		this.survey_category_type = survey_category_type;
	}

	@SerializedName("survey_category_type")
	private String survey_category_type;



	public String getSurvey_number() {
		return survey_number;
	}

	public void setSurvey_number(String survey_number) {
		this.survey_number = survey_number;
	}

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("survey_number")
	private String survey_number;

	public String getVesslesName() {
		return vesslesName;
	}

	public void setVesslesName(String vesslesName) {
		this.vesslesName = vesslesName;
	}

	@SerializedName("vesselsname")
	private String vesslesName;

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

	public void setSurveytypeName(String surveytypeName){
		this.surveytypeName = surveytypeName;
	}

	public String getSurveytypeName(){
		return surveytypeName;
	}

	public void setOperatorId(String operatorId){
		this.operatorId = operatorId;
	}

	public String getOperatorId(){
		return operatorId;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setSurveyorId(String surveyorId){
		this.surveyorId = surveyorId;
	}

	public String getSurveyorId(){
		return surveyorId;
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

	public void setLastStatus(int lastStatus){
		this.lastStatus = lastStatus;
	}

	public int getLastStatus(){
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

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}
}