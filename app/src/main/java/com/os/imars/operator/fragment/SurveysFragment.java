package com.os.imars.operator.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.NotificationActivity;
import com.os.imars.dao.mySurvey.SurveyData;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.dao.mySurvey.SurveyUsersListResponse;
import com.os.imars.dao.mySurveyors.ActiveItem;
import com.os.imars.dao.mySurveyors.MySurveysResponse;
import com.os.imars.dao.mySurveyors.PastItem;
import com.os.imars.operator.adapter.PastSurveysRVAdapter;
import com.os.imars.operator.adapter.SurveysListAdapter;
import com.os.imars.operator.adapter.UpcomingSurveysRVAdapter;
import com.os.imars.operator.dao.notification.NotificationItem;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SurveysFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private TextView txtNoRequestfound, txtUpcomingHeader;
    private FrameLayout frame_layout;
    private Fragment selectedFragment = null;

    private String searchText = "";
    public static TextView txtActiveCount, txtPastCount, txtPendingPaymentCount, txtPaymentReceivedCount, txtPaidCount, txtCancelledCount, txtReportSubmittedCount, txtUpcomingCount, txtPendingCount;
    private ImageView imvNotifications, imvSearch, imvSearchBack, imvFilter, imvUser;
    private Session session;
    private int notificationCount = 0;
    List<NotificationItem> notificationItemList;
    private List<SurveyUserDataItem> userDataItemList;

    private Button btnSearch;

    List<ActiveItem> surveyActiveDataList = new ArrayList<ActiveItem>();
    List<PastItem> surveyPastDataList = new ArrayList<PastItem>();
    List<PastItem> pastDataList = new ArrayList<PastItem>();
    List<ActiveItem> activeDataList = new ArrayList<ActiveItem>();


    private RelativeLayout rlActiveSubTab, rlPastSubTab, rlnotification;
    private int selectedItemPosition = -1, selectedValue = -1;
    public EditText edtSearch;
    public boolean isSearchVisible = false;

    private TextView txtPast, txtNotificationCount, txtReportSubmitted, txtUpcoming, txtPending, txtPendingPayment, txtPaymentReceived, txtPaid, txtCancelled;
    public LinearLayout llActive, llPast, llSearch, llPendingPayment, llPaymentReceived, llPaid, llCancelled, llPending, llUpcoming, llReportSubmitted;

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operator_fragment_surveys, container, false);
        init(view);
        callSurveyListApi("", "", "");
        callOperatorApi();
        return view;
    }

    private void init(View view) {
        try {

            Util.showProDialog(getActivity());
            selectedFragment = new UpcomingFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment)
                    .commit();

            userDataItemList = new ArrayList<>();
            onClickListener = this;
            session = Session.getInstance(getActivity());
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
            llActive = (LinearLayout) view.findViewById(R.id.llActive);
            llPast = (LinearLayout) view.findViewById(R.id.llPast);
            txtActiveCount = (TextView) view.findViewById(R.id.txtActiveCount);
            txtPastCount = (TextView) view.findViewById(R.id.txtPastCount);
            txtUpcomingHeader = (TextView) view.findViewById(R.id.txtUpcomingHeader);
            txtPast = (TextView) view.findViewById(R.id.txtPast);
            imvNotifications = view.findViewById(R.id.imvNotifications);
            imvFilter = view.findViewById(R.id.imvFilter);
            imvSearch = view.findViewById(R.id.imvSearch);
            imvUser = view.findViewById(R.id.imvUser);
            imvSearchBack = view.findViewById(R.id.imvSearchBack);
            llSearch = view.findViewById(R.id.llSearch);
            edtSearch = view.findViewById(R.id.edtSearch);
            rlnotification = view.findViewById(R.id.rlnotification);
            txtNotificationCount = view.findViewById(R.id.txtNotificationCount);
            rlActiveSubTab = view.findViewById(R.id.rlActiveSubTab);
            rlPastSubTab = view.findViewById(R.id.rlPastSubTab);
            btnSearch = view.findViewById(R.id.btnSearch);

            txtPendingPayment = view.findViewById(R.id.txtPendingPayment);
            txtPaymentReceived = view.findViewById(R.id.txtPaymentReceived);
            txtPaid = view.findViewById(R.id.txtPaid);
            txtCancelled = view.findViewById(R.id.txtCancelled);

            txtReportSubmitted = view.findViewById(R.id.txtReportSubmitted);
            txtUpcoming = view.findViewById(R.id.txtUpcoming);
            txtPending = view.findViewById(R.id.txtPending);

            txtPendingPaymentCount = view.findViewById(R.id.txtPendingPaymentCount);
            txtPaidCount = view.findViewById(R.id.txtPaidCount);
            txtCancelledCount = view.findViewById(R.id.txtCancelledCount);
            txtReportSubmittedCount = view.findViewById(R.id.txtReportSubmittedCount);
            txtUpcomingCount = view.findViewById(R.id.txtUpcomingCount);
            txtPendingCount = view.findViewById(R.id.txtPendingCount);
            txtPaymentReceivedCount = view.findViewById(R.id.txtPaymentReceivedCount);

            llPendingPayment = view.findViewById(R.id.llPendingPayment);
            llPaymentReceived = view.findViewById(R.id.llPaymentReceived);
            llPaid = view.findViewById(R.id.llPaid);
            llCancelled = view.findViewById(R.id.llCancelled);
            llPending = view.findViewById(R.id.llPending);
            llUpcoming = view.findViewById(R.id.llUpcoming);
            llReportSubmitted = view.findViewById(R.id.llReportSubmitted);

            llActive.setOnClickListener(onClickListener);
            llPast.setOnClickListener(onClickListener);
            imvSearch.setOnClickListener(onClickListener);
            imvSearchBack.setOnClickListener(onClickListener);
            btnSearch.setOnClickListener(onClickListener);
            imvFilter.setOnClickListener(onClickListener);
            imvUser.setOnClickListener(onClickListener);
            rlnotification.setOnClickListener(onClickListener);

            llPendingPayment.setOnClickListener(onClickListener);
            llPaymentReceived.setOnClickListener(onClickListener);
            llPaid.setOnClickListener(onClickListener);
            llCancelled.setOnClickListener(onClickListener);
            llPending.setOnClickListener(onClickListener);
            llUpcoming.setOnClickListener(onClickListener);
            llReportSubmitted.setOnClickListener(onClickListener);

          /*  if (session.getUserData().getType().equals("0")) {
                imvUser.setVisibility(View.VISIBLE);
            } else {
                imvUser.setVisibility(View.GONE);
            }*/
            get_Notification_list();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callOperatorApi() {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<SurveyUsersListResponse> call = apiService.survey_user(hashMap);
        call.enqueue(new Callback<SurveyUsersListResponse>() {
            @Override
            public void onResponse(Call<SurveyUsersListResponse> call, Response<SurveyUsersListResponse> response) {
                if (response.body() != null) {
                    SurveyUsersListResponse listResponse = response.body();
                    userDataItemList.addAll(listResponse.getResponse().getData());
                }
                if (userDataItemList.size() > 0) {
                    SurveyUserDataItem userDataItem = new SurveyUserDataItem();
                    userDataItem.setName("All Operators");
                    userDataItemList.add(userDataItemList.size(), userDataItem);
                }
            }

            @Override
            public void onFailure(Call<SurveyUsersListResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llActive:
                manageTabs(0, true, false);
                break;
            case R.id.llPast:
                manageTabs(1, false, true);
                break;
            case R.id.rlnotification:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.imvSearch:
                llSearch.setVisibility(View.VISIBLE);
                isSearchVisible = true;
                break;
            case R.id.imvSearchBack:
                edtSearch.setText(null);
                llSearch.setVisibility(View.GONE);
                isSearchVisible = false;
                // txtActiveCount.setText("(" + surveyorDataList.size() + ")");
                break;
            case R.id.imvSearchClose:
                edtSearch.setText(null);
                // txtActiveCount.setText("(" + surveyorDataList.size() + ")");
                break;
            case R.id.imvFilter:
                //  filter();
                break;
            case R.id.imvUser:
                userFilter(userDataItemList);
                break;

            case R.id.llPending:
                manageSubActiveTabs(0, true, false, false);
                break;

            case R.id.llUpcoming:
                manageSubActiveTabs(0, false, true, false);
                break;

            case R.id.llReportSubmitted:
                manageSubActiveTabs(0, false, false, true);
                break;

            case R.id.llPendingPayment:
                manageSubPastTabs(0, true, false, false, false);
                break;

            case R.id.llPaid:
                manageSubPastTabs(0, false, false, true, false);
                break;

            case R.id.llCancelled:
                manageSubPastTabs(0, false, false, false, true);
                break;
            case R.id.btnSearch:
                searchText = edtSearch.getText().toString();
                callSurveyListApi("", "", searchText);
                break;


        }
    }


    private void userFilter(List<SurveyUserDataItem> userDataItemList) {

        final View view1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_recyclerview, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view1).create();
        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        TextView tvTitle = view1.findViewById(R.id.title);
        TextView txtNoRecordFound = view1.findViewById(R.id.txtNoRecordFound);
        tvTitle.setText("Select Operator");
        SurveysListAdapter surveysListAdapter = new SurveysListAdapter(getActivity(), R.layout.adapter_recyclerview, userDataItemList);
        lv.setAdapter(surveysListAdapter);

        if (userDataItemList.size() == 0) {
            txtNoRecordFound.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        } else {
            txtNoRecordFound.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }
        lv.setOnItemClickListener((parent, view, position, id) -> {
            dialog.cancel();
            String name = userDataItemList.get(position).getName();
            String operatorId = userDataItemList.get(position).getId();
            if (selectedFragment instanceof UpcomingFragment) {
                UpcomingSurveysRVAdapter upcomingSurveysRVAdapter = ((UpcomingFragment) selectedFragment).upcomingSurveysRVAdapter;
                if (upcomingSurveysRVAdapter != null) {
                    callSurveyListApi("", operatorId, "");
                }
            } else {
                PastSurveysRVAdapter pastSurveysRVAdapter = ((PastFragment) selectedFragment).pastSurveysRVAdapter;
                if (pastSurveysRVAdapter != null) {
                    callSurveyListApi("", operatorId, "");
                }
            }
        });

        dialog.show();
    }

    private void callSurveyListApi(String surveyor_id, String operator_id, String searchText) {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        hashMap.put("page", "");
        hashMap.put("status", "");
        hashMap.put("surveyor_id", surveyor_id);
        hashMap.put("operator_id", operator_id);
        hashMap.put("search", searchText);

        Util.showProDialog(getActivity());
        Call<MySurveysResponse> call = apiService.survey_all(hashMap);
        call.enqueue(new Callback<MySurveysResponse>() {
            @Override
            public void onResponse(Call<MySurveysResponse> call, Response<MySurveysResponse> response) {
                Util.dismissProDialog();
                surveyActiveDataList.clear();
                surveyPastDataList.clear();
                if (response.body() != null) {
                    MySurveysResponse listResponse = response.body();
                    surveyActiveDataList.addAll(listResponse.getResponse().getData().getActive());
                    int activeCount = 0;
                    if (surveyActiveDataList.size() > 0) {
                        for (ActiveItem activeItem : surveyActiveDataList) {
                            if (!activeItem.getStatus().contains("2")) {
                                activeCount++;
                            }
                        }
                        txtActiveCount.setText("(" + activeCount + ")");
                    }else{
                        activeCount = 0;
                        txtActiveCount.setText("(" + activeCount + ")");
                    }
                    surveyPastDataList.addAll(listResponse.getResponse().getData().getPast());
                    int count = 0;
                    if (surveyPastDataList.size() > 0) {

                        for (PastItem pastItem : surveyPastDataList) {
                            if (!pastItem.getStatus().contains("2")) {
                                count++;
                            }
                        }
                        txtPastCount.setText("(" + count + ")");
                    }else{
                        count = 0;
                        txtPastCount.setText("(" + count + ")");
                    }

                    manageTabs(0, true, false);
                }
            }

            @Override
            public void onFailure(Call<MySurveysResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
                Util.dismissProDialog();
            }
        });


    }



/*    public void updateItem(SurveyData surveyData){
        if (selectedFragment instanceof UpcomingFragment) {
            UpcomingSurveysRVAdapter upcomingSurveysRVAdapter = ((UpcomingFragment) selectedFragment).upcomingSurveysRVAdapter;
            if (upcomingSurveysRVAdapter != null) {
                upcomingSurveysRVAdapter.updateItem(surveyData);
            }
        }
    }*/

/*    private void filter() {
        String[] surveyStatus = null;
        session.setFilterStatus(selectedValue);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Survey Status");
        if (selectedFragment instanceof UpcomingFragment) {
            UpcomingFragment upcomingFragment = (UpcomingFragment) selectedFragment;
            surveyStatus = new String[]{"Pending", "Upcoming", "Cancelled", "Report Submitted"};
        } else {
            surveyStatus = new String[]{"Pending Payment", "Payment Received", "Paid"};
        }
        selectedItemPosition = session.getFilterStatus();
        String[] finalSurveyStatus = surveyStatus;
        builder.setSingleChoiceItems(surveyStatus, selectedItemPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (finalSurveyStatus.length == 3) {
                    selectedItemPosition = which + 4;
                } else {
                    selectedItemPosition = which;
                }

                selectedValue = which;
                session.setFilterStatus(selectedValue);
            }
        });
        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (selectedFragment instanceof UpcomingFragment) {
                    UpcomingSurveysRVAdapter upcomingSurveysRVAdapter = ((UpcomingFragment) selectedFragment).upcomingSurveysRVAdapter;
                    if (upcomingSurveysRVAdapter != null) {
                        if (selectedItemPosition != -1) {
                            String filterData = selectedItemPosition + "/" + "filter";
                            //   upcomingSurveysRVAdapter.getFilter().filter(filterData);
                        }
                    }
                } else {
                    if (selectedFragment instanceof PastFragment) {
                        PastSurveysRVAdapter pastSurveysRVAdapter = ((PastFragment) selectedFragment).pastSurveysRVAdapter;
                        if (pastSurveysRVAdapter != null) {
                            if (selectedItemPosition != -1) {
                                String filterData = selectedItemPosition + "/" + "filter";
                                pastSurveysRVAdapter.getFilter().filter(filterData);
                            }
                        }
                    }
                }
            }
        });
        builder.setNegativeButton("Clear", (dialog, which) -> {
            selectedValue = -1;
            session.setFilterStatus(selectedValue);
            if (selectedFragment instanceof UpcomingFragment) {
                UpcomingSurveysRVAdapter upcomingSurveysRVAdapter = ((UpcomingFragment) selectedFragment).upcomingSurveysRVAdapter;
                if (upcomingSurveysRVAdapter != null) {
                    //  upcomingSurveysRVAdapter.getFilter().filter("");
                }
            } else if (selectedFragment instanceof PastFragment) {
                PastSurveysRVAdapter pastSurveysRVAdapter = ((PastFragment) selectedFragment).pastSurveysRVAdapter;
                if (pastSurveysRVAdapter != null) {
                    if (selectedItemPosition != -1) {
                        pastSurveysRVAdapter.getFilter().filter("");
                    }
                }
            }

        });
        builder.show();


    }*/

    public void manageTabs(int position, boolean active, boolean past) {
        try {

            Bundle bundle = new Bundle();

            switch (position) {
                case 0:
                    activeDataList.clear();
                    for (ActiveItem row : surveyActiveDataList) {
                        if (row.getStatus().contains("0")) {
                            activeDataList.add(row);
                        }
                    }

                    Collections.sort(activeDataList, (o1, o2) -> {
                        if (o1.getStartDate() == null || o2.getStartDate() == null)
                            return 0;
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    });




                    selectedFragment = new UpcomingFragment();
                    bundle.putSerializable("ActiveSurveyData", (Serializable) activeDataList);
                    break;
                case 1:
                    pastDataList.clear();
                    for (PastItem row : surveyPastDataList) {
                        if (row.getStatus().contains("4")) {
                            pastDataList.add(row);
                        }
                    }

                    Collections.sort(pastDataList, (o1, o2) -> {
                        if (o1.getStartDate() == null || o2.getStartDate() == null)
                            return 0;
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    });


                    selectedFragment = new PastFragment();
                    bundle.putSerializable("pastSurveyData", (Serializable) pastDataList);
                    break;
            }


            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            selectedFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();


            if (active) {
                rlActiveSubTab.setVisibility(View.VISIBLE);
                rlPastSubTab.setVisibility(View.GONE);
                llPaymentReceived.setVisibility(View.GONE);
                llActive.setEnabled(false);
                txtUpcomingHeader.setTextColor(getResources().getColor(R.color.white));
                txtActiveCount.setTextColor(getResources().getColor(R.color.white));
                txtPastCount.setTextColor(getResources().getColor(R.color.baseColor));
                llActive.setBackgroundColor(getResources().getColor(R.color.baseColor));
                manageSubActiveTabs(0, true, false, false);

            } else {
                rlActiveSubTab.setVisibility(View.GONE);
                rlPastSubTab.setVisibility(View.VISIBLE);
                llPaymentReceived.setVisibility(View.GONE);
                llActive.setEnabled(true);
                txtUpcomingHeader.setTextColor(getResources().getColor(R.color.baseColor));
                txtActiveCount.setTextColor(getResources().getColor(R.color.baseColor));
                llActive.setBackgroundColor(getResources().getColor(R.color.white));

                llPending.setEnabled(true);
                llPending.setBackgroundColor(getResources().getColor(R.color.white));
                txtPending.setTextColor(getResources().getColor(R.color.baseColor));
                txtPendingCount.setTextColor(getResources().getColor(R.color.baseColor));

            }

            if (past) {
                rlActiveSubTab.setVisibility(View.GONE);
                rlPastSubTab.setVisibility(View.VISIBLE);
                llPaymentReceived.setVisibility(View.GONE);
                llPast.setEnabled(false);
                txtPast.setTextColor(getResources().getColor(R.color.white));
                txtPastCount.setTextColor(getResources().getColor(R.color.white));
                llPast.setBackgroundColor(getResources().getColor(R.color.baseColor));

                manageSubPastTabs(0, true, false, false, false);


            } else {
                rlActiveSubTab.setVisibility(View.VISIBLE);
                rlPastSubTab.setVisibility(View.GONE);
                llPaymentReceived.setVisibility(View.GONE);
                llPast.setEnabled(true);
                txtPastCount.setTextColor(getResources().getColor(R.color.baseColor));
                txtPast.setTextColor(getResources().getColor(R.color.baseColor));
                llPast.setBackgroundColor(getResources().getColor(R.color.white));

                llPendingPayment.setEnabled(true);
                llPendingPayment.setBackgroundColor(getResources().getColor(R.color.white));
                txtPendingPayment.setTextColor(getResources().getColor(R.color.baseColor));
                txtPendingPayment.setTextColor(getResources().getColor(R.color.baseColor));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void manageSubActiveTabs(int position, boolean pending, boolean upcoming, boolean reportSubmitted) {

        List<ActiveItem> activeDataList = new ArrayList<ActiveItem>();

        try {
            Bundle bundle = new Bundle();

            int upcomingCount = 0;
            int reportSubmittedCount = 0;

            if (pending) {
                for (ActiveItem row : surveyActiveDataList) {
                    if (row.getStatus().contains("0")) {

                        activeDataList.add(row);
                    }
                    if (row.getStatus().contains("1")) {
                        upcomingCount++;
                    }
                    if (row.getStatus().contains("3")) {
                        reportSubmittedCount++;
                    }

                }

                Collections.sort(activeDataList, (o1, o2) -> {
                    if (o1.getStartDate() == null || o2.getStartDate() == null)
                        return 0;
                    return o1.getStartDate().compareTo(o2.getStartDate());
                });


                txtPendingCount.setText("(" + activeDataList.size() + ")");
                txtUpcomingCount.setText("(" + upcomingCount + ")");
                txtReportSubmittedCount.setText("(" + reportSubmittedCount + ")");

                selectedFragment = new UpcomingFragment();
                bundle.putSerializable("ActiveSurveyData", (Serializable) activeDataList);

            } else if (upcoming) {
                for (ActiveItem row : surveyActiveDataList) {
                    if (row.getStatus().contains("1")) {
                        activeDataList.add(row);
                    }
                }

                Collections.sort(activeDataList, (o1, o2) -> {
                    if (o1.getStartDate() == null || o2.getStartDate() == null)
                        return 0;
                    return o1.getStartDate().compareTo(o2.getStartDate());
                });


                txtUpcomingCount.setText("(" + activeDataList.size() + ")");

                selectedFragment = new UpcomingFragment();
                bundle.putSerializable("ActiveSurveyData", (Serializable) activeDataList);

            } else {
                for (ActiveItem row : surveyActiveDataList) {
                    if (row.getStatus().contains("3")) {
                        activeDataList.add(row);
                    }
                }

                txtReportSubmittedCount.setText("(" + activeDataList.size() + ")");
                selectedFragment = new UpcomingFragment();
                bundle.putSerializable("ActiveSurveyData", (Serializable) activeDataList);
            }

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            selectedFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();


            if (pending) {
                llPending.setEnabled(false);
                llPending.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtPending.setTextColor(getResources().getColor(R.color.white));
                txtPendingCount.setTextColor(getResources().getColor(R.color.white));

            } else {
                llPending.setEnabled(true);
                llPending.setBackgroundColor(getResources().getColor(R.color.white));
                txtPending.setTextColor(getResources().getColor(R.color.baseColor));
                txtPendingCount.setTextColor(getResources().getColor(R.color.baseColor));
            }


            if (upcoming) {
                llUpcoming.setEnabled(false);
                llUpcoming.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtUpcoming.setTextColor(getResources().getColor(R.color.white));
                txtUpcomingCount.setTextColor(getResources().getColor(R.color.white));

            } else {
                llUpcoming.setEnabled(true);
                llUpcoming.setBackgroundColor(getResources().getColor(R.color.white));
                txtUpcoming.setTextColor(getResources().getColor(R.color.baseColor));
                txtUpcomingCount.setTextColor(getResources().getColor(R.color.baseColor));
            }

            if (reportSubmitted) {
                llReportSubmitted.setEnabled(false);
                llReportSubmitted.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtReportSubmitted.setTextColor(getResources().getColor(R.color.white));
                txtReportSubmittedCount.setTextColor(getResources().getColor(R.color.white));

            } else {
                llReportSubmitted.setEnabled(true);
                llReportSubmitted.setBackgroundColor(getResources().getColor(R.color.white));
                txtReportSubmitted.setTextColor(getResources().getColor(R.color.baseColor));
                txtReportSubmittedCount.setTextColor(getResources().getColor(R.color.baseColor));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void manageSubPastTabs(int position, boolean unPaid, boolean paymentReceived, boolean paid, boolean cancelled) {

        try {
            List<PastItem> pastDataList = new ArrayList<PastItem>();
            int paymentReceivedCount = 0;
            int paidCount = 0;
            int cancelledCount = 0;

            Bundle bundle = new Bundle();

            if (unPaid) {
                pastDataList.clear();
                for (PastItem row : surveyPastDataList) {
                    if (row.getStatus().contains("4")) {
                        pastDataList.add(row);
                    }
                    if (row.getStatus().contains("5") || row.getStatus().contains("6")) {
                        paidCount++;
                    }

                    if (row.getStatus().contains("2")) {
                        cancelledCount++;
                    }
                }


                Collections.sort(pastDataList, (o1, o2) -> {
                    if (o1.getStartDate() == null || o2.getStartDate() == null)
                        return 0;
                    return o1.getStartDate().compareTo(o2.getStartDate());
                });



                txtPendingPaymentCount.setText("(" + pastDataList.size() + ")");
                txtPaidCount.setText("(" + paidCount + ")");
                txtCancelledCount.setText("(" + cancelledCount + ")");

                selectedFragment = new PastFragment();
                bundle.putSerializable("pastSurveyData", (Serializable) pastDataList);

            } else if (paid) {
                pastDataList.clear();
                for (PastItem row : surveyPastDataList) {
                    if (row.getStatus().contains("6") || row.getStatus().contains("5")) {
                        pastDataList.add(row);
                    }
                }
                txtPaidCount.setText("(" + pastDataList.size() + ")");
                selectedFragment = new PastFragment();
                bundle.putSerializable("pastSurveyData", (Serializable) pastDataList);
            } else {
                for (PastItem row : surveyPastDataList) {
                    if (row.getStatus().contains("2")) {
                        pastDataList.add(row);
                    }
                }
                txtCancelledCount.setText("(" + pastDataList.size() + ")");
                selectedFragment = new PastFragment();
                bundle.putSerializable("pastSurveyData", (Serializable) pastDataList);
            }


            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            selectedFragment.setArguments(bundle);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();


            if (unPaid) {
                txtPendingPayment.setText("Unpaid ");
                llPendingPayment.setEnabled(false);
                llPendingPayment.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtPendingPayment.setTextColor(getResources().getColor(R.color.white));

                txtPendingPaymentCount.setTextColor(getResources().getColor(R.color.white));

            } else {
                txtPendingPayment.setText("Unpaid ");
                llPendingPayment.setEnabled(true);
                llPendingPayment.setBackgroundColor(getResources().getColor(R.color.white));
                txtPendingPayment.setTextColor(getResources().getColor(R.color.baseColor));
                txtPendingPaymentCount.setTextColor(getResources().getColor(R.color.baseColor));
            }

            if (paid) {
                llPaid.setEnabled(false);
                llPaid.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtPaid.setTextColor(getResources().getColor(R.color.white));
                txtPaidCount.setTextColor(getResources().getColor(R.color.white));
            } else {
                llPaid.setEnabled(true);
                llPaid.setBackgroundColor(getResources().getColor(R.color.white));
                txtPaid.setTextColor(getResources().getColor(R.color.baseColor));
                txtPaidCount.setTextColor(getResources().getColor(R.color.baseColor));
            }

            if (cancelled) {
                llCancelled.setEnabled(false);
                llCancelled.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtCancelled.setTextColor(getResources().getColor(R.color.white));
                txtCancelledCount.setTextColor(getResources().getColor(R.color.white));
            } else {
                llCancelled.setEnabled(true);
                llCancelled.setBackgroundColor(getResources().getColor(R.color.white));
                txtCancelled.setTextColor(getResources().getColor(R.color.baseColor));
                txtCancelledCount.setTextColor(getResources().getColor(R.color.baseColor));
            }


        } catch (Exception e) {
            e.printStackTrace();
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



    public void updateItem(SurveyData responseData) {

        if (selectedFragment instanceof UpcomingFragment) {
            UpcomingSurveysRVAdapter upcomingSurveysRVAdapter = ((UpcomingFragment) selectedFragment).upcomingSurveysRVAdapter;
            if (upcomingSurveysRVAdapter != null) {
                // upcomingSurveysRVAdapter.updateItem(responseData);
            }
        }
    }

}

