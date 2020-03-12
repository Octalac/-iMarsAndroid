package com.os.imars.surveyor.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserAvailableResponse implements Serializable {

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
		private Data data;

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

		public void setData(Data data){
			this.data = data;
		}

		public Data getData(){
			return data;
		}

		public static class Data implements Serializable {

			@SerializedName("id")
			private String id;

			@SerializedName("status")
			private String status;

			public void setId(String id){
				this.id = id;
			}

			public String getId(){
				return id;
			}

			public void setStatus(String status){
				this.status = status;
			}

			public String getStatus(){
				return status;
			}
		}
	}

}