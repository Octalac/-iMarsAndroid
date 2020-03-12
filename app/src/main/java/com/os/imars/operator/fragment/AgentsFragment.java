package com.os.imars.operator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.NotificationActivity;
import com.os.imars.operator.activity.AgentAddActivity;
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.adapter.AgentsRVAdapter;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.operator.dao.vessel.VesselData;
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


public class AgentsFragment extends BaseFragment {

    private CallApiCallback callApiCallback;
    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvAgents;
    private AgentsRVAdapter agentsRVAdapter;
    private FloatingActionButton fabAgentBtn;
    private Session session;
    private List<AgentData> dataList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout rlAgentView;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout rlnotification;
    private TextView txtNotificationCount;
    private int notificationCount = 0;


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


    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        try {
            dataList = new ArrayList<>();
            refreshLayout = view.findViewById(R.id.refreshLayout);
            session = Session.getInstance(getActivity());
            onClickListener = this;
            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            Util.showShimmer(shimmerFrameLayout);
            txtNotificationCount = view.findViewById(R.id.txtNotificationCount);
            rlnotification = view.findViewById(R.id.rlnotification);
            txtNoDataFound = view.findViewById(R.id.txtNoDataFound);
            coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
            fabAgentBtn = view.findViewById(R.id.fabAgentBtn);
            rvAgents = view.findViewById(R.id.rvAgents);
            rlAgentView = view.findViewById(R.id.rlAgentView);
            rlAgentView = view.findViewById(R.id.rlAgentView);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvAgents.setLayoutManager(mLayoutManager);
            rvAgents.setItemAnimator(new DefaultItemAnimator());
            agentsRVAdapter = new AgentsRVAdapter(getActivity(), dataList);
            rvAgents.setAdapter(agentsRVAdapter);
            fabAgentBtn.setOnClickListener(this);
            rlnotification.setOnClickListener(this);
            get_Notification_list();
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataList.clear();
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
            case R.id.fabAgentBtn:
                Intent intent = new Intent(Env.currentActivity, AgentAddActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.rlnotification:
                Intent intent1 = new Intent(Env.currentActivity, NotificationActivity.class);
                startActivityForResult(intent1, 101);
                break;
        }

    }

    public void setAdapterDataList(List<AgentData> dataList) {
        Util.hideShimmer(shimmerFrameLayout);
        refreshLayout.setRefreshing(false);
        this.dataList.addAll(dataList);
        if (this.dataList.size() == 0) {
            rlAgentView.setVisibility(View.GONE);
            txtNoDataFound.setVisibility(View.VISIBLE);
        } else {
            rlAgentView.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.GONE);
        }
        agentsRVAdapter.notifyDataSetChanged();
    }


    public void updateItem(AgentData data) {
        if (data != null) {
            rlAgentView.setVisibility(View.VISIBLE);
        } else {
            rlAgentView.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.VISIBLE);

        }
        agentsRVAdapter.updateItem(data);
    }

    public void addItem(AgentData data) {
        Log.d("1234","addItem agent");
        if (data != null) {
            rlAgentView.setVisibility(View.GONE);
            txtNoDataFound.setVisibility(View.VISIBLE);
        } else {
            rlAgentView.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.GONE);
        }
        agentsRVAdapter.addItem(data);
    }


    public void get_Notification_list() {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<NotificationResponse> call = apiService.notification_list(myHashMap);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null) {
                    Util.dismissProDialog();
                    NotificationResponse notificationResponse = response.body();
                    if (notificationResponse.getResponse().getStatus() == 1) {
                        if (notificationResponse.getResponse().getData().getUnreadNotificationCount() != 0) {
                            notificationCount = notificationResponse.getResponse().getData().getUnreadNotificationCount();
                            txtNotificationCount.setVisibility(View.VISIBLE);
                            txtNotificationCount.setText(notificationCount + "");
                        } else {
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

    public void noNetworkConnection() {
        Util.hideShimmer(shimmerFrameLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Util.hasInternet(getActivity())) {
                    Util.showShimmer(shimmerFrameLayout);
                    dataList.clear();
                    callApiCallback.callServerApi(true);
                } else {
                    dataList.clear();
                    callApiCallback.callServerApi(true);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("1234", "onActivityResult: agent");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            get_Notification_list();
            if (data != null){
                AgentData agentData = (AgentData) data.getSerializableExtra("responseData");
                agentsRVAdapter.addItem(agentData);
            }

        }
    }

}

