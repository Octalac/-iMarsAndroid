package com.os.imars.surveyor.activity;

import android.app.DownloadManager;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
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
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastSurveyDetailsActivity extends BaseActivity {

    private ImageView imvBack;
    private Button btnSurveyComplete, btnAddRating, btnViewReport, btnDownloadReport, btnViewInvoice, btnDownloadInvoice;
    private String surveyId = "", surveyor_id = "";
    private TextView txtCompanyName, txtOperatorName, txtViewFile, txtCompanyWebsite, txtNoOfSurvey, txtAverageInvoicePaymentTime, txtOperatorCountry, txtSurveyNumber, txtSurveyor, txtPrice, txtDate, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtVesselAddress, txtVesselEmail, txtSurveyorCompany, txtAgentEmail, txtAgentContact, txtAgentName, txtInstructionAndDocument, txtSurveyStatus;
    private CardView cardViewInstructionAndDocument, cardViewReportAndInvoice;
    private ScrollView llBodyView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Session session;
    private String isVisibleAgentDetails = "", agentId = "", comment = "", surveyorId = "", operatorId = "", uploadedFile = "", invoiceURL = "", reportURL = "";
    private SurveyRequestDetails surveyRequestDetails;
    private CoordinatorLayout coordinatorLayout;
    private RatingBar ratingBar;
    private static final int MEGABYTE = 1024 * 1024;
    private LinearLayout llInvoice, llReport;
    private RelativeLayout llDownloadFile;
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
        txtDate = findViewById(R.id.txtDate);
        ratingBar = findViewById(R.id.ratingBar);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        btnAddRating = findViewById(R.id.btnAddRating);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtViewFile = findViewById(R.id.txtViewFile);
        txtPort = findViewById(R.id.txtPort);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtFileName = findViewById(R.id.txtFileName);
        txtVesselName = findViewById(R.id.txtVesselName);
        txtSurveyorCompany = findViewById(R.id.txtSurveyorCompany);
        cardViewInstructionAndDocument = findViewById(R.id.cardViewInstructionAndDocument);
        txtInstructionAndDocument = findViewById(R.id.txtInstructionAndDocument);
        txtIMO = findViewById(R.id.txtIMO);

        btnViewReport = findViewById(R.id.btnViewReport);
        btnDownloadReport = findViewById(R.id.btnDownloadReport);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        btnDownloadInvoice = findViewById(R.id.btnDownloadInvoice);


        txtVesselCompanyName = findViewById(R.id.txtVesselCompanyName);
        txtVesselAddress = findViewById(R.id.txtVesselAddress);
        txtVesselEmail = findViewById(R.id.txtVesselEmail);
        txtAgentContact = findViewById(R.id.txtAgentContact);
        txtAgentName = findViewById(R.id.txtAgentName);
        txtAgentEmail = findViewById(R.id.txtAgentEmail);
        btnSurveyComplete = (Button) findViewById(R.id.btnSurveyComplete);
        txtCompanyName = (TextView) findViewById(R.id.txtCompanyName);
        txtOperatorName = (TextView) findViewById(R.id.txtOperatorName);
        txtCompanyWebsite = (TextView) findViewById(R.id.txtCompanyWebsite);
        txtNoOfSurvey = (TextView) findViewById(R.id.txtNoOfSurvey);
        txtAverageInvoicePaymentTime = (TextView) findViewById(R.id.txtAverageInvoicePaymentTime);
        txtOperatorCountry = (TextView) findViewById(R.id.txtOperatorCountry);

        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        llInvoice = findViewById(R.id.llInvoice);
        llReport = findViewById(R.id.llReport);

        imvBack.setOnClickListener(this);
        btnSurveyComplete.setOnClickListener(this);
        btnAddRating.setVisibility(View.GONE);
        llDownloadFile.setOnClickListener(this);
        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);
        btnViewInvoice.setOnClickListener(this);
        btnDownloadInvoice.setOnClickListener(this);
        txtViewFile.setOnClickListener(this);

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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
                    llBodyView.setVisibility(View.VISIBLE);
                    if (response.body() != null) {
                        surveyRequestDetails = response.body();
                        if (surveyRequestDetails.getResponse().getStatus() == 1) {
                            setSurveyDetailsData(surveyRequestDetails);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SurveyRequestDetails> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Toast.makeText(PastSurveyDetailsActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    Util.hideShimmer(shimmerFrameLayout);
                    llBodyView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Util.hideShimmer(shimmerFrameLayout);
            llBodyView.setVisibility(View.VISIBLE);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
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

            if (details.getResponse().getData().get(0).getSurveyor_id().equals(session.getUserData().getUserId())) {
                Log.d("1234", "setSurveyDetailsData: 1" + details.getResponse().getData().get(0).getStatus());


                if (details.getResponse().getData().get(0).getStatus().equals("4")) {
                    Log.d("1234", "setSurveyDetailsData: 2");
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("5")) {
                    Log.d("1234", "setSurveyDetailsData:3 ");
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    if (!details.getResponse().getData().get(0).getSurveyor_rating().equals("0")) {
                        Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getSurveyor_rating());
                        ratingBar.setVisibility(View.VISIBLE);
                        ratingBar.setRating(Float.parseFloat(details.getResponse().getData().get(0).getSurveyor_rating()));
                    }

                } else if (details.getResponse().getData().get(0).getStatus().equals("6")) {
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                    if (!details.getResponse().getData().get(0).getSurveyor_rating().equals("0")) {
                        Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getSurveyor_rating());
                        ratingBar.setVisibility(View.VISIBLE);
                        ratingBar.setRating(Float.parseFloat(details.getResponse().getData().get(0).getSurveyor_rating()));
                    }
                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                }

            } else {
                if (details.getResponse().getData().get(0).getStatus().equals("4")) {
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("5")) {
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("6")) {
                    Log.d("1234", "setSurveyDetailsData: 123");
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    ratingBar.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                    btnSurveyComplete.setVisibility(View.GONE);
                    btnAddRating.setVisibility(View.GONE);
                    cardViewReportAndInvoice.setVisibility(View.GONE);
                }
            }

            switch (details.getResponse().getData().get(0).getStatus()) {
                case "4":
                    //  holder.tvStatus.setBackgroundResource(R.drawable.bg_round_button_fill_color);
                    txtSurveyStatus.setText("Pending Payment");
                    break;
                case "5":
                    //  holder.tvStatus.setBackgroundResource(R.drawable.bg_blue_fill_color);
                    txtSurveyStatus.setText("Payment Received");
                    break;
                case "6":
                    //  holder.tvStatus.setBackgroundResource(R.drawable.bg_round_button_fill_color);
                    txtSurveyStatus.setText("Paid");
                    break;
                case "2":
                    //  holder.tvStatus.setBackgroundResource(R.drawable.bg_round_button_fill_color);
                    txtSurveyStatus.setText("Cancel");
                    break;

            }

            uploadedFile = details.getResponse().getData().get(0).getFileData();
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

            txtCompanyName.setText(details.getResponse().getData().get(0).getOperator_company());
            txtCompanyWebsite.setText(details.getResponse().getData().get(0).getOperator_company_website());
            txtNoOfSurvey.setText(details.getResponse().getData().get(0).getOperator_survey_count());
            txtOperatorCountry.setText(details.getResponse().getData().get(0).getOperator_country_name());
            reportURL = details.getResponse().getData().get(0).getReportURL();
            invoiceURL = details.getResponse().getData().get(0).getInvoice_url();
            txtSurveyorCompany.setText(details.getResponse().getData().get(0).getSurveyor_company());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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


}
