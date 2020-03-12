package com.os.imars.operator.dao.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NotificationResponse implements Serializable {

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

			@SerializedName("notification_count")
			private int notificationCount;

			@SerializedName("unread_notification_count")
			private int unreadNotificationCount;

			@SerializedName("notification")
			private List<NotificationItem> notification;

			public void setNotificationCount(int notificationCount){
				this.notificationCount = notificationCount;
			}

			public int getNotificationCount(){
				return notificationCount;
			}

			public void setUnreadNotificationCount(int unreadNotificationCount){
				this.unreadNotificationCount = unreadNotificationCount;
			}

			public int getUnreadNotificationCount(){
				return unreadNotificationCount;
			}

			public void setNotification(List<NotificationItem> notification){
				this.notification = notification;
			}

			public List<NotificationItem> getNotification(){
				return notification;
			}

		}
	}
}