package com.os.imars.surveyor.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.LoginActivity;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.surveyor.activity.FinanceActivity;
import com.os.imars.surveyor.activity.IndividualSurveyorProfileActivity;
import com.os.imars.surveyor.activity.MyProfileActivity;
import com.os.imars.surveyor.activity.ReportAnIssueActivity;
import com.os.imars.surveyor.activity.SurveyorHomeActivity;
import com.os.imars.surveyor.activity.SurveyorListActvitity;
import com.os.imars.surveyor.activity.UnderDPSurveyorMyProfileActivity;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;
import com.os.imars.views.BaseView.Env;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreFragment extends BaseFragment {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout llSurveys;
    private RelativeLayout llAppoint;
    private RelativeLayout llVessels;
    private RelativeLayout llAgents;
    private RelativeLayout llAccount;
    private RelativeLayout llSurveyor;
    private RelativeLayout llReportAnIssue;
    private RelativeLayout llLogout;
    private RelativeLayout llFinance;
    private Session session;
    private View viewLine, viewLine1, viewAppoint, viewPorts;


    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        try {
            session = Session.getInstance(getActivity());
            onClickListener = this;
            Log.d("1234", "init: " + session.getUserData().getType());
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            llSurveys = (RelativeLayout) view.findViewById(R.id.llSurveys);
            llAppoint = (RelativeLayout) view.findViewById(R.id.llAppoint);
            llVessels = (RelativeLayout) view.findViewById(R.id.llVessels);
            llAgents = (RelativeLayout) view.findViewById(R.id.llAgents);
            llAccount = (RelativeLayout) view.findViewById(R.id.llAccount);
            llSurveyor = (RelativeLayout) view.findViewById(R.id.llSurveyor);
            llFinance = (RelativeLayout) view.findViewById(R.id.llFinance);
            viewLine = view.findViewById(R.id.viewLine);
            viewLine1 = view.findViewById(R.id.viewLine1);
            viewAppoint = view.findViewById(R.id.viewAppoint);
            viewPorts = view.findViewById(R.id.viewPorts);
            llReportAnIssue = view.findViewById(R.id.llReportAnIssue);
            llLogout = view.findViewById(R.id.llLogout);
            llSurveys.setOnClickListener(onClickListener);
            llAppoint.setOnClickListener(onClickListener);
            llVessels.setOnClickListener(onClickListener);
            llAgents.setOnClickListener(onClickListener);
            llAccount.setOnClickListener(onClickListener);
            llFinance.setOnClickListener(onClickListener);
            llSurveyor.setOnClickListener(onClickListener);
            llReportAnIssue.setOnClickListener(onClickListener);
            llLogout.setOnClickListener(onClickListener);
            if (session.getUserData().getType().equals("2")) {
                llSurveyor.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
                llFinance.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.VISIBLE);
                viewPorts.setVisibility(View.VISIBLE);
                viewAppoint.setVisibility(View.VISIBLE);
                llAppoint.setVisibility(View.VISIBLE);
                llVessels.setVisibility(View.VISIBLE);
            } else if (session.getUserData().getType().equals("3")) {
                llFinance.setVisibility(View.GONE);
                viewLine1.setVisibility(View.GONE);
                llSurveyor.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                viewPorts.setVisibility(View.GONE);
                viewAppoint.setVisibility(View.GONE);
                llAppoint.setVisibility(View.GONE);
                llVessels.setVisibility(View.GONE);

            } else {
                llSurveyor.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                llFinance.setVisibility(View.VISIBLE);
                viewLine1.setVisibility(View.VISIBLE);
                viewPorts.setVisibility(View.VISIBLE);
                viewAppoint.setVisibility(View.VISIBLE);
                llAppoint.setVisibility(View.VISIBLE);
                llVessels.setVisibility(View.VISIBLE);
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSurveys:
                ((SurveyorHomeActivity) Env.currentActivity).manageTabs(0, true, false, false, false, false);
                break;
            case R.id.llAppoint:
                ((SurveyorHomeActivity) Env.currentActivity).manageTabs(1, false, true, false, false, false);
                break;
            case R.id.llVessels:
                ((SurveyorHomeActivity) Env.currentActivity).manageTabs(2, false, false, true, false, false);
                break;
            case R.id.llAgents:
                ((SurveyorHomeActivity) Env.currentActivity).manageTabs(3, false, false, false, true, false);
                break;
            case R.id.llAccount:
                if (session.getUserData().getType().equals("4")) {
                    Intent intent = new Intent(Env.currentActivity, IndividualSurveyorProfileActivity.class);
                    startActivity(intent);
                } else if (session.getUserData().getType().equals("2")) {
                    Intent intent = new Intent(Env.currentActivity, MyProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Env.currentActivity, UnderDPSurveyorMyProfileActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.llSurveyor:
                Intent intent1 = new Intent(Env.currentActivity, SurveyorListActvitity.class);
                startActivity(intent1);
                break;
            case R.id.llFinance:
                Intent intent2 = new Intent(Env.currentActivity, FinanceActivity.class);
                startActivity(intent2);
                break;
            case R.id.llReportAnIssue:
                Intent intent3 = new Intent(Env.currentActivity, ReportAnIssueActivity.class);
                startActivity(intent3);
                break;
            case R.id.llLogout:
                signOut();
                break;
        }
    }

/*
    private void emailComposer() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:octal.team11@gmail.com"));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
        }
    }
*/

    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiForOutUser();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private void callApiForOutUser() {
        Util.showProDialog(getActivity());
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<CommonResponse> call = apiService.logout(myHashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null) {
                    CommonResponse listResponse = response.body();
                    if (listResponse != null) {
                        if (listResponse.getResponse().getStatus() == 1) {
                            Util.dismissProDialog();
                            session.setLogout();
                            Intent intent1 = new Intent(Env.currentActivity, LoginActivity.class);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            getActivity().finish();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {

            }
        });
    }

}

