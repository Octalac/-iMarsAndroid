package com.os.imars.comman.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.operator.activity.AppointSurveyorActivity;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.adapter.NotificationRVAdapter;
import com.os.imars.operator.dao.notification.NotificationItem;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.SwipeHelper;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvNotification;
    private NotificationRVAdapter notificationRVAdapter;
    private ImageView imvBack, imvDelete;
    private Session session;
    private List<NotificationItem> notificationItemList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout rlNotificationView;
    private SwipeRefreshLayout refreshLayout;
    private int isUnReadNotificationCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initView();
        get_Notification_list();
    }

    private void initView() {
        try {
            session = Session.getInstance(this);
            onClickListener = this;
            shimmerFrameLayout = findViewById(R.id.shimmerView);
            notificationItemList = new ArrayList<>();
            rlNotificationView = findViewById(R.id.rlNotificationView);
            imvDelete = findViewById(R.id.imvDelete);
            refreshLayout = findViewById(R.id.refreshLayout);
            txtNoDataFound = (TextView) findViewById(R.id.txtNoDataFound);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            imvBack = (ImageView) findViewById(R.id.imvBack);
            rvNotification = (RecyclerView) findViewById(R.id.rvNotification);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvNotification.setLayoutManager(mLayoutManager);
            rvNotification.setItemAnimator(new DefaultItemAnimator());
            notificationRVAdapter = new NotificationRVAdapter(this, notificationItemList, onClickListener);
            rvNotification.setAdapter(notificationRVAdapter);
            imvBack.setOnClickListener(onClickListener);
            imvDelete.setOnClickListener(onClickListener);
            setSwipeToShowDeleteButton();
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    notificationItemList.clear();
                    txtNoDataFound.setVisibility(View.GONE);
                    get_Notification_list();
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
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.imvDelete:
                deleteAllNotification();
                break;
            case R.id.view_foreground: {
                int position = (int) v.getTag();
                isReadNotification(position);
            }
        }
    }

    private void deleteAllNotification() {

        if (notificationItemList.size() != 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
            builder.setMessage("All notifications will be deleted!!! Do you want to proceed?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Util.showProDialog(NotificationActivity.this);
                    HashMap<String, Object> myHashMap = new HashMap<String, Object>();
                    myHashMap.put("user_id", session.getUserData().getUserId());
                    myHashMap.put("type", "All");
                    myHashMap.put("notification_id", "");
                    RxService apiService = App.getClient().create(RxService.class);
                    Call<CommonResponse> call = apiService.notification_delete(myHashMap);
                    call.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.body() != null) {
                                Util.dismissProDialog();
                                CommonResponse response1 = response.body();
                                if (response1.getResponse().getStatus() == 1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                                    builder.setMessage("All notifications have been deleted…");
                                    builder.setPositiveButton("OK", (dialog, which) -> {
                                        notificationItemList.clear();
                                        notificationRVAdapter.notifyDataSetChanged();
                                        txtNoDataFound.setVisibility(View.VISIBLE);
                                    });
                                    builder.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Util.hideShimmer(shimmerFrameLayout);
                            Log.d("1234", "onFailure: " + t.getMessage());
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
        }
    }


    private void get_Notification_list() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(NotificationActivity.this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<NotificationResponse> call = apiService.notification_list(myHashMap);
            call.enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    notificationItemList.clear();
                    if (response.body() != null) {
                        Util.hideShimmer(shimmerFrameLayout);
                        refreshLayout.setRefreshing(false);
                        NotificationResponse response1 = response.body();
                        isUnReadNotificationCount = response1.getResponse().getData().getUnreadNotificationCount();
                        notificationItemList.addAll(response1.getResponse().getData().getNotification());
                        if (response1.getResponse().getStatus() == 1) {
                            if (response1.getResponse().getData().getNotification().size() != 0) {
                                rlNotificationView.setVisibility(View.VISIBLE);
                                notificationRVAdapter.notifyDataSetChanged();
                            }

                        } else {
                            txtNoDataFound.setVisibility(View.VISIBLE);
                            rlNotificationView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    Util.hideShimmer(shimmerFrameLayout);
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Toast.makeText(NotificationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            noNetworkConnection();
        }
    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                get_Notification_list();
            }
        });
        builder.show();
    }

    private void setSwipeToShowDeleteButton() {

        new SwipeHelper(NotificationActivity.this, rvNotification) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {

                underlayButtons.add(new UnderlayButton("delete", 0, ContextCompat.getColor(NotificationActivity.this, R.color.red_700), new UnderlayButtonClickListener() {
                    @Override
                    public void onClick(final int pos) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
                        builder.setMessage("Do you want to delete this notification?");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            NotificationItem notificationItem = notificationItemList.get(pos);
                            Util.showProDialog(NotificationActivity.this);
                            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
                            myHashMap.put("user_id", session.getUserData().getUserId());
                            myHashMap.put("type", "");
                            myHashMap.put("notification_id", notificationItem.getId());
                            RxService apiService = App.getClient().create(RxService.class);
                            Call<CommonResponse> call = apiService.notification_delete(myHashMap);
                            call.enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                                    if (response.body() != null) {
                                        CommonResponse response1 = response.body();
                                        Log.d("1234", "onResponse: " + response1.getResponse().getStatus());
                                        Util.dismissProDialog();
                                        notificationItemList.remove(pos);
                                        notificationRVAdapter.notifyItemRemoved(pos);
                                        if (notificationItemList.size() == 0) {
                                            txtNoDataFound.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<CommonResponse> call, Throwable t) {
                                    Util.hideShimmer(shimmerFrameLayout);
                                    Log.d("1234", "onFailure: " + t.getMessage());
                                }
                            });

                        });

                        builder.show();


                    }
                }));
            }
        };
    }

    public void isReadNotification(final int pos) {
        NotificationItem notificationItem = notificationItemList.get(pos);
        Util.showProDialog(NotificationActivity.this);
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("is_read", "1");
        myHashMap.put("notification_id", notificationItem.getId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<CommonResponse> call = apiService.notification_read(myHashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, retrofit2.Response<CommonResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    CommonResponse response1 = response.body();
                    if (response1.getResponse().getStatus() == 1) {
                        int count = session.getNotificationCount();
                        session.setNotificationCount(count - 1);
                        notificationItemList.get(pos).setIsRead(1);
                        notificationRVAdapter.notifyItemChanged(pos);

                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
                Util.dismissProDialog();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

/*    public void showCustomDialog(){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dailog_no_data_found, null, false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
    }*/
}
