package com.os.imars.surveyor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnderDPSurveyorMyProfileActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack, imvUserProfileEdit, imvEdit;
    private Session session;
    private CircleImageView imvUserProfile;
    private TextView txtEmailAddress, txtCompanyWebsite, txtUsername, txtAddress,
            txtCompanyName, txtDesignation, txtCountry, txtContactNumber, txtWebsite,
            txtSSSNumber, txtNumberOfSurvey, txtExperience, txtAboutMe, txtRating, txtCompanyCountry, txtDesignationPersonEmail,
            txtAverageResponseTime, txtJobAcceptance, txtPhoneNumber;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ScrollView scrollView;
    private View viewSSN;
    private LinearLayout llSSN, llMain;
    private RatingBar ratingBar;
    private TextView txtMAddress, txtStreet, txtZip, txtState, txtCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor_under_dp_my_profile);
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
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyWebsite = findViewById(R.id.txtCompanyWebsite);
        txtDesignation = findViewById(R.id.txtDesignation);
        txtAverageResponseTime = findViewById(R.id.txtAverageResponseTime);
        txtDesignationPersonEmail = findViewById(R.id.txtDesignationPersonEmail);
        txtWebsite = findViewById(R.id.txtWebsite);
        txtAboutMe = findViewById(R.id.txtAboutMe);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtContactNumber = findViewById(R.id.txtContactNumber);
        txtSSSNumber = findViewById(R.id.txtSSSNumber);
        txtNumberOfSurvey = findViewById(R.id.txtNumberOfSurvey);
        txtExperience = findViewById(R.id.txtExperience);
        txtRating = findViewById(R.id.txtRating);
        txtJobAcceptance = findViewById(R.id.txtJobAcceptance);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);

        txtMAddress = findViewById(R.id.txtMAddress);
        txtStreet = findViewById(R.id.txtStreet);
        txtZip = findViewById(R.id.txtZip);
        txtState = findViewById(R.id.txtState);
        txtCity = findViewById(R.id.txtCity);
        txtCountry = findViewById(R.id.txtCountry);

        imvEdit.setOnClickListener(this);
        imvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
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
        txtAddress.setText(data.getCity() + "," + data.getCountryName());
        txtUsername.setText(data.getFirstName() + " " + data.getLastName());
        txtEmailAddress.setText(data.getEmail());
        txtCompanyName.setText(data.getCompany());
        if (!data.getRating().equals(""))
            ratingBar.setRating(Float.parseFloat(data.getRating()));
        txtNumberOfSurvey.setText(data.getTotal_no_of_survey());
        txtRating.setText("(" + data.getRating() + ")");
        txtCompanyWebsite.setText(data.getCompanyWebsite());
        txtAboutMe.setText(data.getAboutMe());
        txtExperience.setText(data.getExperience());
        txtSSSNumber.setText(data.getSsn());
        txtJobAcceptance.setText(data.getPercentage_job_acceptance());
        txtAverageResponseTime.setText(data.getAverage_response_time());
        txtDesignationPersonEmail.setText(data.getDp_email());
        txtMAddress.setText("" + data.getAddress());
        txtStreet.setText("" + data.getStreet_address());
        txtCountry.setText("" + data.getCountryName());
        txtZip.setText("" + data.getPincode());
        txtState.setText("" + data.getState());
        txtCity.setText("" + data.getCity());
        txtDesignation.setText("" + data.getDesignated_person());
        txtPhoneNumber.setText(data.getCountryCode()+"-"+data.getMobileNumber());
    }

}
