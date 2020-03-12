package com.os.imars.surveyor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserListResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyorDetailsActivity extends BaseActivity {

    private ImageView imvBack, imgEdit;
    private Button btnDeleteSurveyor;
    private TextView txtName, txtEmail, txtMobile;
    private CircleImageView imvSurveyor;
    private Session session;
    private UserDataItem userDataItem;
    private CoordinatorLayout coordinatorLayout;
    private String first_name, last_name, email, mobile, imageUrl, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor_details);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvSurveyor = findViewById(R.id.imvSurveyor);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imgEdit = (ImageView) findViewById(R.id.imgEdit);
        btnDeleteSurveyor = (Button) findViewById(R.id.btnDeleteSurveyor);
        imvBack.setOnClickListener(this);
        btnDeleteSurveyor.setOnClickListener(this);
        imgEdit.setOnClickListener(this);
        userDataItem = (UserDataItem) getIntent().getSerializableExtra("userDataItem");
        setData(userDataItem);
    }

    private void setData(UserDataItem userDataItem) {
        Log.d("1234", "setData: " + userDataItem.getEmail());
        this.userDataItem = userDataItem;
        Util.dismissProDialog();
        id = userDataItem.getUserId();
        Glide.with(this).load(userDataItem.getProfilePic()).placeholder(R.drawable.user_icon).into(imvSurveyor);
        txtEmail.setText(userDataItem.getEmail());
        txtName.setText(userDataItem.getFirstName() + " " + userDataItem.getLastName());
        txtMobile.setText(userDataItem.getMobileNumber());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                Intent intent1 = new Intent();
                intent1.putExtra("userDataItem", userDataItem);
                setResult(RESULT_OK, intent1);
                finish();
                break;
            case R.id.btnDeleteSurveyor:
                deleteSurveyor();
                break;
            case R.id.imgEdit:
                Intent intent = new Intent(this, SurveyorEditActivity.class);
                intent.putExtra("userDataItem", userDataItem);
                startActivityForResult(intent, 101);
                break;

        }
    }

    private void deleteSurveyor() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dailog_delete_user, null, false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(view);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        ImageView imgViewClose = view.findViewById(R.id.imgViewClose);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callApi();


            }
        });
        imgViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void callApi() {
        Util.showProDialog(this);
        if (Util.hasInternet(SurveyorDetailsActivity.this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            myHashMap.put("operator_id", id);
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserListResponse> call = apiService.delete_operators(myHashMap);
            call.enqueue(new Callback<UserListResponse>() {
                @Override
                public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        UserListResponse listResponse = response.body();
                        if (listResponse.getResponse().getStatus() == 1) {
                            Util.showSuccessSnackBar(coordinatorLayout, listResponse.getResponse().getMessage(), SurveyorDetailsActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("type", "delete");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }


                }

                @Override
                public void onFailure(Call<UserListResponse> call, Throwable t) {
                    Util.dismissProDialog();

                    Log.d("1234", "onFailure: " + t.getMessage());
                }
            });
        }else{
            Util.dismissProDialog();
            noNetworkConnection();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            UserDataItem operatorData = (UserDataItem) data.getSerializableExtra("userDataItem");
            setData(operatorData);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        intent1.putExtra("userDataItem", userDataItem);
        setResult(RESULT_OK, intent1);
        finish();

    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorDetailsActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApi();

            }
        });
        builder.show();
    }
}
