package com.os.imars.dao.mySurveyors;

import com.google.gson.annotations.SerializedName;

public class MySurveysResponse{

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
			"MySurveysResponse{" + 
			"response = '" + response + '\'' + 
			"}";
		}
}