package com.os.imars.surveyor.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.mySurvey.SurveyData;
import com.os.imars.dao.mySurvey.SurveyListResponse;
import com.os.imars.dao.mySurveyors.ActiveItem;
import com.os.imars.dao.mySurveyors.MySurveysResponse;
import com.os.imars.surveyor.adapter.UpcomingSurveysRVAdapter;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRecordFound;
    private RecyclerView rvSurveys;
    public UpcomingSurveysRVAdapter upcomingSurveysRVAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout refreshLayout;
    private RelativeLayout rlSurveyView;
    private List<ActiveItem> surveyorDataList;
    private Session session;


    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surveys_chaild_list, container, false);
        init(view);
    //    callUpcomingSurveyApi(true);
        return view;
    }

    private void init(View view) {
        try {
            surveyorDataList = (List<ActiveItem>) getArguments().getSerializable("ActiveSurveyData");
            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            Util.showShimmer(shimmerFrameLayout);
            onClickListener = this;
            session = Session.getInstance(getActivity());
            refreshLayout = view.findViewById(R.id.refreshLayout);
            rlSurveyView = view.findViewById(R.id.rlSurveyView);
            txtNoRecordFound = (TextView) view.findViewById(R.id.txtNoRecordFound);
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            rvSurveys = (RecyclerView) view.findViewById(R.id.rvSurveys);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            upcomingSurveysRVAdapter = new UpcomingSurveysRVAdapter(Env.currentActivity, surveyorDataList);
            rvSurveys.setAdapter(upcomingSurveysRVAdapter);
            rvSurveys.setLayoutManager(mLayoutManager);
            rvSurveys.setItemAnimator(new DefaultItemAnimator());


            if (surveyorDataList.size() == 0){
                txtNoRecordFound.setVisibility(View.VISIBLE);
                rvSurveys.setVisibility(View.GONE);
            }else{
                txtNoRecordFound.setVisibility(View.GONE);
                rvSurveys.setVisibility(View.VISIBLE);
            }

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    surveyorDataList.clear();
                   // callUpcomingSurveyApi(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }



    private void callUpcomingSurveyApi(boolean isShowShimmer) {
        if (isShowShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<MySurveysResponse> call = apiService.survey_all(hashMap);
        call.enqueue(new Callback<MySurveysResponse>() {
            @Override
            public void onResponse(Call<MySurveysResponse> call, Response<MySurveysResponse> response) {

                surveyorDataList.clear();
                rlSurveyView.setVisibility(View.VISIBLE);
                Util.hideShimmer(shimmerFrameLayout);
                refreshLayout.setRefreshing(false);
                if (response.body() != null) {
                    MySurveysResponse listResponse = response.body();
                    surveyorDataList.addAll(listResponse.getResponse().getData().getActive());
                    if (surveyorDataList.size() == 0) {
                        txtNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    upcomingSurveysRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MySurveysResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }

    public void showCustomDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dailog_no_data_found, null, false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        Button btnOk = view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}

