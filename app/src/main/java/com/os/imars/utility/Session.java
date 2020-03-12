package com.os.imars.utility;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.os.imars.operator.dao.userInfo.UserDataItem;


public class Session {
    static SharedPreferences file;
    static SharedPreferences.Editor edit;

    private static String IS_LOGIN = "is_login";
    private static String USER_ID = "user_id";
    private static String FIRST_NAME = "first_name";
    private static String LAST_NAME = "last_name";
    private static String EMAIL = "email";
    private static String IS_SIGNUP = "is_signup";
    private static String COUNTRY_CODE = "country_code";
    private static String COUNTRY_NAME = "country_name";
    private static String COUNTRY_ID = "country_id";
    private static String MOBILE_NUMBER = "mobile_number";
    private static String TYPE = "type";
    private static String ADDRESS = "address";
    private static String PROFILE_PIC = "profile_pic";
    private static String COMPANY = "company";
    private static String COMPANY_ADDRESS = "company_address";
    private static String COMPANY_TAX_ID = "company_tax_id";
    private static String COMPANY_WEBSITE = "company_website";
    private static String SSN = "ssn";
    private static String ABOUT_ME = "about_me";
    private static String RATING = "rating";
    private static String EXPERIENCE = "experience";
    private static String IS_DEACTIVATE = "is_deactivate";
    private static String percentage_job_acceptance = "percentage_job_acceptance";



    static Session session = null;

    public static Session getInstance(Context context) {
        file = PreferenceManager.getDefaultSharedPreferences(context);
        edit = file.edit();
        if (session == null)
            session = new Session();
        return session;
    }

    public boolean isLogin() {
        return file.getBoolean(IS_LOGIN, false);
    }

    public void setLogin() {
        edit.putBoolean(IS_LOGIN, true);
        edit.commit();
    }

    public void setNotificationCount(int login_type) {
        edit.putInt("notification_count", login_type);
        edit.commit();
    }

    public String getCalendarAvailabilityStatus() {
        return file.getString("status", "");
    }

    public void setCalendarAvailabilityStatus(String status) {
        edit.putString("status", status);
        edit.commit();
    }


    public int getCalendarAvailabilityPosition() {
        return file.getInt("position", 0);
    }

    public void setCalendarAvailabilityPosition(int position) {
        edit.putInt("position", position);
        edit.commit();
    }


    public int getNotificationCount() {
        return file.getInt("notification_count", 0);
    }



    public void setIsAvailable(boolean isAvailable) {
        edit.putBoolean("isAvailable", isAvailable);
        edit.commit();
    }

    public boolean getIsAvailable() {
        return file.getBoolean("isAvailable", false);
    }



    public void setFilterStatus(int login_type) {
        edit.putInt("filterStatus", login_type);
        edit.commit();
    }

    public int getFilterStatus() {
        return file.getInt("filterStatus", 0);
    }


    public String getUserId() {
        return file.getString(USER_ID, "");
    }


    public void setUserId(String userId) {
        edit.putString(USER_ID, userId);
        edit.commit();
    }

    public void setUserData(UserDataItem userData) {
        edit.putString(USER_ID, userData.getUserId());
        edit.putString(FIRST_NAME, userData.getFirstName());
        edit.putString(LAST_NAME, userData.getLastName());
        edit.putString(IS_SIGNUP, userData.getIs_signup());
        edit.putString(EMAIL, userData.getEmail());
        edit.putString(MOBILE_NUMBER, userData.getMobileNumber());
        edit.putString(TYPE, userData.getType());
        edit.putString(ADDRESS, userData.getAddress());
        edit.putString(PROFILE_PIC, userData.getProfilePic());
        edit.putString(COMPANY, userData.getCompany());
        edit.putString(COUNTRY_ID, userData.getCountryId());
        edit.putString(COUNTRY_NAME, userData.getCountryName());
        edit.putString(COUNTRY_CODE, userData.getCountryCode());
        edit.putString(COMPANY_TAX_ID, userData.getCompanyTaxId());
        edit.putString(COMPANY_WEBSITE, userData.getCompanyWebsite());
        edit.putString(RATING, userData.getRating());
        edit.putString(SSN, userData.getSsn());
        edit.putString(ABOUT_ME, userData.getAboutMe());
        edit.putString(percentage_job_acceptance, userData.getPercentage_job_acceptance());
        edit.putBoolean(IS_LOGIN, true);
        edit.commit();
    }

    public void setLogout() {
        edit.putBoolean(IS_LOGIN, false);
        edit.clear();
        edit.commit();
    }

    public UserDataItem getUserData() {
        UserDataItem userData = new UserDataItem();
        userData.setEmail(file.getString(EMAIL, ""));
        userData.setIs_signup(file.getString(EMAIL, ""));
        userData.setFirstName(file.getString(FIRST_NAME, ""));
        userData.setLastName(file.getString(LAST_NAME, ""));
        userData.setCountryCode(file.getString(COUNTRY_CODE, ""));
        userData.setMobileNumber(file.getString(MOBILE_NUMBER, ""));
        userData.setUserId(file.getString(USER_ID, ""));
        userData.setType(file.getString(TYPE, ""));
        userData.setProfilePic(file.getString(PROFILE_PIC, ""));
        userData.setCompany(file.getString(COMPANY, ""));
        userData.setCountryName(file.getString(COUNTRY_NAME, ""));
        userData.setCountryId(file.getString(COUNTRY_ID, ""));
        userData.setCountryCode(file.getString(COUNTRY_CODE, ""));
        userData.setRating(file.getString(RATING, ""));
        userData.setAddress(file.getString(ADDRESS, ""));
        userData.setCompanyWebsite(file.getString(COMPANY_WEBSITE, ""));
        userData.setCompanyTaxId(file.getString(COMPANY_TAX_ID, ""));
        userData.setSsn(file.getString(SSN, ""));
        userData.setAboutMe(file.getString(ABOUT_ME, ""));
        userData.setPercentage_job_acceptance(file.getString(percentage_job_acceptance, ""));
        return userData;
    }
}
