package com.os.imars.operator.dao.notification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationItem implements Serializable {

				@SerializedName("id")
				private int id;

				@SerializedName("noti_type")
				private String notiType;

				@SerializedName("user_type")
				private String userType;

				@SerializedName("notification")
				private String notification;

				@SerializedName("user_id")
				private String userId;

				@SerializedName("created_at")
				private String createdAt;

				@SerializedName("is_read")
				private int isRead;

				public void setId(int id){
					this.id = id;
				}

				public int getId(){
					return id;
				}

				public void setNotiType(String notiType){
					this.notiType = notiType;
				}

				public String getNotiType(){
					return notiType;
				}

				public void setUserType(String userType){
					this.userType = userType;
				}

				public String getUserType(){
					return userType;
				}

				public void setNotification(String notification){
					this.notification = notification;
				}

				public String getNotification(){
					return notification;
				}

				public void setUserId(String userId){
					this.userId = userId;
				}

				public String getUserId(){
					return userId;
				}

				public void setCreatedAt(String createdAt){
					this.createdAt = createdAt;
				}

				public String getCreatedAt(){
					return createdAt;
				}

				public void setIsRead(int isRead){
					this.isRead = isRead;
				}

				public int getIsRead(){
					return isRead;
				}
			}