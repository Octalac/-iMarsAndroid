package com.os.imars.firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.os.imars.application.App;
import com.os.imars.config.Config;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.surveyor.activity.SurveyorHomeActivity;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.Env;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Raj Kumawat on 5/27/2016.
 */


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private final String TAG = "MyFirebaseIIDService";
    private Context context;
    OperatorHomeActivity operatorHomeActivity = new OperatorHomeActivity();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        context = MyFirebaseInstanceIDService.this;
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        Config.setFcmToken(refreshedToken);
        Log.d("token", Config.getFcmToken());
        callApiToChangeDeviceId(refreshedToken);

    }

    private void callApiToChangeDeviceId(String refreshedToken) {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", Config.getUserId());
        myHashMap.put("device_id", refreshedToken);
        RxService apiService = App.getClient().create(RxService.class);
        Call<CommonResponse> call = apiService.change_device_id(myHashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
            }
        });


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Util.notificationManager(MyFirebaseInstanceIDService.this, remoteMessage);
        if (Env.currentActivity!=null && Env.currentActivity instanceof OperatorHomeActivity) {
            ((OperatorHomeActivity)Env.currentActivity).setNotificationCount();
        }

        if (Env.currentActivity!=null && Env.currentActivity instanceof SurveyorHomeActivity) {
            ((SurveyorHomeActivity)Env.currentActivity).setNotificationCount();
        }
    }


}