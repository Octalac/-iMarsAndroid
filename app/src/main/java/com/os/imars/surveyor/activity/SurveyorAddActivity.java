package com.os.imars.surveyor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.activity.AgentAddActivity;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.ImagePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyorAddActivity extends BaseActivity {

    private ImageView imvBack;
    private Button btnAdd;
    private EditText edtEmail;
    private CoordinatorLayout coordinatorLayout;
    private ImagePickerUtility imagePickerUtility;
    private File fileImage = null;
    private Session session;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_surveyor);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        edtEmail = findViewById(R.id.edtEmail);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        imvBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAdd:
                addSurveyor();
                break;
        }
    }

    private void addSurveyor() {
        email = edtEmail.getText().toString();
        if (email.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter email", this);
        } else if (!Util.isEmailValid(email)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            Util.showProDialog(this);
            if (Util.hasInternet(SurveyorAddActivity.this)) {
                RequestBody emailAddress = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RxService apiService = App.getClient().create(RxService.class);
                Call<UserResponse> call = apiService.add_operator(user_id, emailAddress);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            UserResponse userResponse = response.body();
                            int status = userResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorAddActivity.this);
                                builder.setMessage("You have added a new surveyor… An email is sent to the new surveyor to sign up.");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", userResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + userResponse.getResponse().getMessage(), SurveyorAddActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(SurveyorAddActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Util.dismissProDialog();
                noNetworkConnection();
            }
        }
    }

    private void noNetworkConnection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SurveyorAddActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addSurveyor();

            }
        });
        builder.show();
    }
}

