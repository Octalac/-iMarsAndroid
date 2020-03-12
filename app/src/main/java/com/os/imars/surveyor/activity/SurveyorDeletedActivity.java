package com.os.imars.surveyor.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserListResponse;
import com.os.imars.surveyor.adapter.DeletedSurveyorsRVAdapter;
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

public class SurveyorDeletedActivity extends BaseActivity {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvDeletedSurveyor;
    private DeletedSurveyorsRVAdapter surveyorsRVAdapter;
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
        setContentView(R.layout.activity_deleted_surveyor);
        initView();
        callApi();
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
            rvDeletedSurveyor = (RecyclerView) findViewById(R.id.rvDeletedSurveyor);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvDeletedSurveyor.setLayoutManager(mLayoutManager);
            rvDeletedSurveyor.setItemAnimator(new DefaultItemAnimator());
            surveyorsRVAdapter = new DeletedSurveyorsRVAdapter(Env.currentActivity, dataList);
            rvDeletedSurveyor.setAdapter(surveyorsRVAdapter);
            imvBack.setOnClickListener(onClickListener);
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

        }
    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(SurveyorDeletedActivity.this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<UserListResponse> call = apiService.deleted_surveyor(myHashMap);
            call.enqueue(new Callback<UserListResponse>() {
                @Override
                public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                    refreshLayout.setRefreshing(false);
                    Util.hideShimmer(shimmerFrameLayout);
                    if (response.body() != null) {
                        UserListResponse listResponse = response.body();
                        dataList.addAll(listResponse.getResponse().getData());
                        if (dataList.size() != 0) {
                            txtNoDataFound.setVisibility(View.GONE);
                            rlSurveyorView.setVisibility(View.VISIBLE);

                        } else {
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            rlSurveyorView.setVisibility(View.VISIBLE);
                        }
                    }

                    surveyorsRVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<UserListResponse> call, Throwable t) {
                    Util.hideShimmer(shimmerFrameLayout);
                    Toast.makeText(SurveyorDeletedActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();
        }
    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyorDeletedActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataList.clear();
                callApi();

            }
        });
        builder.show();
    }
}