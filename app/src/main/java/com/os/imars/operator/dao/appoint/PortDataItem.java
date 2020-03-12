package com.os.imars.operator.dao.appoint;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class PortDataItem implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("country")
	private String country;

	@SerializedName("port")
	private String port;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setPort(String port){
		this.port = port;
	}

	public String getPort(){
		return port;
	}
}