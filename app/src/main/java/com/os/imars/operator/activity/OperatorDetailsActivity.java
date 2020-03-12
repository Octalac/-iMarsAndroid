package com.os.imars.operator.activity;

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
import android.widget.Toast;

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

public class OperatorDetailsActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack, imvOperatorRemove;
    private String first_name, last_name, email, mobile, imageUrl, id;
    private TextView txtFirstName, txtLastName, txtEmail, txtMobile;
    private CircleImageView imvOperator;
    private Session session;
    private UserDataItem userDataItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_details);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvOperatorRemove = findViewById(R.id.imvOperatorRemove);
        imvBack = findViewById(R.id.imvBack);
        imvOperator = findViewById(R.id.imvOperator);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        imvOperatorRemove.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        userDataItem = (UserDataItem) getIntent().getSerializableExtra("UserDataItem");
        setData(userDataItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.imvOperatorRemove:
                deleteOperator();
                break;

        }
    }

    private void setData(UserDataItem userDataItem) {
        Util.dismissProDialog();
        id = userDataItem.getUserId();
        Log.d("1234", "setData: " + first_name + last_name + mobile + email + imageUrl + id);
        Glide.with(this).load(userDataItem.getProfilePic()).placeholder(R.drawable.user_icon).into(imvOperator);
        txtEmail.setText(userDataItem.getEmail());
        txtFirstName.setText(userDataItem.getFirstName());
        txtLastName.setText(userDataItem.getLastName());
        txtMobile.setText(userDataItem.getMobileNumber());
    }

    private void deleteOperator() {
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
                callApi();
                dialog.dismiss();
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
        if (Util.hasInternet(this)) {
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
                            Util.showSuccessSnackBar(coordinatorLayout, listResponse.getResponse().getMessage(), OperatorDetailsActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("", "1");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }


                }

                @Override
                public void onFailure(Call<UserListResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.dismissProDialog();
                    Toast.makeText(OperatorDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Util.dismissProDialog();
            noNetworkConnection();
        }
    }

    public void noNetworkConnection() {
        Util.dismissProDialog();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
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
