package com.os.imars.surveyor.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CalendarDataItem implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("status")
	private String status;

	@Override
	public String toString() {
		return "CalendarDataItem{" +
				"id=" + id +
				", status='" + status + '\'' +
				", start='" + start + '\'' +
				", end='" + end + '\'' +
				", type='" + type + '\'' +
				", imageurl='" + imageurl + '\'' +
				'}';
	}

	@SerializedName("start")
	private String start;

	@SerializedName("end")
	private String end;

	@SerializedName("type")
	private String type;

	@SerializedName("imageurl")
	private String imageurl;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setStart(String start){
		this.start = start;
	}

	public String getStart(){
		return start;
	}

	public void setEnd(String end){
		this.end = end;
	}

	public String getEnd(){
		return end;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setImageurl(String imageurl){
		this.imageurl = imageurl;
	}

	public String getImageurl(){
		return imageurl;
	}
}