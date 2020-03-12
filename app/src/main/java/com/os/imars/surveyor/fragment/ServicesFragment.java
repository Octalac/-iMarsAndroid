package com.os.imars.surveyor.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.NotificationActivity;
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.dao.notification.NotificationItem;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.surveyor.adapter.ServicesRVAdapter;
import com.os.imars.surveyor.dao.ServiceData;
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


public class ServicesFragment extends BaseFragment {

    private CallApiCallback callApiCallback;
    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRecordFound, txtNotificationCount, txtGoToServicePage;
    private RecyclerView rvServices;
    private ServicesRVAdapter servicesRVAdapter;
    private ArrayList<ServiceData> arrayList = new ArrayList<>();
    private Session session;
    private int notificationCount = 0;
    List<NotificationItem> notificationItemList;
    private RelativeLayout rlTab, rlnotification, rlServiceView;
    ShimmerFrameLayout shimmerFrameLayout;
    SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        init(view);
        get_Notification_list();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallApiCallback) {
            callApiCallback = (CallApiCallback) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callApiCallback.callServerApi(false);
    }


    private void init(View view) {
        try {
            onClickListener = this;
            rlnotification = view.findViewById(R.id.rlnotification);
            refreshLayout = view.findViewById(R.id.refreshLayout);
            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            Util.showShimmer(shimmerFrameLayout);
            session = Session.getInstance(getActivity());
            txtNoRecordFound = (TextView) view.findViewById(R.id.txtNoRecordFound);
            txtGoToServicePage = (TextView) view.findViewById(R.id.txtGoToServicePage);
            txtNotificationCount = (TextView) view.findViewById(R.id.txtNotificationCount);
            rvServices = (RecyclerView) view.findViewById(R.id.rvServices);
            rlServiceView = view.findViewById(R.id.rlServiceView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvServices.setLayoutManager(mLayoutManager);
            rvServices.setItemAnimator(new DefaultItemAnimator());
            rlnotification = view.findViewById(R.id.rlnotification);
            rlnotification.setOnClickListener(onClickListener);
            txtGoToServicePage.setOnClickListener(onClickListener);
            servicesRVAdapter = new ServicesRVAdapter(Env.currentActivity, arrayList);
            rvServices.setAdapter(servicesRVAdapter);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    arrayList.clear();
                    callApiCallback.callServerApi(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlnotification:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.txtGoToServicePage:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://72.octallabs.com/imars/public/signin"));
                startActivity(i);
                break;

        }


    }


    public void get_Notification_list() {
        Log.d("1234", "onResponse: ");
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<NotificationResponse> call = apiService.notification_list(myHashMap);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null) {
                    Log.d("1234", "onResponse: 123");
                    Util.dismissProDialog();
                    NotificationResponse notificationResponse = response.body();
                    if (notificationResponse.getResponse().getStatus() == 1) {
                        Log.d("1234", "onResponse:1234 ");
                        if (notificationResponse.getResponse().getData().getUnreadNotificationCount() != 0) {
                            Log.d("1234", "onResponse: 12345");
                            notificationCount = notificationResponse.getResponse().getData().getUnreadNotificationCount();
                            notificationItemList = notificationResponse.getResponse().getData().getNotification();
                            txtNotificationCount.setVisibility(View.VISIBLE);
                            txtNotificationCount.setText(notificationCount + "");
                        } else {
                            Log.d("1234", "onResponse: 123456");
                            txtNotificationCount.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Util.dismissProDialog();
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("1234", "onActivityResult: ");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Log.d("1234", "onActivityResult: ");
            get_Notification_list();
        }
    }

    public void setAdapterDataList(List<ServiceData> serviceDataList) {
        Util.hideShimmer(shimmerFrameLayout);
        refreshLayout.setRefreshing(false);
        this.arrayList.addAll(serviceDataList);
        if (this.arrayList.size() != 0) {
            rlServiceView.setVisibility(View.VISIBLE);
        } else {
            rlServiceView.setVisibility(View.VISIBLE);
            txtNoRecordFound.setVisibility(View.VISIBLE);
        }
        servicesRVAdapter.notifyDataSetChanged();

    }
}

