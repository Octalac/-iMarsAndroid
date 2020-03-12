package com.os.imars.dao.mySurvey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SurveyUsersListResponse implements Serializable {

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
			"SurveyUsersListResponse{" + 
			"response = '" + response + '\'' + 
			"}";
		}

	public static class Response implements Serializable {

		@SerializedName("status")
		private int status;

		@SerializedName("message")
		private String message;

		@SerializedName("data")
		private List<SurveyUserDataItem> data;

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

		public void setData(List<SurveyUserDataItem> data){
			this.data = data;
		}

		public List<SurveyUserDataItem> getData(){
			return data;
		}

		@Override
		public String toString(){
			return
					"Response{" +
							"status = '" + status + '\'' +
							",message = '" + message + '\'' +
							",data = '" + data + '\'' +
							"}";
		}
	}
}