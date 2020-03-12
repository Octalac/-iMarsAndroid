package com.os.imars.surveyor.dao;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PortData implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;


	@SerializedName("price")
	private String price;

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

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}
}