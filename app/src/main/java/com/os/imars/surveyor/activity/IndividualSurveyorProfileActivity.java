package com.os.imars.surveyor.activity;

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
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualSurveyorProfileActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack, imvUserProfileEdit, imvEdit;
    private Session session;
    private CircleImageView imvUserProfile;
    private TextView txtEmailAddress, txtCompanyWebsite, txtPhoneNumber, txtTaxIDNumber, txtUsername, txtAddress, txtCompanyName, txtCountry, txtContactNumber, txtWebsite, txtSSSNumber, txtNumberOfSurvey, txtExperience, txtAboutMe, txtRating, txtAverageResponseTime, txtJobAcceptance;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ScrollView scrollView;
    private View viewSSN;
    private LinearLayout llSSN, llMain;
    private RatingBar ratingBar;
    private TextView txtMAddress, txtStreet, txtZip, txtState, txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_surveyor_profile);
        initView();
        callApi();
    }

    private void initView() {
        session = Session.getInstance(this);
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        scrollView = findViewById(R.id.scrollView);
        imvBack = findViewById(R.id.imvBack);
        viewSSN = findViewById(R.id.viewSSN);
        llSSN = findViewById(R.id.llSSN);
        llMain = findViewById(R.id.llMain);
        imvUserProfile = findViewById(R.id.imvUserProfile);
        imvEdit = findViewById(R.id.imvEdit);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        ratingBar = findViewById(R.id.ratingBar);
        txtUsername = findViewById(R.id.txtUsername);
        txtAverageResponseTime = findViewById(R.id.txtAverageResponseTime);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtAboutMe = findViewById(R.id.txtAboutMe);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtSSSNumber = findViewById(R.id.txtSSSNumber);
        txtExperience = findViewById(R.id.txtExperience);
        txtRating = findViewById(R.id.txtRating);
        txtJobAcceptance = findViewById(R.id.txtJobAcceptance);
        txtAddress = findViewById(R.id.txtAddress);
        txtMAddress = findViewById(R.id.txtMAddress);
        txtStreet = findViewById(R.id.txtStreet);
        txtZip = findViewById(R.id.txtZip);
        txtState = findViewById(R.id.txtState);
        txtCity = findViewById(R.id.txtCity);
        txtCountry = findViewById(R.id.txtCountry);

        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtTaxIDNumber = findViewById(R.id.txtTaxIDNumber);

        imvEdit.setOnClickListener(this);
        imvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.imvEdit:
                Intent intent = new Intent(IndividualSurveyorProfileActivity.this, IndividualSurveyorProfileActivity.class);
                startActivityForResult(intent, 101);
                break;
        }
    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<UserResponse> call = apiService.my_profile(myHashMap);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body() != null) {
                    Util.hideShimmer(shimmerFrameLayout);
                    scrollView.setVisibility(View.VISIBLE);
                    llMain.setVisibility(View.VISIBLE);
                    UserResponse userResponse = response.body();
                    if (userResponse.getResponse().getStatus() == 1) {
                        setData(userResponse.getResponse().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Util.hideShimmer(shimmerFrameLayout);

            }
        });

    }

    private void setData(UserDataItem data) {
        Log.d("1234", "setData: " + data.getRating());
        Glide.with(this).load(data.getProfilePic()).placeholder(R.drawable.user_icon).into(imvUserProfile);
        txtAddress.setText(data.getCity() + ", " + data.getCountryName());
        txtUsername.setText(data.getFirstName() + " " + data.getLastName());
        txtEmailAddress.setText(data.getEmail());
        if (!data.getRating().equals(""))
            ratingBar.setRating(Float.parseFloat(data.getRating()));
        txtRating.setText("(" + data.getRating() + ")");
        txtAboutMe.setText(data.getAboutMe());
        txtExperience.setText(data.getExperience());
        txtSSSNumber.setText(data.getSsn());
        txtJobAcceptance.setText(data.getPercentage_job_acceptance());
        txtAverageResponseTime.setText(data.getAverage_response_time());
        txtPhoneNumber.setText(data.getCountryCode()+"-"+data.getMobileNumber());
        txtTaxIDNumber.setText(""+data.getCompanyTaxId());
        txtMAddress.setText("" + data.getAddress());
        txtStreet.setText("" + data.getStreet_address());
        txtCountry.setText("" + data.getCountryName());
        txtZip.setText("" + data.getPincode());
        txtState.setText("" + data.getState());
        txtCity.setText("" + data.getCity());


    }


}
