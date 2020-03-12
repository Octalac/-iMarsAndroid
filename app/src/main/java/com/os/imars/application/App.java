package com.os.imars.application;

import android.content.Context;
import android.view.Window;

import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.os.imars.config.Config;
import com.os.imars.utility.Constants;
import com.os.imars.views.BaseView.Env;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Raj Kumawat on 08/11/2017.
 */

public class App extends MultiDexApplication {
    public static Context APP_CONTEXT;
    private static Retrofit retrofit = null;
    private static Retrofit new_retrofit=null;
    private Window window;



    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        APP_CONTEXT = getApplicationContext();
        Env.init(this);
        Config.init(APP_CONTEXT);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                System.exit(1);
            }
        });
    }

    public static Retrofit getClient() {
        if (new_retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(120, TimeUnit.SECONDS);
            httpClient.readTimeout(120, TimeUnit.SECONDS);
            httpClient.writeTimeout(120, TimeUnit.SECONDS);
            httpClient.addInterceptor(logging);
            new_retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constants.Main_BASE_URL).client(httpClient.build()).build();
        }
        return new_retrofit;
    }

    public static Retrofit getInitialClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(120, TimeUnit.SECONDS);
            builder.readTimeout(120, TimeUnit.SECONDS);
            builder.writeTimeout(120, TimeUnit.SECONDS);
            builder.addInterceptor(logging);

            OkHttpClient okHttpClient = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.Main_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}
