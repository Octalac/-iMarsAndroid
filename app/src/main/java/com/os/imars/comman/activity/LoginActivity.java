package com.os.imars.comman.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.config.Config;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.surveyor.activity.SurveyorHomeActivity;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EditText edtEmailAddress;
    private EditText edtPassword;
    private TextView txtForgotPassword;
    private Button btnLogin;
    private TextView txtSignUp;
    private CoordinatorLayout coordinatorLayout;
    private Session session;
    private String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = Session.getInstance(LoginActivity.this);
        initView();

        if (session.isLogin()) {
            if (session.getUserData().getType().equals("0") || session.getUserData().getType().equals("1")) {
                Intent intent = new Intent(LoginActivity.this, OperatorHomeActivity.class);
                startActivity(intent);
                finish();
            } else if (session.getUserData().getType().equals("2") || session.getUserData().getType().equals("3") || session.getUserData().getType().equals("4")) {
                Intent intent = new Intent(LoginActivity.this, SurveyorHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void initView() {
        edtEmailAddress = (EditText) findViewById(R.id.edtEmailAddress);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.txtSignUp:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://72.octallabs.com/imars/public/signup"));
                startActivity(i);
                break;
            case R.id.txtForgotPassword:
                forgotPassword();
                break;
        }
    }

    private void forgotPassword() {
        View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        Button btnReset = view.findViewById(R.id.btnReset);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registeredEmail = edtEmail.getText().toString();
                if (registeredEmail.equals("")) {
                    Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!Util.isValidEmail(registeredEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                } else {
                    Util.showProDialog(LoginActivity.this);
                    HashMap<String, Object> myHashMap = new HashMap<String, Object>();
                    myHashMap.put("email", registeredEmail);
                    RxService apiService = App.getClient().create(RxService.class);
                    Call<CommonResponse> call = apiService.forgot_password(myHashMap);
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            Util.dismissProDialog();
                            if (response.body() != null) {
                                CommonResponse response1 = response.body();
                                Log.d("1234", "onResponse: " + response1);
                                Log.d("1234", "onResponse: " + response1.getResponse().getMessage());
                                if (response1.getResponse().getStatus() == 1) {
                                    Log.d("1234", "onResponse: ");
                                    /*    Util.showSuccessSnackBar(coordinatorLayout, "" + response1.getMessage(), LoginActivity.this);*/
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "" + response1.getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Util.dismissProDialog();
                            Log.d("1234", t.toString());
                        }
                    });
                }
            }
        });


    }

    private void login() {
        email = edtEmailAddress.getText().toString();
        password = edtPassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter email/Password", this);
        } else if (!Util.isValidEmail(email)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            Util.showProDialog(this);
            callApi(email, password);
        }

    }

    private void callApi(String email, String password) {
        if (Util.hasInternet(LoginActivity.this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("email", email);
            myHashMap.put("password", password);
            myHashMap.put("device_id", Config.getFcmToken());
            myHashMap.put("device_type", "Android");
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserResponse> call = apiService.login(myHashMap);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    Log.d("1234", "onResponse: "+response.body().getResponse().getStatus());
                    if (response.body() != null) {
                        UserResponse userDaoDTO = response.body();
                        if (userDaoDTO.getResponse().getStatus() == 1) {
                            UserDataItem data = userDaoDTO.getResponse().getData();
                            Log.d("1234", "onResponse: " + data.getType());
                            if(!TextUtils.isEmpty(data.getUnread_count())){
                                Config.setNotificationCount(data.getUserId());
                            }
                            if (data.getType().equals("0") || data.getType().equals("1")) {
                                Util.dismissProDialog();
                                Config.setUserId(data.getUserId());
                                session.setUserData(data);
                                Intent intent = new Intent(Env.currentActivity, OperatorHomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else if (data.getType().equals("2") || data.getType().equals("3") || data.getType().equals("4")) {
                                Util.dismissProDialog();
                                Config.setUserId(data.getUserId());
                                session.setUserData(data);
                                Intent intent = new Intent(Env.currentActivity, SurveyorHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Util.dismissProDialog();
                            Util.showErrorSnackBar(coordinatorLayout, "" + userDaoDTO.getResponse().getMessage(), LoginActivity.this);
                        }
                    }

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Util.dismissProDialog();
                    Log.d("1234", t.toString());
                    Toast.makeText(LoginActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Util.dismissProDialog();
            noNetworkConnection();
        }
    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApi(email, password);
            }
        });
        builder.show();
    }
}
