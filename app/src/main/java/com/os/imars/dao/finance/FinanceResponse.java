package com.os.imars.dao.finance;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FinanceResponse implements Serializable {

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

			@SerializedName("paid")
			private List<PaidItem> paid;

			@SerializedName("unpaid")
			private List<UnpaidItem> unpaid;

			public void setPaid(List<PaidItem> paid){
				this.paid = paid;
			}

			public List<PaidItem> getPaid(){
				return paid;
			}

			public void setUnpaid(List<UnpaidItem> unpaid){
				this.unpaid = unpaid;
			}

			public List<UnpaidItem> getUnpaid(){
				return unpaid;
			}
		}
	}
}