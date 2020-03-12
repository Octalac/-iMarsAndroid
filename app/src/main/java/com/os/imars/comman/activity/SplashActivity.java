package com.os.imars.comman.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.os.imars.R;
import com.os.imars.config.Config;
import com.os.imars.service.OnClearFromRecentService;
import com.os.imars.utility.Session;
import com.os.imars.views.BaseView.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static final int RequestPermissionCode = 1;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
       // getKeyHash();
       /* if (bundle != null && !TextUtils.isEmpty(bundle.getString("notification_type"))) {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
            finish();
        } else */
        //  getKeyHash();
    }

    private void init() {
        Log.d("1234", "init: ");
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d("1234", "run: " + Build.VERSION.SDK_INT);
                    Log.d("1234", "run: " + Build.VERSION_CODES.M);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d("1234", "init: 12");
                        if (checkPermission()) {
                            Log.d("1234", "init: 1");
                            goNextScreen();
                        } else {
                            Log.d("1234", "init: 2");
                            requestPermission();
                        }
                    } else {
                        goNextScreen();
                    }

                }
            }, SPLASH_TIME_OUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION}, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && WriteExternalPermission && ReadExternalPermission) {
                        goNextScreen();
                    } else {
                        goNextScreen();
                    }
                }
                break;
        }
    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void goNextScreen() {
        Log.d("1234", "init:123 ");
        try {
            Config.setNotificationManager("");
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.octal.cargofeb", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onClick(View view) {

    }
}

