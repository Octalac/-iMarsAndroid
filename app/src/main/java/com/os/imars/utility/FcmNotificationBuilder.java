package com.os.imars.utility;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationBuilder {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FcmNotificationBuilder";
    private static final String SERVER_API_KEY = "AAAAyVCwPdo:APA91bFYeZCzN1qjjA2pVenLoPLXKm92hQc1ExhkPxDXh-w1O_a8pO0xwSh3RiibB75Zw5Z9SxmRFmckwifj_IyK30geYtd95VAzXsTHEbNnWwmpm6gUeX_EY7I_PiYJedh0YtGkyTtn";

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_KEY = "key=" + SERVER_API_KEY;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";


    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_TITLE = "title";        //sender name is title
    private static final String KEY_BODY = "body";
    private static final String KEY_TEXT_MESSAGE = "body";
    private static final String KEY_DATA = "data";
    private static final String KEY_NOTIFICATION_IOS = "notification";
    private static final String KEY_SENDER_USERNAME = "username";
    private static final String KEY_SENDER_UID = "uid";
    private static final String KEY_SENDER_FCM_TOKEN = "fcm_token";
    public static final String KEY_OPEN_ACTIVITY_TYPE = "activity_type";

    private String mTitle;
    private String mMessage;
    private String mSenderUsername;
    private String mSenderUid;
    private String mSenderFirebaseToken;
    private String mReceiverFirebaseToken;
    private HashMap<String, String> customDataList = new HashMap<>();

    /* Calling
    * FcmNotificationBuilder.initialize()
//                .title(senderName)
                .message(message)
                .username(senderName)
                .uid(senderId)
                .setCustomData(FcmNotificationBuilder.KEY_OPEN_ACTIVITY_TYPE,"chat")
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    * */
    private FcmNotificationBuilder() {

    }

    public static FcmNotificationBuilder initialize() {
        return new FcmNotificationBuilder();
    }

    public FcmNotificationBuilder title(String title) {
        mTitle = title;
        return this;
    }

    public FcmNotificationBuilder message(String message) {
        mMessage = message;
        return this;
    }

    public FcmNotificationBuilder username(String username) {
        mSenderUsername = username;
        return this;
    }

    public FcmNotificationBuilder uid(String uid) {
        mSenderUid = uid;
        return this;
    }

    public FcmNotificationBuilder firebaseToken(String mSenderFirebaseToken) {
        this.mSenderFirebaseToken = mSenderFirebaseToken;
        return this;
    }

    public FcmNotificationBuilder receiverFirebaseToken(String receiverFirebaseToken) {
        mReceiverFirebaseToken = receiverFirebaseToken;
        return this;
    }

    public FcmNotificationBuilder setCustomData(String key, String value) {
        customDataList.put(key, value);
        return this;
    }


    public void send() {
        RequestBody requestBody = null;
        try {
            requestBody = RequestBody.create(MEDIA_TYPE_JSON, getValidJsonBody().toString());
            Log.e("json", getValidJsonBody().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .addHeader(CONTENT_TYPE, APPLICATION_JSON)
                .addHeader(AUTHORIZATION, AUTH_KEY)
                .url(FCM_URL)
                .post(requestBody)
                .build();

        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onGetAllUsersFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse: " + response.body().string());
            }
        });
    }


    private JSONObject getValidJsonBody() throws JSONException {


        JSONObject jsonObjectBody = new JSONObject();


        jsonObjectBody.put(KEY_TO, mReceiverFirebaseToken);

        JSONObject jsonObjectBody_notification = new JSONObject();      //for ios
        jsonObjectBody_notification.put(KEY_BODY, mMessage);            //for ios
        jsonObjectBody_notification.put(KEY_TITLE, mSenderUsername); //for ios

        JSONObject jsonObjectData = new JSONObject();
       // jsonObjectData.put(KEY_TITLE, mSenderUsername);     //for ios
      //  jsonObjectData.put(KEY_TEXT_MESSAGE, mMessage);
        for (Map.Entry<String, String> entry : customDataList.entrySet()) {
            String key = entry.getKey();
            jsonObjectData.put(key, entry.getValue());
            Log.d("key=value", key + "=" + entry.getValue());
        }

        jsonObjectData.put("sound", "default");

        jsonObjectBody.put(KEY_DATA, jsonObjectData);       // for android

         jsonObjectBody.put(KEY_NOTIFICATION_IOS, jsonObjectBody_notification);        //for ios

        return jsonObjectBody;
    }

}
