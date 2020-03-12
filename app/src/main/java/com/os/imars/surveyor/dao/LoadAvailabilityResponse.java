package com.os.imars.surveyor.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LoadAvailabilityResponse implements Serializable {

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

		public String getIs_avail() {
			return is_avail;
		}

		public void setIs_avail(String is_avail) {
			this.is_avail = is_avail;
		}

		@SerializedName("is_avail")
		private String is_avail;

		@SerializedName("data")
		private List<CalendarDataItem> data;

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

		public void setData(List<CalendarDataItem> data){
			this.data = data;
		}

		public List<CalendarDataItem> getData(){
			return data;
		}
	}
}