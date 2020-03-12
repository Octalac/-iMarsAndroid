package com.os.imars.operator.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.CountryDao;
import com.os.imars.operator.dao.CountryDataDao;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.ImagePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnderDPMyProfileActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack, imvEdit;
    private CircleImageView imvUserProfile;
    private Session session;
    private LinearLayout llMain;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView txtEmailAddress, txtUsername, txtAddress, txtCompanyName,
            txtDesignationTitle, txtDesignation, txtCountry, txtCompanyCountry, txtContactNumber,
            txtWebsite, txtDesignationPersonEmail, txtNumberOfSurvey;
    private TextView txtMAddress, txtStreet, txtZip, txtState, txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_dp_operator_my_profile);
        initView();
        callApi(true);
    }


    private void initView() {
        session = Session.getInstance(this);
        imvBack = findViewById(R.id.imvBack);
        imvUserProfile = findViewById(R.id.imvUserProfile);
        imvEdit = findViewById(R.id.imvEdit);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        txtUsername = findViewById(R.id.txtUsername);
        llMain = findViewById(R.id.llMain);
        txtAddress = findViewById(R.id.txtAddress);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtDesignation = findViewById(R.id.txtDesignation);
        txtCountry = findViewById(R.id.txtCountry);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        imvBack.setOnClickListener(this);
        imvEdit.setOnClickListener(this);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtDesignationTitle = findViewById(R.id.txtDesignationTitle);
        txtDesignationPersonEmail = findViewById(R.id.txtDesignationPersonEmail);
        txtNumberOfSurvey = findViewById(R.id.txtNumberOfSurvey);
        txtMAddress = findViewById(R.id.txtMAddress);
        txtStreet = findViewById(R.id.txtStreet);
        txtZip = findViewById(R.id.txtZip);
        txtState = findViewById(R.id.txtState);
        txtCity = findViewById(R.id.txtCity);
        txtCompanyCountry = findViewById(R.id.txtCompanyCountry);

    }

    private void callApi(boolean isShimmer) {
        if (isShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<UserResponse> call = apiService.my_profile(myHashMap);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Util.hideShimmer(shimmerFrameLayout);
                if (response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getResponse().getStatus() == 1) {
                        llMain.setVisibility(View.VISIBLE);
                        setData(userResponse.getResponse().getData());
                    }
                }


            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Util.hideShimmer(shimmerFrameLayout);
                // noNetworkConnection();
            }
        });

    }


    private void setData(UserDataItem dataItem) {
        Log.d("1234", "setData: " + dataItem.getProfilePic() + dataItem.getType());
        Glide.with(this).load(dataItem.getProfilePic()).placeholder(R.drawable.user_icon).into(imvUserProfile);
        txtAddress.setText(dataItem.getCity() + "," + dataItem.getCountryName());
        txtUsername.setText(dataItem.getFirstName() + " " + dataItem.getLastName());
        txtEmailAddress.setText(dataItem.getEmail());
        txtCompanyName.setText(dataItem.getCompany());

        txtContactNumber.setText(dataItem.getCountryCode() + "-" + dataItem.getMobileNumber());
        txtWebsite.setText(dataItem.getCompanyWebsite());
        txtCountry.setText(dataItem.getCountryName());

        txtMAddress.setText("" + dataItem.getAddress());
        txtStreet.setText("" + dataItem.getStreet_address());
        txtCountry.setText("" + dataItem.getCountryName());
        txtZip.setText("" + dataItem.getPincode());
        txtState.setText("" + dataItem.getState());
        txtCompanyCountry.setText("" + dataItem.getCountryName());
        txtDesignationPersonEmail.setText("" + dataItem.getDp_email());
        txtDesignation.setText(dataItem.getDesignated_person());
        txtNumberOfSurvey.setText(""+dataItem.getTotal_no_of_survey());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
        }
    }

}
