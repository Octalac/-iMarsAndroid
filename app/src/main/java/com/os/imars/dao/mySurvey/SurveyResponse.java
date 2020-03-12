package com.os.imars.dao.mySurvey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyResponse implements Serializable {

	@SerializedName("response")
	private Response response;

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	public static class Response implements Serializable {

		@SerializedName("status")
		private int status;

		@SerializedName("message")
		private String message;

		@SerializedName("data")
		private SurveyData surveyData;

		public void setStatus(int status){
			this.status = status;
		}

		public int getStatus(){
			return status;
		}

		public void setMessage(String message){
			this.message = message;
		}

		public String getMessage(){
			return message;
		}

		public void setSurveyData(SurveyData surveyData){
			this.surveyData = surveyData;
		}

		public SurveyData getSurveyData(){
			return surveyData;
		}
	}
}