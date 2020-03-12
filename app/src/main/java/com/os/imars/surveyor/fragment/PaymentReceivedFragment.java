package com.os.imars.surveyor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.os.imars.dao.finance.FinanceResponse;
import com.os.imars.dao.finance.PaidItem;
import com.os.imars.dao.finance.UnpaidItem;
import com.os.imars.surveyor.adapter.UnPaidRVAdapter;
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


public class PaymentReceivedFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRecordFound, textAmount;
    private RecyclerView rvSurveys;
    public UnPaidRVAdapter unPaidRVAdapter;
    public List<PaidItem> paidItemList;
    public ShimmerFrameLayout shimmerFrameLayout;
    public SwipeRefreshLayout refreshLayout;
    private RelativeLayout rlSurveyView, llTotalAmount;
    private Session session;
    private int unPaidAmount = 0;


    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_unpaid, container, false);
        init(view);
        callPaidApi(true);
        return view;
    }


    private void init(View view) {
        try {
            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            llTotalAmount = view.findViewById(R.id.llTotalAmount);
            paidItemList = new ArrayList<>();
            onClickListener = this;
            session = Session.getInstance(getActivity());
            refreshLayout = view.findViewById(R.id.refreshLayout);
            rlSurveyView = view.findViewById(R.id.rlSurveyView);
            txtNoRecordFound = view.findViewById(R.id.txtNoRecordFound);
            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
            textAmount = view.findViewById(R.id.textAmount);
            rvSurveys = view.findViewById(R.id.rvSurveys);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvSurveys.setLayoutManager(mLayoutManager);
            rvSurveys.setItemAnimator(new DefaultItemAnimator());
            unPaidRVAdapter = new UnPaidRVAdapter(Env.currentActivity, paidItemList);
            rvSurveys.setAdapter(unPaidRVAdapter);
            refreshLayout.setOnRefreshListener(() -> {
                paidItemList.clear();
                callPaidApi(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

    }


    public void callPaidApi(boolean isShowShimmer) {
        if (isShowShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        Log.d("1234", "callUpcomingSurveyApi: vinod ");
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<FinanceResponse> call = apiService.finance(hashMap);
        call.enqueue(new Callback<FinanceResponse>() {
            @Override
            public void onResponse(Call<FinanceResponse> call, Response<FinanceResponse> response) {
                paidItemList.clear();
                Log.d("1234", "callUpcomingSurveyApi: " + response.body());
                rlSurveyView.setVisibility(View.VISIBLE);
                Util.hideShimmer(shimmerFrameLayout);
                refreshLayout.setRefreshing(false);
                if (response.body().getResponse().getStatus() == 1) {
                    Log.d("1234", "onResponse: ");
                    FinanceResponse listResponse = response.body();
                    paidItemList.addAll(listResponse.getResponse().getData().getPaid());
                    if (paidItemList.size() > 0) {
                        txtNoRecordFound.setVisibility(View.GONE);
                    } else {
                        txtNoRecordFound.setVisibility(View.VISIBLE);
                        llTotalAmount.setVisibility(View.GONE);
                        rlSurveyView.setVisibility(View.VISIBLE);
                    }
                    unPaidRVAdapter.notifyDataSetChanged();
                }else{
                    txtNoRecordFound.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<FinanceResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }

}


