package com.os.imars.operator.dao.userInfo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDataItem implements Serializable {

	@SerializedName("user_id")
	private String userId;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("email")
	private String email;

	@SerializedName("type")
	private String type;

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("mailing_address")
	private String mailingAddress;

	@SerializedName("country_id")
	private String countryId;

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("company")
	private String company;

	@SerializedName("address")
	private String address;

	public String getIs_signup() {
		return is_signup;
	}

	public void setIs_signup(String is_signup) {
		this.is_signup = is_signup;
	}

	@SerializedName("is_signup")
	private String is_signup;



	@SerializedName("company_tax_id")
	private String companyTaxId;

	@SerializedName("company_website")
	private String companyWebsite;

	@SerializedName("ssn")
	private String ssn;

	@SerializedName("about_me")
	private String aboutMe;

	@SerializedName("experience")
	private String experience;

	@SerializedName("email_verify")
	private String emailVerify;

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@SerializedName("profile_pic")
	private String profilePic;

	@SerializedName("country_name")
	private String countryName;

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getTotal_no_of_survey() {
		return total_no_of_survey;
	}

	public void setTotal_no_of_survey(String total_no_of_survey) {
		this.total_no_of_survey = total_no_of_survey;
	}

	public String getAverage_response_time() {
		return average_response_time;
	}

	public void setAverage_response_time(String average_response_time) {
		this.average_response_time = average_response_time;
	}

	public String getPercentage_job_acceptance() {
		return percentage_job_acceptance;
	}

	public void setPercentage_job_acceptance(String percentage_job_acceptance) {
		this.percentage_job_acceptance = percentage_job_acceptance;
	}

	@SerializedName("rating")
	private String rating;

	@SerializedName("total_no_of_survey")
	private String total_no_of_survey;

	@SerializedName("average_response_time")
	private String average_response_time;

	@SerializedName("percentage_job_acceptance")
	private String percentage_job_acceptance;


	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCountryId(String countryId){
		this.countryId = countryId;
	}

	public String getCountryId(){
		return countryId;
	}

	public void setMobileNumber(String mobileNumber){
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public void setCompany(String company){
		this.company = company;
	}

	public String getCompany(){
		return company;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setCompanyTaxId(String companyTaxId){
		this.companyTaxId = companyTaxId;
	}

	public String getCompanyTaxId(){
		return companyTaxId;
	}

	public void setCompanyWebsite(String companyWebsite){
		this.companyWebsite = companyWebsite;
	}

	public String getCompanyWebsite(){
		return companyWebsite;
	}

	public void setSsn(String ssn){
		this.ssn = ssn;
	}

	public String getSsn(){
		return ssn;
	}

	public void setAboutMe(String aboutMe){
		this.aboutMe = aboutMe;
	}

	public String getAboutMe(){
		return aboutMe;
	}

	public void setExperience(String experience){
		this.experience = experience;
	}

	public String getExperience(){
		return experience;
	}

	public void setEmailVerify(String emailVerify){
		this.emailVerify = emailVerify;
	}

	public String getEmailVerify(){
		return emailVerify;
	}

	public void setProfilePic(String profilePic){
		this.profilePic = profilePic;
	}

	public String getProfilePic(){
		return profilePic;
	}


	public String getUnread_count() {
		return unread_count;
	}

	public void setUnread_count(String unread_count) {
		this.unread_count = unread_count;
	}

	@SerializedName("unread_count")
	private String unread_count;


	@SerializedName("designated_person")
	private String designated_person;

	public String getDesignated_person() {
		return designated_person;
	}

	public void setDesignated_person(String designated_person) {
		this.designated_person = designated_person;
	}

	@SerializedName("street_address")
	private String street_address;

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	@SerializedName("state")
	private String state;


	@SerializedName("city")
	private String city;

	@SerializedName("pincode")
	private String pincode;

	public String getDp_email() {
		return dp_email;
	}

	public void setDp_email(String dp_email) {
		this.dp_email = dp_email;
	}

	@SerializedName("dp_email")
	private String dp_email;


}