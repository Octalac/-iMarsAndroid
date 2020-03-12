package com.os.imars.surveyor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.activity.AgentAddActivity;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyorEditActivity extends BaseActivity {
    private ImageView imvBack;
    private Button btnSave;
    private String emailAddress, id = "";
    private CoordinatorLayout coordinatorLayout;
    private Session session;
    private EditText edtFirstName, edtLastName, edtEmail, edtMobile;
    private CircleImageView imvSurveyor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_surveyor);
        initView();
        setData();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        btnSave = (Button) findViewById(R.id.btnSave);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvSurveyor = findViewById(R.id.imvSurveyor);
        imvBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnSave:
                updateSurveyor();
                break;
        }
    }

    private void updateSurveyor() {
        emailAddress = edtEmail.getText().toString();
        if (emailAddress.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter email", this);
        } else if (!Util.isEmailValid(emailAddress)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            Util.showProDialog(this);
            RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailAddress);
            RequestBody edit_id = RequestBody.create(MediaType.parse("multipart/form-data"), id);
            RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserResponse> call = apiService.edit_operator(user_id, edit_id, email);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        UserResponse surveyorResponse = response.body();
                        int status = surveyorResponse.getResponse().getStatus();
                        if (status == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorEditActivity.this);
                            builder.setMessage("Surveyor – edited successfully.");
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                Intent intent = new Intent();
                                intent.putExtra("userDataItem", surveyorResponse.getResponse().getData());
                                setResult(RESULT_OK, intent);
                                finish();
                            });
                            builder.show();
                        } else{
                            Util.showErrorSnackBar(coordinatorLayout, "" + surveyorResponse.getResponse().getMessage(), SurveyorEditActivity.this);
                        }
                    }

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Util.dismissProDialog();
                    Log.d("1234", t.toString());
                    noNetworkConnection();
                }
            });
        }

    }

    private void noNetworkConnection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SurveyorEditActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateSurveyor();

            }
        });
        builder.show();
    }

    private void setData() {
        UserDataItem data = (UserDataItem) getIntent().getSerializableExtra("userDataItem");
        if (data != null) {
            id = data.getUserId();
            Glide.with(this).load(data.getProfilePic()).placeholder(R.drawable.user_icon).into(imvSurveyor);
            edtEmail.setText(data.getEmail());
            edtFirstName.setText(data.getFirstName());
            edtLastName.setText(data.getLastName());
            edtMobile.setText(data.getMobileNumber());
        }

    }
}

