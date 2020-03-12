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
import androidx.appcompat.app.AlertDialog;
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
import com.os.imars.operator.adapter.UnPaidRVAdapter;
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


public class PendingPaymentFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRecordFound, textAmount;
    private RecyclerView rvSurveys;
    public UnPaidRVAdapter unPaidRVAdapter;
    public List<UnpaidItem> paidItemList;
    public ShimmerFrameLayout shimmerFrameLayout;
    public SwipeRefreshLayout refreshLayout;
    private RelativeLayout rlSurveyView;
    private Session session;
    private int paidAmount = 0;
    private RelativeLayout llTotalAmount;

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unpaid, container, false);
        init(view);
        callUnpaidApi(true);
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
            textAmount = view.findViewById(R.id.textAmount);
            txtNoRecordFound = view.findViewById(R.id.txtNoRecordFound);
            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
            rvSurveys = view.findViewById(R.id.rvSurveys);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvSurveys.setLayoutManager(mLayoutManager);
            rvSurveys.setItemAnimator(new DefaultItemAnimator());
            unPaidRVAdapter = new UnPaidRVAdapter(Env.currentActivity, paidItemList);
            rvSurveys.setAdapter(unPaidRVAdapter);
            refreshLayout.setOnRefreshListener(() -> {
                paidItemList.clear();
                callUnpaidApi(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

    }


    public void callUnpaidApi(boolean isShowShimmer) {
        if (isShowShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        if (Util.hasInternet(getActivity())) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user_id", session.getUserData().getUserId());
            Call<FinanceResponse> call = apiService.finance(hashMap);
            call.enqueue(new Callback<FinanceResponse>() {
                @Override
                public void onResponse(Call<FinanceResponse> call, Response<FinanceResponse> response) {
                    paidItemList.clear();
                    rlSurveyView.setVisibility(View.VISIBLE);
                    Util.hideShimmer(shimmerFrameLayout);
                    refreshLayout.setRefreshing(false);
                    if (response.body() != null) {
                        FinanceResponse listResponse = response.body();
                        paidItemList.addAll(listResponse.getResponse().getData().getUnpaid());
                        if (paidItemList.size() > 0) {
                            txtNoRecordFound.setVisibility(View.GONE);
                        } else {
                            txtNoRecordFound.setVisibility(View.VISIBLE);
                            llTotalAmount.setVisibility(View.GONE);
                            rlSurveyView.setVisibility(View.VISIBLE);
                        }
                        unPaidRVAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<FinanceResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.hideShimmer(shimmerFrameLayout);
                    Toast.makeText(getActivity(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();
        }
    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callUnpaidApi(true);
            }
        });
        builder.show();
    }



}





