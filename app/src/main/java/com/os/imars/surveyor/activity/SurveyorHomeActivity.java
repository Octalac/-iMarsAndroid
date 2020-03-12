package com.os.imars.surveyor.activity;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.mySurvey.SurveyData;
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.fragment.AgentsFragment;
import com.os.imars.surveyor.dao.CalendarDataItem;
import com.os.imars.surveyor.dao.PortData;
import com.os.imars.surveyor.dao.PortResponse;
import com.os.imars.surveyor.dao.ServiceData;
import com.os.imars.surveyor.dao.ServiceResponse;
import com.os.imars.surveyor.fragment.CalendarFragment;
import com.os.imars.surveyor.fragment.MoreFragment;
import com.os.imars.surveyor.fragment.PortsFragment;
import com.os.imars.surveyor.fragment.ServicesFragment;
import com.os.imars.surveyor.fragment.SurveysFragment;
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

public class SurveyorHomeActivity extends BaseActivity implements CallApiCallback {

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
    private TextView txtNotificationCount;
    private RelativeLayout rlNotification;
    private RelativeLayout llVessels;
    private ImageView imvAgents;
    private TextView txtAgents;
    private LinearLayout llAgents;
    private ImageView imvMore, imvFilter;
    private TextView txtMore;
    private LinearLayout llMore;
    private List<PortData> portDataList;
    private List<ServiceData> serviceDataList;
    private List<SurveyData> surveyDataList;
    private List<CalendarDataItem> calendarDataItemList;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyor_home);
        initView();
    }

    private void initView() {
        try {
            session = Session.getInstance(this);
            portDataList = new ArrayList<>();
            serviceDataList = new ArrayList<>();
            surveyDataList = new ArrayList<>();
            calendarDataItemList = new ArrayList<>();
            selectedFragment = new SurveysFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new SurveysFragment())
                    .commit();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            llBottom = (LinearLayout) findViewById(R.id.llBottom);
            setSupportActionBar(toolbar);
            appBar = (AppBarLayout) findViewById(R.id.appBar);
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
            txtNotificationCount = (TextView) findViewById(R.id.txtNotificationCount);
            rlNotification = (RelativeLayout) findViewById(R.id.rlNotification);
            llVessels = (RelativeLayout) findViewById(R.id.llVessels);
            imvAgents = (ImageView) findViewById(R.id.imvAgents);
            txtAgents = (TextView) findViewById(R.id.txtAgents);
            llAgents = (LinearLayout) findViewById(R.id.llAgents);
            imvMore = (ImageView) findViewById(R.id.imvMore);
            txtMore = (TextView) findViewById(R.id.txtMore);
            llMore = (LinearLayout) findViewById(R.id.llMore);
            imvFilter = (ImageView) findViewById(R.id.imvFilter);

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
        if (session.getUserData().getType().equals("3")){
            llVessels.setVisibility(View.GONE);
            llAppoint.setVisibility(View.GONE);
        }
    }


    public void setNotificationCount() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (selectedFragment instanceof SurveysFragment) {
                        ((SurveysFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof ServicesFragment) {
                        ((ServicesFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof PortsFragment) {
                        ((PortsFragment) selectedFragment).get_Notification_list();
                    }
                    if (selectedFragment instanceof CalendarFragment) {
                        ((CalendarFragment) selectedFragment).get_Notification_list();
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

    public void manageTabs(int position, boolean surveys, boolean service, boolean port, boolean calendar, boolean more) {
        try {
            switch (position) {
                case 0:
                    selectedFragment = new SurveysFragment();
                    break;
                case 1:
                    selectedFragment = new ServicesFragment();
                    break;
                case 2:
                    selectedFragment = new PortsFragment();
                    break;
                case 3:
                    selectedFragment = new CalendarFragment();
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
            if (service) {
                llAppoint.setEnabled(false);
                imvAppoint.setImageDrawable(getResources().getDrawable(R.drawable.agents_select));
            } else {
                llAppoint.setEnabled(true);
                imvAppoint.setImageDrawable(getResources().getDrawable(R.drawable.agents_unselect));
            }

            if (port) {
                llVessels.setEnabled(false);
                imvVessels.setImageDrawable(getResources().getDrawable(R.drawable.vessels_select));
            } else {
                llVessels.setEnabled(true);
                imvVessels.setImageDrawable(getResources().getDrawable(R.drawable.vessels_unselect));
            }
            if (calendar) {
                llAgents.setEnabled(false);
                imvAgents.setImageDrawable(getResources().getDrawable(R.drawable.calendra_select));
                imvSearch.setVisibility(View.GONE);
            } else {
                llAgents.setEnabled(true);
                imvAgents.setImageDrawable(getResources().getDrawable(R.drawable.calendra_unselect));
            }
            if (more) {
                llMore.setEnabled(false);
                imvSearch.setVisibility(View.GONE);
                imvFilter.setVisibility(View.GONE);
                imvMore.setImageDrawable(getResources().getDrawable(R.drawable.more_select));
            } else {
                llMore.setEnabled(true);
                imvMore.setImageDrawable(getResources().getDrawable(R.drawable.more_unselect));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (selectedFragment instanceof SurveysFragment) {
                SurveysFragment surveysFragment = (SurveysFragment) selectedFragment;
                SurveyData responseData = (SurveyData) data.getSerializableExtra("responseData");
                Log.d("1234", "onActivityResult: "+responseData.getStatus());
                surveysFragment.updateItem(responseData);
            }
        }
    }




    @Override
    public void onBackPressed() {
        Log.d("1234", "onBackPressed: ");
        if (selectedFragment instanceof SurveysFragment) {
            Log.d("1234", "onBackPressed:12 ");
            SurveysFragment agentsFragment = (SurveysFragment) selectedFragment;
            Log.d("1234", "onBackPressed: 123" + agentsFragment.isSearchVisible);
            if (agentsFragment.isSearchVisible) {
                Log.d("1234", "onBackPressed: 1234");
                agentsFragment.llSearch.setVisibility(View.GONE);
                agentsFragment.isSearchVisible = !agentsFragment.isSearchVisible;
            } else {
                Log.d("1234", "onBackPressed: 1235" + agentsFragment.isSearchVisible);
                finish();
            }
        }

    }

    @Override
    public void callServerApi(boolean isClearData) {

        if (selectedFragment instanceof ServicesFragment) {
            if (isClearData) {
                serviceDataList.clear();
                callServiceApi();
            } else {
                if (serviceDataList.size() > 0) {
                    ServicesFragment servicesFragment = (ServicesFragment) selectedFragment;
                    servicesFragment.setAdapterDataList(serviceDataList);
                } else {
                    callServiceApi();
                }
            }

        } else if (selectedFragment instanceof PortsFragment) {
            if (isClearData) {
                portDataList.clear();
                callPortApi();
            } else {
                if (portDataList.size() > 0) {
                    PortsFragment portsFragment = (PortsFragment) selectedFragment;
                    portsFragment.setAdapterDataList(portDataList);
                } else {
                    callPortApi();
                }
            }
        }/* else if (selectedFragment instanceof CalendarFragment) {
            if (isClearData) {
                surveyDataList.clear();
                callSurveyAccordingCalendarApi();
            } else {
                if (portDataList.size() > 0) {
                    CalendarFragment calendarFragment = (CalendarFragment) selectedFragment;
                    calendarFragment.setAdapterDataList(calendarDataItemList);
                } else {
                    callSurveyAccordingCalendarApi();
                }
            }
        }*/
    }

  /*  private void callSurveyAccordingCalendarApi() {
        if (Util.hasInternet(this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<LoadAvailabilityResponse> call = apiService.event_load(myHashMap);
            call.enqueue(new Callback<LoadAvailabilityResponse>() {
                @Override
                public void onResponse(Call<LoadAvailabilityResponse> call, Response<LoadAvailabilityResponse> response) {
                    if (response.body() != null) {
                        calendarDataItemList.addAll(response.body().getResponse().getData());
                        if (selectedFragment instanceof CalendarFragment) {
                            CalendarFragment calendarFragment = (CalendarFragment) selectedFragment;
                            calendarFragment.setAdapterDataList(calendarDataItemList);
                        }

                    }
                }

                @Override
                public void onFailure(Call<LoadAvailabilityResponse> call, Throwable t) {


                }
            });
        }
    }
*/


    private void callServiceApi() {
        if (Util.hasInternet(this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<ServiceResponse> call = apiService.surveyor_services(myHashMap);
            call.enqueue(new Callback<ServiceResponse>() {
                @Override
                public void onResponse(Call<ServiceResponse> call, Response<ServiceResponse> response) {
                    if (response.body() != null) {
                        ServiceResponse listResponse = response.body();
                        if (listResponse != null) {
                            serviceDataList.addAll(listResponse.getResponse().getData());
                            if (selectedFragment instanceof ServicesFragment) {
                                ServicesFragment servicesFragment = (ServicesFragment) selectedFragment;
                                servicesFragment.setAdapterDataList(serviceDataList);
                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<ServiceResponse> call, Throwable t) {
                    if (selectedFragment instanceof AgentsFragment) {
                        AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                        agentsFragment.noNetworkConnection();
                    }

                }
            });
        } else {
            // Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }

    }


    private void callPortApi() {
        if (Util.hasInternet(this)) {
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<PortResponse> call = apiService.surveyor_port(myHashMap);
            call.enqueue(new Callback<PortResponse>() {
                @Override
                public void onResponse(Call<PortResponse> call, Response<PortResponse> response) {
                    if (response.body() != null){
                        PortResponse listResponse = response.body();
                        if (listResponse != null) {
                            portDataList.addAll(listResponse.getResponse().getData());
                            if (selectedFragment instanceof PortsFragment) {
                                PortsFragment portsFragment = (PortsFragment) selectedFragment;
                                portsFragment.setAdapterDataList(portDataList);
                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<PortResponse> call, Throwable t) {
                    if (selectedFragment instanceof AgentsFragment) {
                        AgentsFragment agentsFragment = (AgentsFragment) selectedFragment;
                        agentsFragment.noNetworkConnection();
                    }

                }
            });
        } else {
            // Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }

    }

}

