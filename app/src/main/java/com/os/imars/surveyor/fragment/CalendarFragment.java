package com.os.imars.surveyor.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.NotificationActivity;
import com.os.imars.custom.CustomCalendarView;
import com.os.imars.custom.CustomSliderButton;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.dao.mySurvey.SurveyUsersListResponse;
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.adapter.SurveysListAdapter;
import com.os.imars.operator.dao.notification.NotificationItem;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.surveyor.dao.CalendarDataItem;
import com.os.imars.surveyor.dao.CalenderDao;
import com.os.imars.surveyor.dao.LoadAvailabilityResponse;
import com.os.imars.surveyor.dao.UserAvailableResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CalendarFragment extends BaseFragment {
    private CallApiCallback callApiCallback;
    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRequestfound, txtSlideOff, txtNotificationCount;
    private CustomSliderButton btnCalendar;
    private CustomCalendarView customCalendarView;
    private ImageView imvFilter;
    private Session session;
    private int notificationCount = 0;
    List<NotificationItem> notificationItemList;
    List<SurveyUserDataItem> userDataItemList;
    private RelativeLayout rlTab, rlnotification;
    private List<CalendarDataItem> calendarDataItemList;
    private boolean lockFlag = true, unlockFlag = true;
    private int isAvalibleInCalendarStatus = 0;
    private String userFilterId = "";


    @Override

    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        session = Session.getInstance(getActivity());
        init(view);
        callSurveyAccordingCalendarApi(session.getUserData().getUserId(), true);
        get_Notification_list();
        callSurveyorApi();
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
            Util.showProDialog(getActivity());
            onClickListener = this;
            calendarDataItemList = new ArrayList<>();
            userDataItemList = new ArrayList<>();
            txtNoRequestfound = (TextView) view.findViewById(R.id.txtNoDataFound);
            txtSlideOff = (TextView) view.findViewById(R.id.txtSlideOff);
            btnCalendar = (CustomSliderButton) view.findViewById(R.id.btnCalendar);
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            customCalendarView = (CustomCalendarView) view.findViewById(R.id.customCalendarView);
            txtNotificationCount = (TextView) view.findViewById(R.id.txtNotificationCount);
            imvFilter = view.findViewById(R.id.imvFilter);
            rlnotification = view.findViewById(R.id.rlnotification);
            rlnotification.setOnClickListener(onClickListener);
            imvFilter.setOnClickListener(onClickListener);
            if (session.getUserData().getType().equals("2"))
                imvFilter.setVisibility(View.VISIBLE);
            else {
                imvFilter.setVisibility(View.GONE);
            }
            if (userFilterId.equals("")) {
                userFilterId = session.getUserData().getUserId();
            }
            btnCalendar.setOnUnlockListener(() -> {
                txtSlideOff.setVisibility(View.VISIBLE);
                Log.d("1234", "init:unlock ");
                callApi();
            });


            btnCalendar.setOnLockListener(() -> {
                txtSlideOff.setVisibility(View.VISIBLE);
                Log.d("1234", "init:lock ");
                callApi();
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void callApi() {
        Util.showProDialog(getActivity());
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<UserAvailableResponse> call = apiService.user_setting(myHashMap);
        call.enqueue(new Callback<UserAvailableResponse>() {
            @Override
            public void onResponse(Call<UserAvailableResponse> call, Response<UserAvailableResponse> response) {
                Util.dismissProDialog();
                Log.d("1234", "onResponse: 1" + response.body().getResponse().getData().getStatus());
                if (response.body() != null) {
                    UserAvailableResponse userAvailableResponse = response.body();
                    if (userAvailableResponse.getResponse().getStatus() == 1) {
                        Log.d("1234", "onResponse: 12 ");

                        if (userAvailableResponse.getResponse().getData().getStatus().equals("1")) {
                            isAvalibleInCalendarStatus = 4;
                            session.setCalendarAvailabilityStatus(String.valueOf(isAvalibleInCalendarStatus));
                        } else {
                            isAvalibleInCalendarStatus = 3;
                            session.setCalendarAvailabilityStatus(String.valueOf(isAvalibleInCalendarStatus));
                        }
                        customCalendarView.changeStatusColor(isAvalibleInCalendarStatus);

                        if (userAvailableResponse.getResponse().getData().getStatus().equals("1")) {
                            Toast.makeText(getActivity(), "You are now back online and are listed in operators’ search results on your available days.", Toast.LENGTH_SHORT).show();
                            btnCalendar.setStuBackground(R.drawable.bg_round_grey_shape);
                            txtSlideOff.setText("Slide to OFF");
                        } else {
                            btnCalendar.setStuBackground(R.drawable.bg_round_green_fill_color);
                            Toast.makeText(getActivity(), "You are offline and will not be be listed in operators’ search results. Use “Slide to On” slider to become online again.", Toast.LENGTH_SHORT).show();
                            txtSlideOff.setText("Slide to ON");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<UserAvailableResponse> call, Throwable t) {
                Util.dismissProDialog();

                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlnotification:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.event_wrapper:
                Log.d("1234", "onFailure: " + isAvalibleInCalendarStatus);
                if (isAvalibleInCalendarStatus == 1 || isAvalibleInCalendarStatus == 4) {
                    Log.d("1234", "onFailure:1 " + userFilterId);
                    Log.d("1234", "onFailure:2 " + session.getUserData().getUserId());
                    if (userFilterId.equals(session.getUserData().getUserId())) {
                        Log.d("1234", "onFailure:3 " + session.getUserData().getUserId());
                        CalenderDao calenderDao = (CalenderDao) view.getTag();
                        if (calenderDao.getSurveyType() == null) {
                            apiLoadAvailability(calenderDao);
                        } else if ((calenderDao.getSurveyType().equals("0") || calenderDao.getSurveyType().equals("1"))) {
                            apiLoadAvailability(calenderDao);
                        }
                    }
                }
                break;
            case R.id.imvFilter:
                userFilter();
                break;

        }
    }

    private void userFilter() {
        final View view1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_recyclerview, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view1).create();
        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        TextView tvTitle = view1.findViewById(R.id.title);
        tvTitle.setText("Select Surveyor");
        SurveysListAdapter surveysListAdapter = new SurveysListAdapter(getActivity(), R.layout.adapter_recyclerview, userDataItemList);
        lv.setAdapter(surveysListAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            userFilterId = userDataItemList.get(position).getId();
            dialog.cancel();
            if (session.getUserData().getUserId().equals(userFilterId)) {
                callSurveyAccordingCalendarApi(session.getUserData().getUserId(), true);
                Log.d("1234","data:"+session.getUserData().getUserId());
            } else if (userDataItemList.get(position).getName().equals("All Surveyors")) {
                Log.d("1234","data:"+session.getUserData().getUserId());
                callSurveyAccordingCalendarApi(session.getUserData().getUserId(), false);
            } else {
                callSurveyAccordingCalendarApi(userFilterId, true);
            }

        });

        dialog.show();
    }

    private void callSurveyorApi() {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<SurveyUsersListResponse> call = apiService.assign_surveyor_list(hashMap);
        call.enqueue(new Callback<SurveyUsersListResponse>() {
            @Override
            public void onResponse(Call<SurveyUsersListResponse> call, Response<SurveyUsersListResponse> response) {
                if (response.body() != null) {
                    SurveyUsersListResponse listResponse = response.body();
                    userDataItemList.addAll(listResponse.getResponse().getData());
                }
                if (userDataItemList.size() > 0) {
                    SurveyUserDataItem userDataItem = new SurveyUserDataItem();
                    userDataItem.setName("All Surveyors");
                    userDataItemList.add(userDataItemList.size(), userDataItem);
                }
            }

            @Override
            public void onFailure(Call<SurveyUsersListResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }

    private void apiLoadAvailability(CalenderDao calenderDao) {

        @SuppressLint("SimpleDateFormat")
        String dateInRange = new SimpleDateFormat("yyyy-MM-dd").format(calenderDao.getDate());
        String status = calenderDao.getSurveyType() == null ? "0" : (calenderDao.getSurveyType().equals("1") ? "0" : "1");
        Util.showProDialog(getActivity());
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        myHashMap.put("start", dateInRange);
        myHashMap.put("end", dateInRange);
        myHashMap.put("status", status);
        RxService apiService = App.getClient().create(RxService.class);
        Call<LoadAvailabilityResponse> call = apiService.change_availability(myHashMap);
        call.enqueue(new Callback<LoadAvailabilityResponse>() {
            @Override
            public void onResponse(Call<LoadAvailabilityResponse> call, Response<LoadAvailabilityResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    LoadAvailabilityResponse calendarAvailabilityResponse = response.body();
                    Log.d("1234", "onResponse: " + calendarAvailabilityResponse.getResponse().getData());
                    if (calendarAvailabilityResponse.getResponse().getStatus() == 1) {
                        customCalendarView.calendarDataItemList.clear();
                        if (customCalendarView.mAdapter != null) {
                            Log.d("1234", "onResponse:1 ");
                            String surveyStatus = calendarAvailabilityResponse.getResponse().getData().get(0).getStatus();
                            customCalendarView.dayValueInCells.get(calenderDao.getPosition()).setSurveyType(surveyStatus);
                            String date = calendarAvailabilityResponse.getResponse().getData().get(0).getStart();
                            if (!findItemInList(date)) {
                                Log.d("1234", "onResponse: 2" + date);
                                customCalendarView.calendarDataItemList.add(calendarAvailabilityResponse.getResponse().getData().get(0));
                                customCalendarView.changeStatusColor(1);
                            } else {
                                Log.d("1234", "onResponse: 2 ");
                                customCalendarView.calendarDataItemList.set(calenderDao.getPosition(), calendarAvailabilityResponse.getResponse().getData().get(0));
                                customCalendarView.changeStatusColor(1);

                            }
                        }
                    }
                }
            }

            private boolean findItemInList(String itemToFind) {
                for (int i = 0; i < customCalendarView.calendarDataItemList.size(); i++) {
                    if (customCalendarView.calendarDataItemList.get(i).getStart().equals(itemToFind)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onFailure(Call<LoadAvailabilityResponse> call, Throwable t) {
                Util.dismissProDialog();
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });
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
                            notificationCount = notificationResponse.getResponse().getData().getUnreadNotificationCount();
                            notificationItemList = notificationResponse.getResponse().getData().getNotification();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            get_Notification_list();
        }
    }


    private void callSurveyAccordingCalendarApi(String userId, boolean isFilter) {
        Util.showProDialog(getActivity());
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", userId);
        myHashMap.put("filter", isFilter);
        Log.d("1234","data:"+ Arrays.asList(myHashMap));
        RxService apiService = App.getClient().create(RxService.class);
        Call<LoadAvailabilityResponse> call = apiService.event_load(myHashMap);
        call.enqueue(new Callback<LoadAvailabilityResponse>() {
            @Override
            public void onResponse(Call<LoadAvailabilityResponse> call, Response<LoadAvailabilityResponse> response) {
                Util.dismissProDialog();

                calendarDataItemList.clear();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus() == 1) {
                        int status = Integer.parseInt(response.body().getResponse().getIs_avail());
                        isAvalibleInCalendarStatus = status;
                        session.setCalendarAvailabilityStatus(response.body().getResponse().getIs_avail());

                        if (response.body().getResponse().getIs_avail().equals("4")) {
                            btnCalendar.setStuBackground(R.drawable.bg_round_grey_shape);
                            txtSlideOff.setText("Slide to OFF");
                        } else {
                            btnCalendar.setStuBackground(R.drawable.bg_round_green_fill_color);
                            txtSlideOff.setText("Slide to ON");
                        }

                        calendarDataItemList.addAll(response.body().getResponse().getData());
                    }
                    customCalendarView.loadAdapterDataSet(calendarDataItemList, onClickListener);

                }
            }

            @Override
            public void onFailure(Call<LoadAvailabilityResponse> call, Throwable t) {
                Util.dismissProDialog();

            }
        });
    }


}

