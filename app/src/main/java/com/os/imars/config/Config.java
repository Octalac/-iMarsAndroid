package com.os.imars.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Raj Kumawat on 08/11/2017.
 */

public final class Config {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCES = "preferences";

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void setFcmToken(String fcmToken) {
        editor.putString("FcmToken", fcmToken).apply();
    }

    public static String getFcmToken() {
        return preferences.getString("FcmToken", null);
    }

    public static void setUserToken(String userToken) {
        editor.putString("userToken", userToken).apply();
    }

    public static String getUserToken() {
        return preferences.getString("userToken", null);
    }

    public static void setNotificationManager(String username) {
        editor.putString("noty_boy", username).apply();
    }

    public static String getNotificationManager() {
        return preferences.getString("noty_boy", "");
    }

    public static void setLongitude(String longitude) {
        editor.putString("Longitude", longitude).apply();
    }

    public static String getLongitude() {
        return preferences.getString("Longitude", null);
    }


    public static void setLatitude(String lat) {
        editor.putString("Latitude", lat).apply();
    }

    public static String getLatitude() {
        return preferences.getString("Latitude", null);
    }

    public static void setUserId(String user_id) {
        editor.putString("user_id", user_id).apply();
    }

    public static String getSocialId() {
        return preferences.getString("SocialId", null);
    }

    public static void setSocialId(String SocialId) {
        editor.putString("SocialId", SocialId).apply();
    }

    public static String getMobileNumber() {
        return preferences.getString("mobile_number", null);
    }

    public static void setMobileNumber(String mobile) {
        editor.putString("mobile_number", mobile).apply();
    }

    public static String getUserId() {
        return preferences.getString("user_id", "");
    }

    public static void setFirstName(String first_name) {
        editor.putString("first_name", first_name).apply();
    }

    public static String getFirstName() {
        return preferences.getString("first_name", null);
    }


    public static void setLastName(String last_name) {
        editor.putString("last_name", last_name).apply();
    }

    public static String getLastName() {
        return preferences.getString("last_name", null);
    }

    public static String getLanguage() {
        return preferences.getString("language", "eng");
    }

    public static void setLanguage(String language) {
        editor.putString("language", language).apply();
    }



    public static void setUserName(String username) {
        editor.putString("username", username).apply();
    }

    public static String getUserName() {
        return preferences.getString("username", null);
    }

    public static void setEmail(String email) {
        editor.putString("email", email).apply();
    }

    public static String getEmail() {
        return preferences.getString("email", null);
    }

    public static void setLoginType(String login_type) {
        editor.putString("login_type", login_type).apply();
    }

    public static String getLoginType() {
        return preferences.getString("login_type", null);
    }

    public static void setNotificationCount(String notification_count) {
        editor.putString("notification_count", notification_count).apply();
    }

    public static String getNotificationCount() {
        return preferences.getString("notification_count", null);
    }

    public static void setCity(String login_type) {
        editor.putString("city", login_type).apply();
    }

    public static String getCity() {
        return preferences.getString("city", null);
    }


    public static void setNationality(String login_type) {
        editor.putString("nationality", login_type).apply();
    }

    public static String getNationality() {
        return preferences.getString("nationality", null);
    }


    public static void setAddress(String login_type) {
        editor.putString("address", login_type).apply();
    }

    public static String getAddress() {
        return preferences.getString("address", null);
    }


    public static void setDefaultImg(String img) {
        editor.putString("img", img).apply();
    }

    public static String getDefaultImg() {
        return preferences.getString("img", null);
    }

    public static void setLogoutStatus(boolean img) {
        editor.putBoolean("status", img).apply();
    }

    public static boolean getLogoutStatus() {
        return preferences.getBoolean("status", false);
    }

    public static String getGender() {
        return preferences.getString("gender", "");
    }

    public static void setGender(String first_name) {
        editor.putString("gender", first_name).apply();
    }

    public static void savePreferences() {
        editor.commit();
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }

    public static void removeValueForKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            editor.remove(key);
            savePreferences();
        }

    }
}
