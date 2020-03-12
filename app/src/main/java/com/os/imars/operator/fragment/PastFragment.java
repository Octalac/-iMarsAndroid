package com.os.imars.operator.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.os.imars.dao.mySurveyors.PastItem;
import com.os.imars.operator.adapter.PastSurveysRVAdapter;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView rvSurveys;
    public PastSurveysRVAdapter pastSurveysRVAdapter;
    private TextView txtNoRecordFound;
    public List<PastItem> surveyorPastDataList;
    public ShimmerFrameLayout shimmerFrameLayout;
    public SwipeRefreshLayout refreshLayout;
    private RelativeLayout rlSurveyView;
    private Session session;

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surveys_past_chaild_list, container, false);
        init(view);
       // callPastSurveyApi(true);
        return view;
    }

    private void init(View view) {
        try {


            surveyorPastDataList = (List<PastItem>) getArguments().getSerializable("pastSurveyData");
            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            onClickListener = this;
            session = Session.getInstance(getActivity());
            refreshLayout = view.findViewById(R.id.refreshLayout);
            rlSurveyView = view.findViewById(R.id.rlSurveyView);
            txtNoRecordFound = (TextView) view.findViewById(R.id.txtNoRecordFound);
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            rvSurveys = (RecyclerView) view.findViewById(R.id.rvSurveys);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvSurveys.setLayoutManager(mLayoutManager);
            rvSurveys.setItemAnimator(new DefaultItemAnimator());
            pastSurveysRVAdapter = new PastSurveysRVAdapter(Env.currentActivity, surveyorPastDataList, onClickListener);
            rvSurveys.setAdapter(pastSurveysRVAdapter);

            if (surveyorPastDataList.size() == 0){
                txtNoRecordFound.setVisibility(View.VISIBLE);
                rvSurveys.setVisibility(View.GONE);
            }else{
                txtNoRecordFound.setVisibility(View.GONE);
                rvSurveys.setVisibility(View.VISIBLE);
            }



            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                  //  surveyorDataList.clear();
                  //  callPastSurveyApi(false);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public void callPastSurveyApi(boolean isShowShimmer) {
        if (isShowShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        if (Util.hasInternet(getActivity())) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_id", session.getUserData().getUserId());
            Log.d("1234", "callPastSurveyApi: " + Arrays.asList(hashMap));
            Call<SurveyListResponse> call = apiService.survey_list_past(hashMap);
            call.enqueue(new Callback<SurveyListResponse>() {
                @Override
                public void onResponse(Call<SurveyListResponse> call, Response<SurveyListResponse> response) {
                    surveyorDataList.clear();
                    Log.d("1234", "callUpcomingSurveyApi: " + response.body());
                    rlSurveyView.setVisibility(View.VISIBLE);
                    Util.hideShimmer(shimmerFrameLayout);
                    refreshLayout.setRefreshing(false);
                    if (response.body() != null) {
                        Log.d("1234", "onResponse: ");
                        SurveyListResponse listResponse = response.body();
                        surveyorDataList.addAll(listResponse.getResponse().getData());
                        if (surveyorDataList.size() > 0) {
                            txtNoRecordFound.setVisibility(View.GONE);
                        } else {
                            //  showCustomDialog();
                            txtNoRecordFound.setVisibility(View.VISIBLE);
                            rlSurveyView.setVisibility(View.VISIBLE);
                        }
                        pastSurveysRVAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<SurveyListResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    surveyorDataList.clear();
                    rlSurveyView.setVisibility(View.VISIBLE);
                    Util.hideShimmer(shimmerFrameLayout);
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            surveyorDataList.clear();
            rlSurveyView.setVisibility(View.VISIBLE);
            Util.hideShimmer(shimmerFrameLayout);
            refreshLayout.setRefreshing(false);
            noNetworkConnection();
        }

    }*/

/*
    public void noNetworkConnection() {
        Util.dismissProDialog();
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.hideShimmer(shimmerFrameLayout);
                callPastSurveyApi(true);
            }
        });
        builder.show();
    }
*/


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }


}

