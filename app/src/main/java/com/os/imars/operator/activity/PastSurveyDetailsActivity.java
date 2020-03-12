package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastSurveyDetailsActivity extends BaseActivity {

    private ImageView imvBack;
    private Button btnSurveyComplete, btnAddRating, btnViewReport, btnDownloadReport, btnViewInvoice, btnDownloadInvoice;
    private String surveyId = "", surveyor_id = "";
    private TextView txtOperatorDetails, txtViewFile, txtSurveyNumber, txtSurveyor, txtPrice, txtDate, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtSurveyorCompany, txtVesselAddress, txtNoDataFound, txtVesselEmail, txtAgentEmail, txtAgentContact, txtAgentName, txtInstructionAndDocument, txtSurveyStatus;
    private CardView cardViewInstructionAndDocument, cardViewOperatorDetails, cardViewReportAndInvoice;
    private ScrollView llBodyView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Session session;
    private String isVisibleAgentDetails = "", agentId = "", comment = "", surveyorId = "", operatorId = "", uploadedFile = "", surveyReport = "", invoiceURL = "", reportURL = "";
    private SurveyRequestDetails surveyRequestDetails;
    private CoordinatorLayout coordinatorLayout;
    private RatingBar ratingBar;
    private LinearLayout llInvoice, llReport;
    private RelativeLayout llDownloadFile;
    private static final int MEGABYTE = 1024 * 1024;
    long refid;
    ArrayList<Long> list = new ArrayList<>();


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
        setContentView(R.layout.activity_past_survey_details);
        initView();
        callApi();
    }

    private void initView() {
        session = Session.getInstance(this);
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        surveyId = getIntent().getStringExtra("id");
        surveyor_id = getIntent().getStringExtra("surveyor_id");
        imvBack = (ImageView) findViewById(R.id.imvBack);
        txtSurveyNumber = findViewById(R.id.txtSurveyNumber);
        txtSurveyor = findViewById(R.id.txtSurveyor);
        llBodyView = findViewById(R.id.llBodyView);
        txtPrice = findViewById(R.id.txtPrice);
        ratingBar = findViewById(R.id.ratingBar);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtDate = findViewById(R.id.txtDate);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtOperatorDetails = findViewById(R.id.txtOperatorDetails);
        txtPort = findViewById(R.id.txtPort);
        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        cardViewOperatorDetails = findViewById(R.id.cardViewOperatorDetails);
        txtFileName = findViewById(R.id.txtFileName);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        btnAddRating = findViewById(R.id.btnAddRating);
        txtVesselName = findViewById(R.id.txtVesselName);
        cardViewInstructionAndDocument = findViewById(R.id.cardViewInstructionAndDocument);
        txtInstructionAndDocument = findViewById(R.id.txtInstructionAndDocument);
        txtIMO = findViewById(R.id.txtIMO);
        txtViewFile = findViewById(R.id.txtViewFile);
        txtVesselCompanyName = findViewById(R.id.txtVesselCompanyName);
        txtVesselAddress = findViewById(R.id.txtVesselAddress);
        txtVesselEmail = findViewById(R.id.txtVesselEmail);
        txtAgentContact = findViewById(R.id.txtAgentContact);
        txtAgentName = findViewById(R.id.txtAgentName);
        txtAgentEmail = findViewById(R.id.txtAgentEmail);
        btnSurveyComplete = (Button) findViewById(R.id.btnSurveyComplete);

        btnViewReport = findViewById(R.id.btnViewReport);
        btnDownloadReport = findViewById(R.id.btnDownloadReport);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        btnDownloadInvoice = findViewById(R.id.btnDownloadInvoice);
        txtSurveyorCompany = findViewById(R.id.txtSurveyorCompany);

        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        llInvoice = findViewById(R.id.llInvoice);
        llReport = findViewById(R.id.llReport);


        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);
        btnViewInvoice.setOnClickListener(this);
        btnDownloadInvoice.setOnClickListener(this);
        txtViewFile.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        btnSurveyComplete.setOnClickListener(this);
        btnAddRating.setOnClickListener(this);
        llDownloadFile.setOnClickListener(this);
        txtOperatorDetails.setVisibility(View.GONE);
        cardViewOperatorDetails.setVisibility(View.GONE);


        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(PastSurveyDetailsActivity.this)) {
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
                        llBodyView.setVisibility(View.VISIBLE);
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
                    Toast.makeText(PastSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            noNetworkConnection();

        }

    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PastSurveyDetailsActivity.this);
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

        if (details != null) {

            if (details.getResponse().getData().size() != 0) {
                Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getStatus());
                if (details.getResponse().getData().get(0).getOperator_id().equals(session.getUserData().getUserId())) {
                    Log.d("1234", "setSurveyDetailsData: 1" + details.getResponse().getData().get(0).getStatus());
                    if (details.getResponse().getData().get(0).getStatus().equals("4")) {
                        Log.d("1234", "setSurveyDetailsData: 2");
                        btnSurveyComplete.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                        if (details.getResponse().getData().get(0).getSurveyor_rating().equals("0")) {
                            Log.d("1234", "setSurveyDetailsData: 4");
                            btnAddRating.setVisibility(View.VISIBLE);
                        } else {
                            btnAddRating.setVisibility(View.GONE);
                        }
                    } else if (details.getResponse().getData().get(0).getStatus().equals("5")) {
                        Log.d("1234", "setSurveyDetailsData:3 ");
                        btnSurveyComplete.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                        btnAddRating.setVisibility(View.GONE);
                    } else if (details.getResponse().getData().get(0).getStatus().equals("6")) {
                        btnSurveyComplete.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                        btnAddRating.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                    }
                } else {
                    if (details.getResponse().getData().get(0).getStatus().equals("4")) {
                        btnSurveyComplete.setVisibility(View.GONE);
                        btnAddRating.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                        if (details.getResponse().getData().get(0).getSurveyor_rating().equals("0")) {
                            btnAddRating.setVisibility(View.VISIBLE);
                        } else {
                            btnAddRating.setVisibility(View.GONE);
                        }
                    } else if (details.getResponse().getData().get(0).getStatus().equals("5")) {
                        btnSurveyComplete.setVisibility(View.GONE);
                        btnAddRating.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    } else if (details.getResponse().getData().get(0).getStatus().equals("6")) {
                        btnSurveyComplete.setVisibility(View.GONE);
                        btnAddRating.setVisibility(View.GONE);
                        ratingBar.setVisibility(View.GONE);
                        cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    }
                }
                if (details.getResponse().getData().get(0).getFileData().equals("")) {
                    txtInstructionAndDocument.setVisibility(View.GONE);
                    cardViewInstructionAndDocument.setVisibility(View.GONE);
                } else {
                    txtInstruction.setText(details.getResponse().getData().get(0).getInstruction());
                    txtFileName.setText("doc file");
                }

                switch (details.getResponse().getData().get(0).getStatus()) {
                    case "2":
                        txtSurveyStatus.setText("Cancelled");
                        break;
                    case "4":
                        txtSurveyStatus.setText("Unpaid");
                        break;
                    case "5":
                        txtSurveyStatus.setText("Paid");
                        break;
                    case "6":
                        txtSurveyStatus.setText("Paid");
                        break;
                }

                surveyorId = details.getResponse().getData().get(0).getSurveyor_id();
                operatorId = details.getResponse().getData().get(0).getOperator_id();
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
                txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
                txtAgentContact.setText(details.getResponse().getData().get(0).getAgentsmobile());
                txtAgentName.setText(details.getResponse().getData().get(0).getAgentName());
                reportURL = details.getResponse().getData().get(0).getReportURL();
                invoiceURL = details.getResponse().getData().get(0).getInvoice_url();
                txtSurveyorCompany.setText(details.getResponse().getData().get(0).getSurveyor_company());
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAddRating:
                addFeedback();
                break;
            case R.id.llDownloadFile:
                downloadFile(uploadedFile);
                break;
            case R.id.btnViewReport:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(reportURL));
                startActivity(intent);
                break;
            case R.id.btnDownloadReport:
                downloadFile(reportURL);
                break;
            case R.id.btnViewInvoice:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(invoiceURL));
                startActivity(intent1);

                break;
            case R.id.btnDownloadInvoice:
                downloadFile(invoiceURL);
                break;
            case R.id.txtViewFile:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uploadedFile));
                startActivity(i);
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


    private void callApiToDownloadReport(String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
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


    private void addFeedback() {
        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_dialog_submit_feedback, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        RatingBar ratingBar = view1.findViewById(R.id.ratingBar);
        EditText edtComment = view1.findViewById(R.id.edtComment);
        Button btnSubmit = view1.findViewById(R.id.btnSubmit);
        Button btnCancel = view1.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float getRating = ratingBar.getRating();
                comment = edtComment.getText().toString();
                if (getRating == 0) {
                    Toast.makeText(PastSurveyDetailsActivity.this, "Please Add Rating", Toast.LENGTH_SHORT).show();
                } else {
                    String rating = String.valueOf(getRating);
                    callApiAddFeedback(comment, rating);
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.cancel();
            }
        });

        alertDialog.show();

    }


    private void callApiAddFeedback(String comment, String rating) {
        Util.showProDialog(this);
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("operator_id", operatorId);
        hashMap.put("surveyor_id", surveyor_id);
        hashMap.put("survey_id", surveyId);
        hashMap.put("rating", rating);
        hashMap.put("comment", comment);
        Log.d("1234", "callApiAddFeedback: " + Arrays.asList(hashMap));
        Call<CommonResponse> call = apiService.addRating(hashMap);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                Util.dismissProDialog();
                llBodyView.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    if (response.body().getResponse().getStatus() == 1) {
                        Toast.makeText(PastSurveyDetailsActivity.this, "Feedback Submitted Successully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PastSurveyDetailsActivity.this, OperatorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
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
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);

    }
}
