package com.os.imars.operator.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountryDataDao implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("sortname")
	private String sortname;

	@SerializedName("phonecode")
	private String phonecode;

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

	public void setSortname(String sortname){
		this.sortname = sortname;
	}

	public String getSortname(){
		return sortname;
	}

	public void setPhonecode(String phonecode){
		this.phonecode = phonecode;
	}

	public String getPhonecode(){
		return phonecode;
	}
}