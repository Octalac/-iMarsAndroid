package com.os.imars.operator.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.os.imars.operator.activity.FinanceActivity;
import com.os.imars.operator.activity.DPMyProfileActivity;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.activity.OperatorListActvitity;
import com.os.imars.operator.activity.ReportAnIssueActivity;
import com.os.imars.operator.activity.UnderDPMyProfileActivity;
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
    private RelativeLayout llOperators;
    private RelativeLayout llReportAnIssue;
    private RelativeLayout llLogout, llFinance;
    private Session session;
    private View viewLine, viewLine1;

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_operator, container, false);
        session = Session.getInstance(getActivity());
        init(view);
        return view;
    }

    private void init(View view) {
        try {
            onClickListener = this;
            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            llSurveys = (RelativeLayout) view.findViewById(R.id.llSurveys);
            llAppoint = (RelativeLayout) view.findViewById(R.id.llAppoint);
            llVessels = (RelativeLayout) view.findViewById(R.id.llVessels);
            llAgents = (RelativeLayout) view.findViewById(R.id.llAgents);
            llAccount = (RelativeLayout) view.findViewById(R.id.llAccount);
            llFinance = (RelativeLayout) view.findViewById(R.id.llFinance);
            viewLine = view.findViewById(R.id.viewLine);
            viewLine1 = view.findViewById(R.id.viewLine1);
            llOperators = (RelativeLayout) view.findViewById(R.id.llOperators);
            llReportAnIssue = (RelativeLayout) view.findViewById(R.id.llReportAnIssue);
            llLogout = (RelativeLayout) view.findViewById(R.id.llLogout);
            llSurveys.setOnClickListener(onClickListener);
            llAppoint.setOnClickListener(onClickListener);
            llVessels.setOnClickListener(onClickListener);
            llAgents.setOnClickListener(onClickListener);
            llAccount.setOnClickListener(onClickListener);
            llOperators.setOnClickListener(onClickListener);
            llFinance.setOnClickListener(onClickListener);
            llReportAnIssue.setOnClickListener(onClickListener);
            llLogout.setOnClickListener(onClickListener);
            if (!session.getUserData().getType().equals("1")) {
                llOperators.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSurveys:
                ((OperatorHomeActivity) Env.currentActivity).manageTabs(0, true, false, false, false, false);
                break;
            case R.id.llAppoint:
                ((OperatorHomeActivity) Env.currentActivity).manageTabs(1, false, true, false, false, false);
                break;
            case R.id.llVessels:
                ((OperatorHomeActivity) Env.currentActivity).manageTabs(2, false, false, true, false, false);
                break;
            case R.id.llAgents:
                ((OperatorHomeActivity) Env.currentActivity).manageTabs(3, false, false, false, true, false);
                break;
            case R.id.llAccount:
                if (session.getUserData().getType().equals("0")) {
                    Intent intent = new Intent(Env.currentActivity, DPMyProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Env.currentActivity, UnderDPMyProfileActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.llOperators:
                Intent intent1 = new Intent(Env.currentActivity, OperatorListActvitity.class);
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

    private void emailComposer() {
     /*   Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = {"octal.team11@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Report an issue");
        intent.setType("text/html");
        startActivity(Intent.createChooser(intent, "Send mail"));*/
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:octal.team11@gmail.com"));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
        }
    }

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

