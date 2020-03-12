package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.adapter.AppointCustomSurveyorAdapter;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.surveyor.SurveyorUseListResponse;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointCustomSurveyorActivity extends BaseActivity {


    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack;
    private RecyclerView rvAppointSurvey;
    private AppointCustomSurveyorAdapter upcomingSurveysRVAdapter;
    private List<SurveyorUserData> surveyorUserDataList;
    private Button btnNext;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String portId = "", categoryId = "", vesselId = "", endDate = "", startDate = "", agentId = "", surveyType = "";
    private List<AgentData> agentsDataItemList;
    private RelativeLayout rlSurveyView;
    private TextView txtNoDataFound;
    private LinearLayout llBottom;
    private CheckBox chkAssignCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_custom_surveyor);
        initView();
        callApi();
    }

    private void initView() {
        portId = getIntent().getStringExtra("portId");
        categoryId = getIntent().getStringExtra("categoryId");
        vesselId = getIntent().getStringExtra("vesselId");
        startDate = getIntent().getStringExtra("start_date");
        endDate = getIntent().getStringExtra("end_date");
        surveyType = getIntent().getStringExtra("surveyType");
        agentsDataItemList = (ArrayList<AgentData>) getIntent().getSerializableExtra("agentsDataItemList");
        surveyorUserDataList = new ArrayList<>();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        rvAppointSurvey = (RecyclerView) findViewById(R.id.rvAppointSurvey);
        rlSurveyView = findViewById(R.id.rlSurveyView);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        llBottom = findViewById(R.id.llBottom);
        chkAssignCondition = findViewById(R.id.chkAssignCondition);

        imvBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
        rvAppointSurvey.setLayoutManager(mLayoutManager);
        rvAppointSurvey.setItemAnimator(new DefaultItemAnimator());
        upcomingSurveysRVAdapter = new AppointCustomSurveyorAdapter(Env.currentActivity, surveyorUserDataList);
        rvAppointSurvey.setAdapter(upcomingSurveysRVAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnNext:
                calltoNextAppointCustomSurvey();
                break;
        }
    }

    private void calltoNextAppointCustomSurvey() {
        String selectedId = upcomingSurveysRVAdapter.checkedValue();
        Intent intent = new Intent(this, NextAppointCustomSurveyorActivity.class);
        intent.putExtra("selectedId", selectedId);
        intent.putExtra("agentsDataItemList", (Serializable) agentsDataItemList);
        intent.putExtra("portId", portId);
        intent.putExtra("vesselId", vesselId);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);
        intent.putExtra("surveyType", surveyType);
        startActivity(intent);

    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(AppointCustomSurveyorActivity.this)) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("survey_category_id", categoryId);
            myHashMap.put("start_date", startDate);
            myHashMap.put("end_date", endDate);
            myHashMap.put("port_id", portId);
            Call<SurveyorUseListResponse> call = apiService.survey_user_list(myHashMap);
            call.enqueue(new Callback<SurveyorUseListResponse>() {
                @Override
                public void onResponse(Call<SurveyorUseListResponse> call, Response<SurveyorUseListResponse> response) {
                    Util.hideShimmer(shimmerFrameLayout);
                    rlSurveyView.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        SurveyorUseListResponse listResponse = response.body();
                        surveyorUserDataList.addAll(listResponse.getResponse().getData());
                        if (surveyorUserDataList.size() > 0) {
                            rvAppointSurvey.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.GONE);
                            llBottom.setVisibility(View.VISIBLE);
                            chkAssignCondition.setVisibility(View.GONE);
                            btnNext.setVisibility(View.VISIBLE);
                        } else {
                            rvAppointSurvey.setVisibility(View.GONE);
                            llBottom.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(AppointCustomSurveyorActivity.this);
                            builder.setMessage("No Surveyor Found");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    }
                    upcomingSurveysRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<SurveyorUseListResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.dismissProDialog();
                    Toast.makeText(AppointCustomSurveyorActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
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
                Util.hideShimmer(shimmerFrameLayout);
                rlSurveyView.setVisibility(View.VISIBLE);
                callApi();
            }
        });
        builder.show();
    }

}

