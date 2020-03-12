package com.os.imars.operator.dao.appoint;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CategoryDataItem implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("name")
	private String name;

	@SerializedName("price")
	private String code;

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

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}
}