package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.adapter.OperatorsRVAdapter;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserListResponse;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorListActvitity extends BaseActivity {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvOperator;
    private OperatorsRVAdapter operatorsRVAdapter;
    private ImageView imvBack, imvOperatorAdd;
    private Session session;
    private List<UserDataItem> datalist;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout rlOperatorView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_list);
        initView();
        callApi(true);
    }

    private void initView() {
        try {
            datalist = new ArrayList<>();
            session = Session.getInstance(this);
            onClickListener = this;
            shimmerFrameLayout = findViewById(R.id.shimmerView);
            rlOperatorView = findViewById(R.id.rlOperatorView);
            refreshLayout = findViewById(R.id.refreshLayout);
            txtNoDataFound = (TextView) findViewById(R.id.txtNoDataFound);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            imvBack = (ImageView) findViewById(R.id.imvBack);
            imvOperatorAdd = (ImageView) findViewById(R.id.imvOperatorAdd);
            rvOperator = (RecyclerView) findViewById(R.id.rvOperator);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvOperator.setLayoutManager(mLayoutManager);
            rvOperator.setItemAnimator(new DefaultItemAnimator());
            operatorsRVAdapter = new OperatorsRVAdapter(Env.currentActivity, datalist, onClickListener);
            rvOperator.setAdapter(operatorsRVAdapter);
            imvBack.setOnClickListener(onClickListener);
            imvOperatorAdd.setOnClickListener(onClickListener);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    datalist.clear();
                    callApi(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.imvOperatorAdd:
                Intent intent = new Intent(this, OperatorAddActivity.class);
                startActivityForResult(intent, 103);
                break;
            case R.id.btnResend:
                int position = (int) v.getTag();
                callResendApi(position);
                break;
        }
    }


    private void callResendApi(int position) {
        UserDataItem userDataItem = datalist.get(position);
        if (Util.hasInternet(OperatorListActvitity.this)) {
            Util.showProDialog(this);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            myHashMap.put("email", userDataItem.getEmail());
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserResponse> call = apiService.resend_operators(myHashMap);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        UserResponse listResponse = response.body();
                        if (listResponse.getResponse().getStatus() == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OperatorListActvitity.this);
                            builder.setMessage("A new email has been sent to the surveyor to sign up. ");
                            builder.setPositiveButton("OK", (dialog, which) -> {

                            });
                            builder.show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Util.dismissProDialog();
                    Toast.makeText(OperatorListActvitity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void callApi(boolean isShowShimmer) {
        if (isShowShimmer)
            Util.showShimmer(shimmerFrameLayout);

        if (Util.hasInternet(OperatorListActvitity.this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserListResponse> call = apiService.operator_list(myHashMap);
            call.enqueue(new Callback<UserListResponse>() {
                @Override
                public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                    if (response.body() != null) {
                        UserListResponse listResponse = response.body();
                        datalist.addAll(listResponse.getResponse().getData());
                        if (datalist.size() != 0) {
                            Util.hideShimmer(shimmerFrameLayout);
                            refreshLayout.setRefreshing(false);
                            txtNoDataFound.setVisibility(View.GONE);
                            rlOperatorView.setVisibility(View.VISIBLE);
                        } else {
                            refreshLayout.setRefreshing(false);
                            Util.hideShimmer(shimmerFrameLayout);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            rlOperatorView.setVisibility(View.VISIBLE);
                        }
                    }
                    operatorsRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<UserListResponse> call, Throwable t) {
                    Util.hideShimmer(shimmerFrameLayout);
                    Toast.makeText(OperatorListActvitity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            txtNoDataFound.setVisibility(View.GONE);
            UserDataItem operatorData = (UserDataItem) data.getSerializableExtra("responseData");
            operatorsRVAdapter.updateItem(operatorData);
        }
        if (requestCode == 102 && resultCode == RESULT_OK) {
            operatorsRVAdapter.deleteItem();
            if (datalist.size() == 0)
                txtNoDataFound.setVisibility(View.VISIBLE);
            else
                txtNoDataFound.setVisibility(View.GONE);

        }
        if (requestCode == 103 && resultCode == RESULT_OK) {
            txtNoDataFound.setVisibility(View.GONE);
            UserDataItem UserDataItem = (UserDataItem) data.getSerializableExtra("responseData");
            operatorsRVAdapter.addItem(UserDataItem);
        }
    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OperatorListActvitity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datalist.clear();
                callApi(true);

            }
        });
        builder.show();
    }


}