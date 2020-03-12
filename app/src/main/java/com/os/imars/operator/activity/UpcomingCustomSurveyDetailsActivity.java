package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.dao.mySurvey.SurveyResponse;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.operator.adapter.AcceptBidUserAdapter;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.surveyor.BiddingUserItem;
import com.os.imars.operator.dao.surveyor.BiddingUserResponse;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingCustomSurveyDetailsActivity extends BaseActivity {

    private ImageView imvBack, edtAgentDetails;
    private Button btnCancel, btnSurveyComplete, btnReportAccept, btnAcceptBidding, btnReportDownload, btnViewReport, btnDownloadReport, btnViewInvoice, btnDownloadInvoice;
    private String surveyId = "", surveyor_id = "";
    private TextView txtSurveyNumber, txtSurveyor, txtPrice, txtDate, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtVesselAddress, txtSurveyStatus, txtVesselEmail, txtAgentEmail, txtAgentContact, txtAgentName, txtNoDataFound, txtInstructionAndDocument;
    private CardView cardViewInstructionAndDocument, cardViewReportAndInvoice;
    private ScrollView llBodyView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Session session;
    private String isVisibleAgentDetails = "", agentId = "", survey_id = "", survey_type = "", operator_id = "", uploadedFile = "", reportUrl = "", bidUserId = "";
    private SurveyRequestDetails surveyRequestDetails;
    private ArrayList<String> agentDataList;
    private ArrayList<AgentData> agentDataArrayList;
    private HashMap<String, String> hashMap;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<BiddingUserItem> biddingUser;
    private int selectedItemPosition = -1;
    private ProgressDialog progressDialog;
    private static final int MEGABYTE = 1024 * 1024;
    private LinearLayout llDownloadFile, llSurveyorDetails;
    private DownloadManager downloadManager;
    private LinearLayout llInvoice;
    ArrayList<Long> list = new ArrayList<>();
    long refid;


    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            list.remove(referenceId);
            if (list.isEmpty()) {
                Log.e("INSIDE", "" + referenceId);
                Toast.makeText(ctxt, "File downloaded Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_custom_survey_details);
        initView();
        callApi();
        callAgentApi();
        callApiBidingUser();
    }


    private void initView() {
        agentDataList = new ArrayList<>();
        biddingUser = new ArrayList<>();
        agentDataArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        session = Session.getInstance(UpcomingCustomSurveyDetailsActivity.this);

        btnViewReport = findViewById(R.id.btnViewReport);
        btnDownloadReport = findViewById(R.id.btnViewReport);

        shimmerFrameLayout = findViewById(R.id.shimmerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        surveyId = getIntent().getStringExtra("id");
        surveyor_id = getIntent().getStringExtra("surveyor_id");
        imvBack = (ImageView) findViewById(R.id.imvBack);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtSurveyNumber = findViewById(R.id.txtSurveyNumber);
        edtAgentDetails = findViewById(R.id.edtAgentDetails);
        txtSurveyor = findViewById(R.id.txtSurveyor);
        llBodyView = findViewById(R.id.llBodyView);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtPrice = findViewById(R.id.txtPrice);
        llSurveyorDetails = findViewById(R.id.llSurveyorDetails);
        txtDate = findViewById(R.id.txtDate);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        txtPort = findViewById(R.id.txtPort);
        btnReportAccept = findViewById(R.id.btnReportAccept);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtFileName = findViewById(R.id.txtFileName);
        txtVesselName = findViewById(R.id.txtVesselName);
        btnAcceptBidding = findViewById(R.id.btnAcceptBidding);
        cardViewInstructionAndDocument = findViewById(R.id.cardViewInstructionAndDocument);
        txtInstructionAndDocument = findViewById(R.id.txtInstructionAndDocument);
        txtIMO = findViewById(R.id.txtIMO);
        txtVesselCompanyName = findViewById(R.id.txtVesselCompanyName);
        txtVesselAddress = findViewById(R.id.txtVesselAddress);
        txtVesselEmail = findViewById(R.id.txtVesselEmail);
        txtAgentContact = findViewById(R.id.txtAgentContact);
        txtAgentName = findViewById(R.id.txtAgentName);
        txtAgentEmail = findViewById(R.id.txtAgentEmail);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        btnReportDownload = findViewById(R.id.btnDownloadReport);
        btnSurveyComplete = (Button) findViewById(R.id.btnSurveyComplete);
        llInvoice = findViewById(R.id.llInvoice);
        imvBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSurveyComplete.setOnClickListener(this);
        edtAgentDetails.setOnClickListener(this);
        btnReportAccept.setOnClickListener(this);
        btnAcceptBidding.setOnClickListener(this);
        btnReportDownload.setOnClickListener(this);
        llDownloadFile.setOnClickListener(this);

        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);

        isVisibleAgentDetails = session.getUserData().getType();
        if (isVisibleAgentDetails.equals("0")) {
            edtAgentDetails.setVisibility(View.VISIBLE);
        }
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void callApiBidingUser() {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("survey_id", surveyId);
        RxService apiService = App.getClient().create(RxService.class);
        Call<BiddingUserResponse> call = apiService.custom_survey_biding_user(myHashMap);
        call.enqueue(new Callback<BiddingUserResponse>() {
            @Override
            public void onResponse(Call<BiddingUserResponse> call, Response<BiddingUserResponse> response) {
                if (response.body() != null) {
                    BiddingUserResponse listResponse = response.body();
                    if (listResponse != null) {
                        biddingUser.addAll(listResponse.getResponse().getData());
                    }
                }

            }

            @Override
            public void onFailure(Call<BiddingUserResponse> call, Throwable t) {
                Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void callAgentApi() {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<AgentListResponse> call = apiService.agents_list(myHashMap);
        call.enqueue(new Callback<AgentListResponse>() {
            @Override
            public void onResponse(Call<AgentListResponse> call, Response<AgentListResponse> response) {
                if (response.body() != null) {
                    AgentListResponse listResponse = response.body();
                    if (listResponse != null) {
                        for (AgentData agentsDataItem : listResponse.getResponse().getData()) {
                            agentDataArrayList.add(agentsDataItem);
                            agentDataList.add(agentsDataItem.getFirst_name() + " " + agentsDataItem.getLast_name());
                            hashMap.put(agentsDataItem.getFirst_name() + " " + agentsDataItem.getLast_name(), agentsDataItem.getId());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<AgentListResponse> call, Throwable t) {
                Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnSurveyComplete:
                startActivity(new Intent(UpcomingCustomSurveyDetailsActivity.this, SurveyInvoiceActivity.class));
                break;
            case R.id.edtAgentDetails:
                edtAgentDetailsDialog("Select Agent", txtAgentName, txtAgentContact, txtAgentEmail, agentDataList);
                break;
            case R.id.btnCancel:
                cancelSurveyRequest();
                break;
            case R.id.btnReportAccept:
                callApiReportAccept();
                break;
            case R.id.btnAcceptBidding:
                callListUserBidding();
                break;
            case R.id.llDownloadFile:
                downloadFile(uploadedFile);
                break;
            case R.id.btnViewReport:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(reportUrl));
                startActivity(intent);
                break;
            case R.id.btnDownloadReport:
                downloadFile(reportUrl);
                break;


        }
    }


    private void downloadFile(String uploadedFile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("iMarS");
        builder.setMessage("Are you sure you want to Download File?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiToDownloadReport(uploadedFile);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private void downloadReport() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("iMarS");
        builder.setMessage("Are you sure you want to Download survey report?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiToDownloadReport(reportUrl);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }


    private void callApiToDownloadReport(String url) {

        // new UpcomingCustomSurveyDetailsActivity.DownloadFile().execute(url);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("GadgetSaint Downloading " + "Sample" + ".png");
        request.setDescription("Downloading " + "Sample" + ".png");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/iMars/" + "/" + url);
        refid = downloadManager.enqueue(request);
        list.add(refid);


    }

    private void callListUserBidding() {


        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.adapter_recycler_accept_bid, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view1).create();
        ListView lv = view1.findViewById(R.id.listViewDialog);
        TextView tvTitle = view1.findViewById(R.id.title);
        LinearLayout llBottom = view1.findViewById(R.id.llBottom);
        TextView txtNoRecordFound = view1.findViewById(R.id.txtNoRecordFound);
        Button btnAccept = view1.findViewById(R.id.btnAccept);
        Button btnCancel = view1.findViewById(R.id.btnCancel);
        tvTitle.setText("Select User");
        lv.setChoiceMode(lv.CHOICE_MODE_SINGLE);
        if (biddingUser.size() == 0) {
            txtNoRecordFound.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            llBottom.setVisibility(View.GONE);
        } else {
            txtNoRecordFound.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
        }
        AcceptBidUserAdapter acceptBidUserAdapter = new AcceptBidUserAdapter(this, R.layout.survey_bid_user_row, biddingUser);
        lv.setAdapter(acceptBidUserAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {

            for (BiddingUserItem biddingUserItem : biddingUser) {
                biddingUserItem.setSelected(false);
            }
            bidUserId = biddingUser.get(position).getId();
            biddingUser.get(position).setSelected(true);
            acceptBidUserAdapter.notifyDataSetChanged();
        });
        btnAccept.setOnClickListener(v -> {
            if (bidUserId.equals("") || bidUserId == null) {
                Toast.makeText(this, "Select User", Toast.LENGTH_SHORT).show();
            } else {
                callAcceptBidding(bidUserId);
                dialog.cancel();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.cancel());

        dialog.show();


    }


    private void callAcceptBidding(String bidUserId) {

        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        Util.showProDialog(this);
        myHashMap.put("survey_id", survey_id);
        myHashMap.put("operator_id", operator_id);
        myHashMap.put("surveyors_id", bidUserId);
        Log.d("1234", "callAcceptBidding: " + Arrays.asList(myHashMap));
        RxService apiService = App.getClient().create(RxService.class);
        Call<SurveyResponse> call = apiService.operator_custom_bidding_accept(myHashMap);
        call.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    SurveyResponse listResponse = response.body();

                    if (listResponse.getResponse().getStatus() == 1) {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);
                        Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, OperatorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {

                Util.dismissProDialog();
            }
        });
    }

    private void callApiReportAccept() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("iMarS");
        builder.setMessage("Are you sure you want to accept this survey report?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
                HashMap<String, Object> myHashMap = new HashMap<String, Object>();
                myHashMap.put("operator_id", session.getUserData().getUserId());
                myHashMap.put("surveyor_id", surveyor_id);
                myHashMap.put("survey_id", surveyId);
                Log.d("1234", "callApiChangeAgent: " + Arrays.asList(myHashMap));
                RxService apiService = App.getClient().create(RxService.class);
                Call<SurveyResponse> call = apiService.report_accept(myHashMap);
                call.enqueue(new Callback<SurveyResponse>() {
                    @Override
                    public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            SurveyResponse listResponse = response.body();
                            if (listResponse.getResponse().getStatus() == 1) {
                                Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);

                                Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, OperatorHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SurveyResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
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
    }


    private void cancelSurveyRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingCustomSurveyDetailsActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to Cancel this survey?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callSurveyCancel();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void callSurveyCancel() {
        Log.d("1234", "callCustomAcceptAndDeclineApi: ");
        HashMap<String, Object> hashMap = new HashMap<>();
        Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
        RxService apiService = App.getClient().create(RxService.class);
        hashMap.put("user_id", session.getUserData().getUserId());
        hashMap.put("survey_id", survey_id);
        Log.d("1234", "callCustomAcceptAndDeclineApi: " + Arrays.asList(hashMap));
        Call<SurveyResponse> call = apiService.cancel_survey(hashMap);
        call.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                Util.dismissProDialog();
                llBodyView.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    SurveyResponse details = response.body();
                    Log.d("1234", "onResponse: " + details.getResponse().getStatus());
                    if (details.getResponse().getStatus() == 1) {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);
                        /*Intent intent = new Intent();
                        intent.putExtra("responseData", details.getResponse().getSurveyData());
                        setResult(RESULT_OK, intent);
                        finish();
                        */

                        Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, OperatorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);

                    }
                }

            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });


    }

/*    private void callAcceptAndDeclineApi(String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
        RxService apiService = App.getClient().create(RxService.class);
        hashMap.put("surveyors_id", session.getUserData().getUserId());
        hashMap.put("type", type);
        hashMap.put("survey_id", survey_id);
        Call<SurveyResponse> call = apiService.survey_accept_reject(hashMap);
        call.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                Util.dismissProDialog();
                llBodyView.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    SurveyResponse details = response.body();
                    Log.d("1234", "onResponse: " + details.getResponse().getStatus());
                    if (details.getResponse().getStatus() == 1) {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);
                        Intent intent = new Intent();
                        intent.putExtra("responseData", details.getResponse().getSurveyData());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);

                    }
                }

            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }*/

    private void edtAgentDetailsDialog(String title, TextView txtAgentName, TextView txtAgentContact, TextView txtAgentEmail, List<String> portList) {

        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_agent_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(UpcomingCustomSurveyDetailsActivity.this)
                .setTitle(title)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UpcomingCustomSurveyDetailsActivity.this, android.R.layout.simple_list_item_1, portList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            String entry = (String) parent.getAdapter().getItem(position);
            Log.d("1234", "edtAgentDetailsDialog: " + agentDataArrayList.size());
            txtAgentName.setText(entry);
            agentId = hashMap.get(entry);
            txtAgentContact.setText(agentDataArrayList.get(position).getMobile());
            txtAgentEmail.setText(agentDataArrayList.get(position).getEmail());
            callApiChangeAgent(agentId, surveyId);
            dialog.cancel();
        });
        dialog.show();
    }

    private void callApiChangeAgent(String agentId, String surveyId) {
        Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", operator_id);
        myHashMap.put("survey_id", surveyId);
        myHashMap.put("agent_id", agentId);
        Log.d("1234", "callApiChangeAgent: " + Arrays.asList(myHashMap));
        RxService apiService = App.getClient().create(RxService.class);
        Call<CommonResponse> call = apiService.edit_agent_survey(myHashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    CommonResponse listResponse = response.body();
                    if (listResponse.getResponse().getStatus() == 1) {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);
                    }
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(UpcomingCustomSurveyDetailsActivity.this)) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", surveyId);
            hashMap.put("user_id", session.getUserData().getUserId());
            Call<SurveyRequestDetails> call = apiService.survey_details(hashMap);
            call.enqueue(new Callback<SurveyRequestDetails>() {
                @Override
                public void onResponse(Call<SurveyRequestDetails> call, Response<SurveyRequestDetails> response) {
                    Util.hideShimmer(shimmerFrameLayout);
                    if (response.body() != null) {
                        surveyRequestDetails = response.body();
                        if (surveyRequestDetails.getResponse().getStatus() == 1) {
                            llBodyView.setVisibility(View.VISIBLE);
                            txtNoDataFound.setVisibility(View.GONE);
                            setSurveyDetailsData(surveyRequestDetails);
                        } else {
                            llBodyView.setVisibility(View.GONE);
                            txtNoDataFound.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SurveyRequestDetails> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.hideShimmer(shimmerFrameLayout);
                    Toast.makeText(UpcomingCustomSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();
        }

    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingCustomSurveyDetailsActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApi();
            }
        });
        builder.show();
    }


    private void setSurveyDetailsData(SurveyRequestDetails details) {

        if (details.getResponse().getData().size() != 0) {
            Log.d("1234", "setSurveyDetailsData: 1" + details.getResponse().getData().get(0).getStatus());
            if (details.getResponse().getData().get(0).getFileData().equals("")) {
                txtInstructionAndDocument.setVisibility(View.GONE);
                cardViewInstructionAndDocument.setVisibility(View.GONE);
            } else {
                txtInstruction.setText(details.getResponse().getData().get(0).getInstruction());
                txtFileName.setText("doc file");
            }


            if (details.getResponse().getData().get(0).getOperator_id().equals(session.getUserData().getUserId())) {
                if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                    btnCancel.setVisibility(View.VISIBLE);
                    btnReportAccept.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnReportDownload.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                    if (details.getResponse().getData().get(0).getBid_accept_status().equals("0")) {
                        btnAcceptBidding.setVisibility(View.VISIBLE);
                    } else {
                        btnAcceptBidding.setVisibility(View.GONE);
                    }

                } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                    btnAcceptBidding.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnReportDownload.setVisibility(View.GONE);

                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {

                    btnAcceptBidding.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnReportDownload.setVisibility(View.GONE);


                } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                    btnAcceptBidding.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.VISIBLE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnReportDownload.setVisibility(View.VISIBLE);
                    llInvoice.setVisibility(View.GONE);
                }

            } else {
                if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                    edtAgentDetails.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                    if (details.getResponse().getData().get(0).getBid_accept_status().equals("0")) {
                        btnAcceptBidding.setVisibility(View.VISIBLE);
                    } else {
                        btnAcceptBidding.setVisibility(View.GONE);
                    }
                } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                    btnCancel.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                    Log.d("1234", "setSurveyDetailsData: 123");
                    btnCancel.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                    Log.d("1234", "setSurveyDetailsData: 123");
                    btnCancel.setVisibility(View.GONE);
                    btnSurveyComplete.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                }
            }

            switch (details.getResponse().getData().get(0).getStatus()) {
                case "0":
                    txtSurveyStatus.setText("Pending");
                    break;
                case "1":
                    txtSurveyStatus.setText("Upcoming");
                    break;
                case "2":
                    txtSurveyStatus.setText("Cancelled");
                    break;
                case "3":
                    txtSurveyStatus.setText("Report Submitted");
                    break;
                case "4":
                    txtSurveyStatus.setText("Report Accepted");
                    break;
                case "5":
                    txtSurveyStatus.setText("Invoice Paid");
                    break;
                case "6":
                    txtSurveyStatus.setText("Surveyor Paid");
                    break;
            }


            if (details.getResponse().getData().get(0).getSurveyor_id().equals("") || details.getResponse().getData().get(0).getSurveyor_id() == null) {
                llSurveyorDetails.setVisibility(View.GONE);
            } else {
                llSurveyorDetails.setVisibility(View.VISIBLE);
            }
            reportUrl = details.getResponse().getData().get(0).getReportURL();
            operator_id = details.getResponse().getData().get(0).getOperator_id();
            survey_type = details.getResponse().getData().get(0).getSurveycateName();
            survey_id = details.getResponse().getData().get(0).getId();
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtSurveyor.setText(details.getResponse().getData().get(0).getSurveyorsName());
            txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing());
            String startDate = details.getResponse().getData().get(0).getStartDate();
            String endDate = details.getResponse().getData().get(0).getEndDate();
            txtDate.setText(Util.parseDateToddMMyyyy(startDate) + " to " + Util.parseDateToddMMyyyy(endDate));
            txtSurveyType.setText(details.getResponse().getData().get(0).getSurveycateName());
            txtPort.setText(details.getResponse().getData().get(0).getPort());
            txtOperator.setText(details.getResponse().getData().get(0).getOperator_name());
            txtVesselName.setText(details.getResponse().getData().get(0).getVesselsname());
            txtIMO.setText(details.getResponse().getData().get(0).getImo_number());
            txtVesselCompanyName.setText(details.getResponse().getData().get(0).getVesselscompany());
            txtVesselAddress.setText(details.getResponse().getData().get(0).getVesselsaddress());
            txtVesselEmail.setText(details.getResponse().getData().get(0).getVesselsemail());
            txtAgentEmail.setText(details.getResponse().getData().get(0).getAgentsemail());
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtAgentContact.setText(details.getResponse().getData().get(0).getAgentsmobile());
            txtAgentName.setText(details.getResponse().getData().get(0).getAgentName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }


}



