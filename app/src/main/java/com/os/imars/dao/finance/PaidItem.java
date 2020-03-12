package com.os.imars.dao.finance;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaidItem implements Serializable {

	@SerializedName("id")
	private String id;

	@SerializedName("survey_number")
	private String surveyNumber;

	@SerializedName("invoice_date")
	private String invoiceDate;

	@SerializedName("invoice_amount")
	private int invoiceAmount;

	@SerializedName("vessels_name")
	private String vesselsName;

	@SerializedName("port_name")
	private String portName;

	@SerializedName("survey_code")
	private String surveyCode;

	@SerializedName("status")
	private String status;

	public int getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(int invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	@SerializedName("invoice")
	private String invoice;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSurveyNumber(String surveyNumber){
		this.surveyNumber = surveyNumber;
	}

	public String getSurveyNumber(){
		return surveyNumber;
	}

	public void setInvoiceDate(String invoiceDate){
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceDate(){
		return invoiceDate;
	}

	public void setVesselsName(String vesselsName){
		this.vesselsName = vesselsName;
	}

	public String getVesselsName(){
		return vesselsName;
	}

	public void setPortName(String portName){
		this.portName = portName;
	}

	public String getPortName(){
		return portName;
	}

	public void setSurveyCode(String surveyCode){
		this.surveyCode = surveyCode;
	}

	public String getSurveyCode(){
		return surveyCode;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setInvoice(String invoice){
		this.invoice = invoice;
	}

	public String getInvoice(){
		return invoice;
	}
}