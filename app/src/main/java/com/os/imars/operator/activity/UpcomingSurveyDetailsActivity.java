package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.dao.mySurvey.SurveyResponse;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.dao.mySurvey.SurveyUsersListResponse;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.surveyor.BiddingUserItem;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.surveyor.adapter.SurveyorListAdapter;
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
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingSurveyDetailsActivity extends BaseActivity {

    private ImageView imvBack, edtAgentDetails, imgEditOperator;
    private Button btnCancel, btnSurveyComplete, btnReportAccept, btnAcceptBidding, btnViewReport, btnDownloadReport, btnViewInvoice, btnDownloadInvoice;
    private String surveyId = "", surveyor_id = "", operatorId = "";
    private TextView txtSurveyNumber, txtViewFile, txtSurveyor, txtPrice, txtDate, txtSurveyorCompany, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtVesselAddress, txtSurveyStatus, txtVesselEmail, txtAgentEmail, txtAgentContact, txtAgentName, txtNoDataFound, txtInstructionAndDocument;
    private CardView cardViewInstructionAndDocument, cardViewReportAndInvoice;
    private ScrollView llBodyView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Session session;
    private String isVisibleAgentDetails = "", agentId = "", survey_id = "", survey_type = "", operator_id = "", uploadedFile = "", surveyReport = "", invoiceURL = "", reportURL = "";
    private SurveyRequestDetails surveyRequestDetails;
    private ArrayList<String> agentDataList;
    private ArrayList<AgentData> agentDataArrayList;
    HashMap<String, String> hashMap;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<String> bidingUserArrayList;
    ArrayList<BiddingUserItem> arrayList;
    private int selectedItemPosition = -1;
    private ProgressDialog progressDialog;
    private static final int MEGABYTE = 1024 * 1024;
    private LinearLayout llReportAccept, llReport, llInvoice;
    private RelativeLayout llDownloadFile;
    private DownloadManager downloadManager;
    ArrayList<Long> list = new ArrayList<>();
    long refid;
    private List<SurveyUserDataItem> userDataItemList;
    private List<String> userList = new ArrayList<>();


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
        setContentView(R.layout.activity_upcoming_survey_details);
        initView();
        callApi();
        callAgentApi();
        callOperatorListApi();
    }


    private void initView() {
        agentDataList = new ArrayList<>();
        arrayList = new ArrayList<>();
        agentDataArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        userDataItemList = new ArrayList<>();
        bidingUserArrayList = new ArrayList<>();
        session = Session.getInstance(UpcomingSurveyDetailsActivity.this);
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        surveyId = getIntent().getStringExtra("id");
        surveyor_id = getIntent().getStringExtra("surveyor_id");
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imgEditOperator = findViewById(R.id.imgEditOperator);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtSurveyNumber = findViewById(R.id.txtSurveyNumber);
        edtAgentDetails = findViewById(R.id.edtAgentDetails);
        txtSurveyor = findViewById(R.id.txtSurveyor);
        llBodyView = findViewById(R.id.llBodyView);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtPrice = findViewById(R.id.txtPrice);
        txtDate = findViewById(R.id.txtDate);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtSurveyorCompany = findViewById(R.id.txtSurveyorCompany);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        txtPort = findViewById(R.id.txtPort);
        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        btnReportAccept = findViewById(R.id.btnReportAccept);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtFileName = findViewById(R.id.txtFileName);
        txtVesselName = findViewById(R.id.txtVesselName);
        txtViewFile = findViewById(R.id.txtViewFile);
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

        llReportAccept = findViewById(R.id.llReportAccept);
        llReport = findViewById(R.id.llReport);
        llInvoice = findViewById(R.id.llInvoice);

        btnDownloadReport = findViewById(R.id.btnDownloadReport);
        btnViewReport = findViewById(R.id.btnViewReport);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        btnDownloadInvoice = findViewById(R.id.btnDownloadInvoice);

        imvBack.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        edtAgentDetails.setOnClickListener(this);
        btnReportAccept.setOnClickListener(this);
        btnAcceptBidding.setOnClickListener(this);
        imgEditOperator.setOnClickListener(this);
        llDownloadFile.setOnClickListener(this);
        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);
        btnViewInvoice.setOnClickListener(this);
        btnDownloadInvoice.setOnClickListener(this);


        isVisibleAgentDetails = session.getUserData().getType();
        if (isVisibleAgentDetails.equals("0")) {
            edtAgentDetails.setVisibility(View.VISIBLE);
        }
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void callOperatorListApi() {
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
                    userList.clear();
                    for (SurveyUserDataItem userData : userDataItemList) {
                        userList.add(userData.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<SurveyUsersListResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
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
                Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
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
            case R.id.imgEditOperator:
                callOperatorListDialog("Select Operator", userDataItemList, userList);
                break;
            case R.id.btnViewReport:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(reportURL));
                startActivity(intent);
                break;
            case R.id.btnDownloadReport:
                downloadReport(reportURL);
                break;
            case R.id.btnViewInvoice:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(invoiceURL));
                startActivity(intent1);
                break;
            case R.id.btnDownloadInvoice:
                downloadReport(invoiceURL);
                break;
            case R.id.txtViewFile:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uploadedFile));
                startActivity(i);
                break;

        }
    }

    private void callOperatorListDialog(String title, List<SurveyUserDataItem> userDataList, List<String> arrayList) {
        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_surveyor_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        Button btnUpdateSurveyor = view1.findViewById(R.id.btnUpdateSurveyor);
        Button btnCancel = view1.findViewById(R.id.btnCancel);
        TextView txtNoRecordFound = view1.findViewById(R.id.txtNoRecordFound);
        TextView txtTitle = view1.findViewById(R.id.txtTitle);
        txtTitle.setText(title);
        //  lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);

        SurveyorListAdapter surveyorListAdapter = new SurveyorListAdapter(this, R.layout.adapter_spinner_row, userDataList);
        lv.setAdapter(surveyorListAdapter);

        if (userDataList.size() == 0) {
            txtNoRecordFound.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        } else {
            txtNoRecordFound.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }
        lv.setOnItemClickListener((parent, view, position, id) -> {
            operatorId = userDataList.get(position).getId();
            for (SurveyUserDataItem userDataItem : userDataList) {
                userDataItem.setSelectedValue(false);
            }
            userDataList.get(position).setSelectedValue(true);
            surveyorListAdapter.notifyDataSetChanged();

        });
        btnUpdateSurveyor.setOnClickListener(v -> {

            if (operatorId.equals("")) {
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            } else {
                dialog.cancel();
                callChangeSurveyorApi(operatorId);
            }

        });

        btnCancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();

    }

    private void callChangeSurveyorApi(String operatorId) {

        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        hashMap.put("survey_id", survey_id);
        hashMap.put("operator_id", operatorId);
        Call<CommonResponse> call = apiService.assign_to_operator(hashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() != null) {
                    CommonResponse details = response.body();
                    if (details.getResponse().getStatus() == 1) {
                        callApi();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });


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


    private void downloadReport(String surveyReport) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("iMarS");
        builder.setMessage("Are you sure you want to Download file?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiToDownloadReport(surveyReport);
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
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("iMars");
        request.setDescription("iMars File downloading...");
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/iMars/" + "/" + url);
        refid = downloadManager.enqueue(request);
        list.add(refid);

    }

    private void callListUserBidding() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select User");
        builder.setSingleChoiceItems(bidingUserArrayList.toArray(new String[bidingUserArrayList.size()]), selectedItemPosition, (dialog, which) -> {
            selectedItemPosition = which;

        });
        builder.setPositiveButton("Accept", (dialog, which) -> {
            if (selectedItemPosition > -1) {
                callAcceptBidding(survey_id);
            }

        });
        builder.setNegativeButton("Clear", (dialog, which) -> {
        });
        builder.show();

    }

    private void callAcceptBidding(String survey_id) {
        Util.showProDialog(this);
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("survey_id", survey_id);
        RxService apiService = App.getClient().create(RxService.class);
        Call<SurveyResponse> call = apiService.operator_custom_bidding_accept(myHashMap);
        call.enqueue(new Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                Util.dismissProDialog();
                if (response.body() != null) {
                    SurveyResponse listResponse = response.body();

                    if (listResponse.getResponse().getStatus() == 1) {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);

                        Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, OperatorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                       /* Intent intent = new Intent();
                        intent.putExtra("responseData", listResponse.getResponse().getSurveyData());
                        setResult(RESULT_OK, intent);
                        finish();*/
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
                Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                                Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                           /*     Intent intent = new Intent();
                                intent.putExtra("responseData", listResponse.getResponse().getSurveyData());
                                setResult(RESULT_OK, intent);
                                finish();*/

                                Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, OperatorHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SurveyResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this);
        builder.setMessage("Are you sure you want to cancel this survey? Cancelling too many surveys may result in penalties. Do you want to proceed?");
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
        Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this);
                        builder.setMessage("You have successfully cancelled this survey. The other party will be notified.");
                        builder.show();
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, OperatorHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });

                    } else {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);

                    }
                }

            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });


    }

/*    private void callAcceptAndDeclineApi(String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                        Intent intent = new Intent();
                        intent.putExtra("responseData", details.getResponse().getSurveyData());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);

                    }
                }

            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }*/

    private void edtAgentDetailsDialog(String title, TextView txtAgentName, TextView txtAgentContact, TextView txtAgentEmail, List<String> portList) {

        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_agent_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this)
                .setTitle(title)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UpcomingSurveyDetailsActivity.this, R.layout.adapter_spinner_row, portList);
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
        Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                        Util.showSuccessSnackBar(coordinatorLayout, "" + listResponse.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                    }
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Util.dismissProDialog();
                Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        llBodyView.setVisibility(View.GONE);
        if (Util.hasInternet(UpcomingSurveyDetailsActivity.this)) {
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
                    Toast.makeText(UpcomingSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();
        }

    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this);
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
            if (details.getResponse().getData().get(0).getFileData().equals("")) {
                txtInstructionAndDocument.setVisibility(View.GONE);
                cardViewInstructionAndDocument.setVisibility(View.GONE);
            } else {
                txtInstruction.setText(details.getResponse().getData().get(0).getInstruction());
                txtFileName.setText("doc file");
            }

            if (details.getResponse().getData().get(0).getOperator_id().equals(session.getUserData().getUserId())) {
                Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getStatus());
                if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                    edtAgentDetails.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnReportAccept.setVisibility(View.GONE);
                    btnDownloadReport.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                    btnCancel.setVisibility(View.VISIBLE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    btnDownloadReport.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);

                    if (session.getUserData().getType().equals("0")) {
                        imgEditOperator.setVisibility(View.VISIBLE);
                    } else {
                        imgEditOperator.setVisibility(View.GONE);
                    }

                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                    btnCancel.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    btnDownloadReport.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                    btnCancel.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.VISIBLE);
                    btnDownloadReport.setVisibility(View.VISIBLE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    llReportAccept.setVisibility(View.VISIBLE);
                    llReport.setVisibility(View.VISIBLE);
                    llInvoice.setVisibility(View.GONE);
                }

            } else {
                if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                    edtAgentDetails.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                    btnCancel.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.VISIBLE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                    if (session.getUserData().getType().equals("0")) {
                        imgEditOperator.setVisibility(View.VISIBLE);
                    } else {
                        imgEditOperator.setVisibility(View.GONE);
                    }
                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                    Log.d("1234", "setSurveyDetailsData: 123");
                    btnCancel.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                    Log.d("1234", "setSurveyDetailsData: 123");
                    btnCancel.setVisibility(View.GONE);
                    edtAgentDetails.setVisibility(View.GONE);
                    btnReportAccept.setVisibility(View.GONE);
                    imgEditOperator.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    llReportAccept.setVisibility(View.VISIBLE);
                    llReport.setVisibility(View.VISIBLE);
                    llInvoice.setVisibility(View.GONE);
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


            surveyReport = details.getResponse().getData().get(0).getReportURL();
            uploadedFile = details.getResponse().getData().get(0).getFileData();
            operator_id = details.getResponse().getData().get(0).getOperator_id();
            survey_type = details.getResponse().getData().get(0).getSurveycateName();
            survey_id = details.getResponse().getData().get(0).getId();
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtSurveyor.setText(details.getResponse().getData().get(0).getSurveyorsName());
            if (details.getResponse().getData().get(0).getTransportation_cost().equals("")) {
                txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing());

            } else {
                txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing() + "+ $" + details.getResponse().getData().get(0).getTransportation_cost() + " Transportation Cost");

            }
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
            txtAgentContact.setText(details.getResponse().getData().get(0).getAgentsmobile());
            txtAgentName.setText(details.getResponse().getData().get(0).getAgentName());
            txtSurveyorCompany.setText(details.getResponse().getData().get(0).getSurveyor_company());
            reportURL = details.getResponse().getData().get(0).getReportURL();
            invoiceURL = details.getResponse().getData().get(0).getInvoice_url();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

}


