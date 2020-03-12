package com.os.imars.surveyor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.os.imars.operator.activity.OperatorListActvitity;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserListResponse;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.surveyor.adapter.SurveyorsRVAdapter;
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

public class SurveyorListActvitity extends BaseActivity {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvSurveyor;
    private SurveyorsRVAdapter surveyorsRVAdapter;
    private ImageView imvBack, imvSurveyorAdd;
    private Button btnDeletedSurveyor;
    private List<UserDataItem> dataList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout rlSurveyorView;
    private SwipeRefreshLayout refreshLayout;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor_list);
        initView();
        callApi(true);
    }

    private void initView() {
        try {
            onClickListener = this;
            dataList = new ArrayList<>();
            session = Session.getInstance(this);
            shimmerFrameLayout = findViewById(R.id.shimmerView);
            rlSurveyorView = findViewById(R.id.rlSurveyorView);
            refreshLayout = findViewById(R.id.refreshLayout);
            txtNoDataFound = (TextView) findViewById(R.id.txtNoDataFound);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            imvBack = (ImageView) findViewById(R.id.imvBack);
            imvSurveyorAdd = (ImageView) findViewById(R.id.imvSurveyorAdd);
            btnDeletedSurveyor = (Button) findViewById(R.id.btnDeletedSurveyor);
            rvSurveyor = (RecyclerView) findViewById(R.id.rvSurveyor);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvSurveyor.setLayoutManager(mLayoutManager);
            rvSurveyor.setItemAnimator(new DefaultItemAnimator());
            imvBack.setOnClickListener(onClickListener);
            imvSurveyorAdd.setOnClickListener(onClickListener);
            btnDeletedSurveyor.setOnClickListener(onClickListener);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dataList.clear();
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
            case R.id.imvSurveyorAdd:
                Intent intent = new Intent(this, SurveyorAddActivity.class);
                startActivityForResult(intent, 103);
                break;
            case R.id.btnDeletedSurveyor:
                startActivity(new Intent(this, SurveyorDeletedActivity.class));
                break;
            case R.id.btnResend:
                int position = (int) v.getTag();
                callResendApi(position);
        }
    }




    private void callResendApi(int position) {

        UserDataItem userDataItem = dataList.get(position);
        if (Util.hasInternet(SurveyorListActvitity.this)) {
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorListActvitity.this);
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
                    Toast.makeText(SurveyorListActvitity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    private void callApi(boolean isShowShimmer) {
        if (isShowShimmer) {
            Util.showShimmer(shimmerFrameLayout);
        }
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<UserListResponse> call = apiService.operator_list(myHashMap);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus() == 1) {
                        btnDeletedSurveyor.setVisibility(View.VISIBLE);
                        Util.hideShimmer(shimmerFrameLayout);
                        refreshLayout.setRefreshing(false);
                        rlSurveyorView.setVisibility(View.VISIBLE);
                        UserListResponse listResponse = response.body();
                        dataList.addAll(listResponse.getResponse().getData());
                        surveyorsRVAdapter = new SurveyorsRVAdapter(SurveyorListActvitity.this, dataList, onClickListener);
                        rvSurveyor.setAdapter(surveyorsRVAdapter);
                        if (dataList.size() == 0) {
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            rvSurveyor.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Util.hideShimmer(shimmerFrameLayout);
                noNetworkConnection();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            String type = data.getStringExtra("type");
            if (type != null) {
                if (type.equals("delete")) {
                    surveyorsRVAdapter.deleteItem();
                    if (dataList.size() == 0)
                        txtNoDataFound.setVisibility(View.VISIBLE);
                        rvSurveyor.setVisibility(View.GONE);
                } else {
                    UserDataItem surveyorData = (UserDataItem) data.getSerializableExtra("userDataItem");
                    surveyorsRVAdapter.updateItem(surveyorData);
                }
            } else {
                UserDataItem surveyorData = (UserDataItem) data.getSerializableExtra("userDataItem");
                surveyorsRVAdapter.updateItem(surveyorData);
            }


        }
        if (requestCode == 103 && resultCode == RESULT_OK) {
            UserDataItem UserDataItem = (UserDataItem) data.getSerializableExtra("responseData");
            surveyorsRVAdapter.addItem(UserDataItem);
        }
    }


    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorListActvitity.this);
        builder.setTitle("Network Error");
        builder.setCancelable(false);
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataList.clear();
                callApi(true);

            }
        });
        builder.show();
    }

}