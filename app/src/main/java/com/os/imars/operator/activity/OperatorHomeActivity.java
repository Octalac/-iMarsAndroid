package com.os.imars.operator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.mySurvey.SurveyData;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.appoint.AppointData;
import com.os.imars.operator.dao.appoint.SurveyTypeResponse;
import com.os.imars.operator.dao.notification.NotificationItem;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.operator.dao.vessel.VesselListResponse;
import com.os.imars.operator.fragment.AgentsFragment;
import com.os.imars.operator.fragment.AppointFragment;
import com.os.imars.operator.fragment.MoreFragment;
import com.os.imars.operator.fragment.SurveysFragment;
import com.os.imars.operator.fragment.VesselsFragment;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorHomeActivity extends BaseActivity implements CallApiCallback {

    private CallApiCallback callApiCallback;
    private FrameLayout frame_layout;
    private AppBarLayout appBar;
    private Toolbar toolbar;
    private LinearLayout llBottom;
    private Fragment selectedFragment = null;
    private ImageView imvNotifications;
    private TextView toolbarTitle;
    private ImageView imvSearch;
    private ImageView imvSurveys;
    private TextView txtSurveys;
    private LinearLayout llSurveys;
    private ImageView imvAppoint;
    private TextView txtAppoint;
    private LinearLayout llAppoint;
    private ImageView imvVessels;
    private TextView txtVessels;
    private RelativeLayout rlNotification;
    private RelativeLayout llVessels;
    private ImageView imvAgents;
    private TextView txtAgents;
    private LinearLayout llAgents;
    private ImageView imvMore;
    private TextView txtMore;
    private LinearLayout llMore;
    private Session session;
    private List<AgentData> agentDataList;
    private List<VesselData> vesselDataList;
    private AppointData appointData;
    private int notificationCount = 0;
    private List<NotificationItem> notificationItemList;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_home);
        initView();
    }


    private void initView() {
        try {
            Log.d("1234", "initView: ");
            session = Session.getInstance(this);
            agentDataList = new ArrayList<>();
            vesselDataList = new ArrayList<>();
            notificationItemList = new ArrayList<>();
            selectedFragment = new SurveysFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new SurveysFragment())
                    .commit();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            llBottom = (LinearLayout) findViewById(R.id.llBottom);
            setSupportActionBar(toolbar);
            appBar = (AppBarLayout) findViewById(R.id.appBar);
            coordinatorLayout = findViewById(R.id.coordinatorLayout);
            frame_layout = (FrameLayout) findViewById(R.id.frame_layout);
            imvNotifications = (ImageView) findViewById(R.id.imvNotifications);
            toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
            imvSearch = (ImageView) findViewById(R.id.imvSearch);
            imvSurveys = (ImageView) findViewById(R.id.imvSurveys);
            txtSurveys = (TextView) findViewById(R.id.txtSurveys);
            llSurveys = (LinearLayout) findViewById(R.id.llSurveys);
            imvAppoint = (ImageView) findViewById(R.id.imvAppoint);
            txtAppoint = (TextView) findViewById(R.id.txtAppoint);
            llAppoint = (LinearLayout) findViewById(R.id.llAppoint);
            imvVessels = (ImageView) findViewById(R.id.imvVessels);
            txtVessels = (TextView) findViewById(R.id.txtVessels);
            rlNotification = (RelativeLayout) findViewById(R.id.rlNotification);
            llVessels = (RelativeLayout) findViewById(R.id.llVessels);
            imvAgents = (ImageView) findViewById(R.id.imvAgents);
            txtAgents = (TextView) findViewById(R.id.txtAgents);
            llAgents = (LinearLayout) findViewById(R.id.llAgents);
            imvMore = (ImageView) findViewById(R.id.imvMore);
            txtMore = (TextView) findViewById(R.id.txtMore);
            llMore = (LinearLayout) findViewById(R.id.llMore);
            // get_Notification_list();
            if (Util.hasNavBar(Env.currentActivity)) {
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                params1.addRule(RelativeLayout.BELOW, appBar.getId());
                params1.addRule(RelativeLayout.ABOVE, llBottom.getId());
                frame_layout.setLayoutParams(params1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        imvNotifications.setOnClickListener(this);
        imvSearch.setOnClickListener(this);
        llSurveys.setOnClickListener(this);
        llAppoint.setOnClickListener(this);
        llVessels.setOnClickListener(this);
        llAgents.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    public void setNotificationCount() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (selectedFragment instanceof SurveysFragment) {
                        ((SurveysFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof AppointFragment) {
                        ((AppointFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof VesselsFragment) {
                        ((VesselsFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof AgentsFragment) {
                        ((AgentsFragment) selectedFragment).get_Notification_list();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.llSurveys:
                manageTabs(0, true, false, false, false, false);
                break;
            case R.id.llAppoint:
                manageTabs(1, false, true, false, false, false);
                break;
            case R.id.llVessels:
                manageTabs(2, false, false, true, false, false);
                break;
            case R.id.llAgents:
                manageTabs(3, false, false, false, true, false);
                break;
            case R.id.llMore:
                manageTabs(4, false, false, false, false, true);
                break;
        }

    }

    public void manageTabs(int position, boolean surveys, boolean appoint, boolean vessels, boolean agents, boolean more) {
        try {
            switch (position) {
                case 0:
                    selectedFragment = new SurveysFragment();
                    break;
                case 1:
                    selectedFragment = new AppointFragment();
                    break;
                case 2:
                    selectedFragment = new VesselsFragment();
                    break;
                case 3:
                    selectedFragment = new AgentsFragment();

                    break;
                case 4:
                    selectedFragment = new MoreFragment();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            if (surveys) {
                llSurveys.setEnabled(false);
                imvSurveys.setImageDrawable(getResources().getDrawable(R.drawable.surveys_select));
            } else {
                llSurveys.setEnabled(true);
                imvSurveys.setImageDrawable(getResources().getDrawable(R.drawable.surveys_unselect));
            }
            if (appoint) {
                llAppoint.setEnabled(false);
                imvAppoint.setImageDrawable(getResources().getDrawable(R.drawable.appoint_select));
            } else {
                llAppoint.setEnabled(true);
                imvAppoint.setImageDrawable(getResources().getDrawable(R.drawable.appoint_unselect));
            }
            if (vessels) {
                llVessels.setEnabled(false);
                imvVessels.setImageDrawable(getResources().getDrawable(R.drawable.vessels_select));
            } else {
                llVessels.setEnabled(true);
                imvVessels.setImageDrawable(getResources().getDrawable(R.drawable.vessels_unselect));
            }
            if (agents) {
                llAgents.setEnabled(false);
                imvAgents.setImageDrawable(getResources().getDrawable(R.drawable.agents_select));
            } else {
                llAgents.setEnabled(true);
                imvAgents.setImageDrawable(getResources().getDrawable(R.drawable.agents_unselect));
            }
            if (more) {
                llMore.setEnabled(false);
                imvMore.setImageDrawable(getResources().getDrawable(R.drawable.more_select));
            } else {
                llMore.setEnabled(true);
                imvMore.setImageDrawable(getResources().getDrawable(R.drawable.more_unselect));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callAgentApi() {
        if (Util.hasInternet(this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<AgentListResponse> call = apiService.agents_list(myHashMap);
            call.enqueue(new Callback<AgentListResponse>() {
                @Override
                public void onResponse(Call<AgentListResponse> call, Response<AgentListResponse> response) {
                    AgentListResponse listResponse = response.body();
                    if (listResponse != null) {
                        agentDataList.addAll(listResponse.getResponse().getData());
                        if (selectedFragment instanceof AgentsFragment) {
                            AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                            agentsFragment.setAdapterDataList(agentDataList);
                        }

                    }
                }

                @Override
                public void onFailure(Call<AgentListResponse> call, Throwable t) {
                    if (selectedFragment instanceof AgentsFragment) {
                        AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                        agentsFragment.noNetworkConnection();
                    }

                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }
    }


    private void callSurveyTypeApi() {
        if (Util.hasInternet(this)) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            Log.d("1234","operator:"+ Arrays.asList(myHashMap));
            Call<SurveyTypeResponse> call = apiService.survey_category(myHashMap);
            call.enqueue(new Callback<SurveyTypeResponse>() {
                @Override
                public void onResponse(Call<SurveyTypeResponse> call, Response<SurveyTypeResponse> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        SurveyTypeResponse listResponse = response.body();
                        if (listResponse != null) {
                            appointData = listResponse.getResponse().getData();
                            if (appointData != null) {
                                if (selectedFragment instanceof AppointFragment) {
                                    AppointFragment appointFragment = (AppointFragment) selectedFragment;
                                    appointFragment.setListData(appointData);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SurveyTypeResponse> call, Throwable t) {

                    Log.d("1234","error"+t.getMessage());

                    Util.dismissProDialog();
                    if (selectedFragment instanceof AppointFragment) {
                        AppointFragment appointFragment = (AppointFragment) selectedFragment;
                        appointFragment.noNetworkConnection();

                    }
                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }
    }


    private void callVesselsApi() {
        if (Util.hasInternet(this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<VesselListResponse> call = apiService.vessels_list(myHashMap);
            call.enqueue(new Callback<VesselListResponse>() {
                @Override
                public void onResponse(Call<VesselListResponse> call, Response<VesselListResponse> response) {
                    if (response.body() != null) {
                        VesselListResponse listResponse = response.body();
                        if (listResponse != null) {
                            Log.d("1234", "onResponse: ");
                            vesselDataList.addAll(listResponse.getResponse().getData());
                            if (selectedFragment instanceof VesselsFragment) {
                                Log.d("1234", "onResponse: 1234");
                                VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                                vesselsFragment.setAdapterDataList(vesselDataList);
                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<VesselListResponse> call, Throwable t) {
                    if (selectedFragment instanceof VesselsFragment) {
                        VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                        vesselsFragment.noNetworkConnection();
                    }
                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }

    }

    @Override
    public void callServerApi(boolean isClearData) {
        if (selectedFragment instanceof AgentsFragment) {
            if (isClearData) {
                agentDataList.clear();
                callAgentApi();
            } else {
                if (agentDataList.size() > 0) {
                    AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                    agentsFragment.setAdapterDataList(agentDataList);
                } else {
                    callAgentApi();
                }
            }

        } else if (selectedFragment instanceof VesselsFragment) {
            if (isClearData) {
                vesselDataList.clear();
                callVesselsApi();
            } else {
                if (vesselDataList.size() > 0) {
                    VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                    vesselsFragment.setAdapterDataList(vesselDataList);
                } else {
                    callVesselsApi();
                }
            }

        } else if (selectedFragment instanceof AppointFragment) {
            if (appointData != null) {
                AppointFragment appointFragment = (AppointFragment) selectedFragment;
                appointFragment.setListData(appointData);
            } else {
                callSurveyTypeApi();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            if (selectedFragment instanceof AgentsFragment) {
                AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                AgentData agentData = (AgentData) data.getSerializableExtra("responseData");
                agentsFragment.updateItem(agentData);
            } else if (selectedFragment instanceof VesselsFragment) {
                VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                VesselData vesselData = (VesselData) data.getSerializableExtra("responseData");
                vesselsFragment.updateItem(vesselData);
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            if (selectedFragment instanceof AgentsFragment) {
                Log.d("1234", "onActivityResult: ");
                AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                AgentData agentData = (AgentData) data.getSerializableExtra("responseData");
                agentsFragment.addItem(agentData);
            } else if (selectedFragment instanceof VesselsFragment) {
                VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                VesselData vesselData = (VesselData) data.getSerializableExtra("responseData");
                vesselsFragment.addItem(vesselData);
            }
        } else if (requestCode == 104 && resultCode == RESULT_OK) {
            if (selectedFragment instanceof SurveysFragment) {
                SurveysFragment surveysFragment = (SurveysFragment) selectedFragment;
                SurveyData surveyData = (SurveyData) data.getSerializableExtra("responseData");
                surveysFragment.updateItem(surveyData);
            } else if (selectedFragment instanceof VesselsFragment) {
                VesselsFragment vesselsFragment = (VesselsFragment) selectedFragment;
                VesselData vesselData = (VesselData) data.getSerializableExtra("responseData");
                vesselsFragment.addItem(vesselData);
            }
        } else if (requestCode == 105 && resultCode == RESULT_OK) {
            Log.d("1234", "onActivityResult: "+selectedFragment);
            if (selectedFragment instanceof SurveysFragment) {
                Log.d("1234", "onActivityResult:1 ");
                SurveysFragment surveysFragment = (SurveysFragment) selectedFragment;
                SurveyData surveyData = (SurveyData) data.getSerializableExtra("responseData");
                surveysFragment.updateItem(surveyData);
            }
        }
    }

}
