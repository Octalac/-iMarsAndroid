package com.os.imars.dao.mySurvey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyUserDataItem implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("mobile")
	private String mobile;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}


	public boolean selectedValue = false;

	public boolean isSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(boolean selectedValue) {
		this.selectedValue = selectedValue;
	}

	@Override
 	public String toString(){
		return 
			"SurveyUserDataItem{" +
			"id = '" + id + '\'' + 
			",name = '" + name + '\'' + 
			",mobile = '" + mobile + '\'' + 
			"}";
		}
}