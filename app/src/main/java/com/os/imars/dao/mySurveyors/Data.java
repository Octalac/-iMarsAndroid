package com.os.imars.dao.mySurveyors;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

	@SerializedName("past")
	private List<PastItem> past;

	@SerializedName("active")
	private List<ActiveItem> active;

	public void setPast(List<PastItem> past){
		this.past = past;
	}

	public List<PastItem> getPast(){
		return past;
	}

	public void setActive(List<ActiveItem> active){
		this.active = active;
	}

	public List<ActiveItem> getActive(){
		return active;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"past = '" + past + '\'' + 
			",active = '" + active + '\'' + 
			"}";
		}
}