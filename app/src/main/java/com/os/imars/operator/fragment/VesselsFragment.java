package com.os.imars.operator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.activity.VesselAddActivity;
import com.os.imars.operator.adapter.VesselsRVAdapter;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.operator.dao.vessel.VesselResponse;
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


public class VesselsFragment extends BaseFragment {

    private CallApiCallback callApiCallback;
    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoDataFound;
    private RecyclerView rvVessels;
    private VesselsRVAdapter vesselsRVAdapter;
    private FloatingActionButton febAddVessels;
    private Session session;
    private SwipeRefreshLayout refreshLayout;
    private List<VesselData> vesselDataList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout rlVesselsView;
    private RelativeLayout rlNotification;
    private TextView txtNotificationCount;
    private int notificationCount = 0;
    private static boolean flag = true;
    private ImageView imvFavourite, imvSearchBack, imvSearchClose, imvSearch;
    private LinearLayout llSearch;
    public EditText edtSearch;

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
        View view = inflater.inflate(R.layout.fragment_vessels, container, false);
        Log.d("1234", "onCreateViewPost: ");
        init(view);
        return view;
    }

    private void init(View view) {
        try {

            shimmerFrameLayout = view.findViewById(R.id.shimmerView);
            Util.showShimmer(shimmerFrameLayout);
            vesselDataList = new ArrayList<>();
            session = Session.getInstance(Env.currentActivity);
            onClickListener = this;
            rlVesselsView = view.findViewById(R.id.rlVesselsView);
            imvFavourite = view.findViewById(R.id.imvFavourite);
            imvSearchBack = view.findViewById(R.id.imvSearchBack);
            llSearch = view.findViewById(R.id.llSearch);
            imvSearch = view.findViewById(R.id.imvSearch);
            edtSearch = view.findViewById(R.id.edtSearch);
            imvSearchClose = view.findViewById(R.id.imvSearchClose);
            refreshLayout = view.findViewById(R.id.refreshLayout);
            rvVessels = (RecyclerView) view.findViewById(R.id.rvVessels);
            txtNoDataFound = (TextView) view.findViewById(R.id.txtNoDataFound);
            febAddVessels = (FloatingActionButton) view.findViewById(R.id.febAddVessels);
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            rlNotification = view.findViewById(R.id.rlNotification);
            txtNotificationCount = view.findViewById(R.id.txtNotificationCount);
            febAddVessels.setOnClickListener(this);
            rlNotification.setOnClickListener(this);
            imvFavourite.setOnClickListener(this);
            llSearch.setOnClickListener(this);
            imvSearch.setOnClickListener(this);
            imvSearchBack.setOnClickListener(this);
            imvSearchClose.setOnClickListener(this);
            edtSearch.setOnClickListener(this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvVessels.setLayoutManager(mLayoutManager);
            rvVessels.setItemAnimator(new DefaultItemAnimator());
            vesselsRVAdapter = new VesselsRVAdapter(getActivity(), vesselDataList, onClickListener);
            rvVessels.setAdapter(vesselsRVAdapter);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    vesselDataList.clear();
                    callApiCallback.callServerApi(true);
                }
            });
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() == 0) {
                        imvSearchClose.setVisibility(View.GONE);
                    } else {
                        imvSearchClose.setVisibility(View.VISIBLE);
                    }
                    String isSearch = s + "/" + "search";
                    vesselsRVAdapter.getFilter().filter(isSearch);

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            get_Notification_list();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.febAddVessels:
                Intent intent = new Intent(Env.currentActivity, VesselAddActivity.class);
                getActivity().startActivityForResult(intent, 101);
                break;
            case R.id.rlNotification:
                Intent intent1 = new Intent(Env.currentActivity, NotificationActivity.class);
                startActivityForResult(intent1, 101);
                break;
            case R.id.imgFav:
                ImageView imgFav = (ImageView) view;
                int position = (int) view.getTag();
                Util.zoomButtonViewLikeFB(getActivity(), imgFav);
                imgFav.setImageResource(R.drawable.vessels_wishlist);
                callApiForIsFavourite(position);
                break;
            case R.id.imvFavourite:
                filterFavourite();
                break;
            case R.id.imvSearch:
                llSearch.setVisibility(View.VISIBLE);
                edtSearch.requestFocus();
                edtSearch.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtSearch, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.imvSearchBack:
                edtSearch.setText(null);
                llSearch.setVisibility(View.GONE);
                imvFavourite.setImageResource(R.drawable.wishlist);
                break;
            case R.id.imvSearchClose:
                edtSearch.setText(null);
                break;
        }

    }

    private void filterFavourite() {
        if (flag) {
            imvFavourite.setImageResource(R.drawable.heart_unselected);
            String isFav = "1" + "/" + "fav";
            vesselsRVAdapter.getFilter().filter(isFav);
            if (vesselsRVAdapter.getItemCount() == 0) {
                Util.showErrorSnackBar(coordinatorLayout, "No Record Found", getActivity());
            } else {
                Util.showSuccessSnackBar(coordinatorLayout, "Your Favourite Item", getActivity());
            }
            flag = !flag;
        } else {
            imvFavourite.setImageResource(R.drawable.wishlist);
            vesselsRVAdapter.getFilter().filter("");
            flag = !flag;
        }


    }

    private void callApiForIsFavourite(int position) {
        VesselData vesselData = vesselDataList.get(position);
        Util.showProDialog(getActivity());
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", "" + session.getUserData().getUserId());
        myHashMap.put("ship_id", vesselData.getId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<VesselResponse> call = apiService.add_ship_favourite(myHashMap);
        call.enqueue(new Callback<VesselResponse>() {
            @Override
            public void onResponse(Call<VesselResponse> call, retrofit2.Response<VesselResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    VesselResponse response1 = response.body();
                    if (response1.getResponse().getStatus() == 1) {
                        vesselDataList.get(position).setIsFavourite(response1.getResponse().getData().getIsFavourite());
                        vesselsRVAdapter.notifyItemChanged(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<VesselResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
                Util.dismissProDialog();
            }
        });


    }


    public void setAdapterDataList(List<VesselData> dataList) {
        Log.d("1234", "setAdapterDataList: ");
        Util.hideShimmer(shimmerFrameLayout);
        refreshLayout.setRefreshing(false);
        this.vesselDataList.addAll(dataList);
        if (vesselDataList.size() == 0) {
            rlVesselsView.setVisibility(View.GONE);
            txtNoDataFound.setVisibility(View.VISIBLE);
        } else {
            txtNoDataFound.setVisibility(View.GONE);
            rlVesselsView.setVisibility(View.VISIBLE);
        }
        vesselsRVAdapter.notifyDataSetChanged();
    }

    public void updateItem(VesselData data) {
        if (data != null) {
            rlVesselsView.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.GONE);
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            rlVesselsView.setVisibility(View.VISIBLE);
        }
        vesselsRVAdapter.updateItem(data);
    }

    public void addItem(VesselData data) {
        if (data != null) {
            rlVesselsView.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.GONE);
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            rlVesselsView.setVisibility(View.GONE);
        }
        vesselsRVAdapter.addItem(data);
    }

    public void get_Notification_list() {
        Log.d("1234", "get_Notification_list: ");
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
                            Log.d("1234", "onResponse: 1234 visible");
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
                    vesselDataList.clear();
                    callApiCallback.callServerApi(true);
                } else {
                    vesselDataList.clear();
                    callApiCallback.callServerApi(true);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            get_Notification_list();
        }
    }


}

