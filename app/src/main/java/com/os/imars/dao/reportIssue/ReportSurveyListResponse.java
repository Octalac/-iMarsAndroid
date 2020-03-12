package com.os.imars.dao.reportIssue;

import com.google.gson.annotations.SerializedName;

public class ReportSurveyListResponse {

	@SerializedName("response")
	private Response response;

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	@Override
 	public String toString(){
		return 
			"ReportSurveyListResponse{" +
			"response = '" + response + '\'' + 
			"}";
		}
}