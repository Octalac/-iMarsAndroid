package com.os.imars.dao.reportIssue;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("id")
	private String id;

	@SerializedName("survey_number")
	private String surveyNumber;

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

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"id = '" + id + '\'' + 
			",survey_number = '" + surveyNumber + '\'' + 
			"}";
		}
}