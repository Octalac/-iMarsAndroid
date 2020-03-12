package com.os.imars.surveyor.activity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CustomSliderButton;
import com.os.imars.dao.mySurvey.SurveyResponse;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.utility.FilePickerUtility;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingCustomSurveyDetailsActivity extends BaseActivity implements FilePickerUtility.ImagePickerCallback {


    private ImageView imvBack;
    private TextView tvDecline, txtSlideRightSide, txtSurveyStatus, txtSubmitReport;
    private TextView txtSurveyNumber, txtSurveyor, txtPrice, txtDate, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtVesselAddress, txtUploadStatus, txtUploadFile, txtNoDataFound, txtVesselEmail, txtAgentEmail, txtAgentContact, txtAgentName, txtInstructionAndDocument, txtAverageInvoicePaymentTime, txtOperatorCountry, txtNoOfSurvey, txtCompanyWebsite, txtOperatorName, txtCompanyName;
    private CardView cardViewInstructionAndDocument, cardViewUploadFile, cardViewReportAndInvoice;
    private CustomSliderButton btnSlideToAccept;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String id = "", survey_id = "", uploadedFile = "", reportUrl = "";
    private ScrollView llBodyView;
    private RelativeLayout rlBottomSlide;
    private Session session;
    private CoordinatorLayout coordinatorLayout;
    private Button btnUploadFile;
    private File file;
    private FilePickerUtility filePickerUtility;
    private String surveyType = "", amount = "", survey_category_type = "";
    private EditText edtNoDay;
    private static final int MEGABYTE = 1024 * 1024;
    private RelativeLayout llDownloadFile, rlBottomView, rlbtnCancel;
    private RelativeLayout llSurveyorDetails;
    private LinearLayout llInvoice;
    private Button btnViewReport, btnDownloadReport;
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
        setContentView(R.layout.activity_upcoming_custom_surveyor_survey__details);
        initView();
        callApi();
    }

    private void initView() {
        id = getIntent().getStringExtra("id");
        Log.d("1234", "initView: ");
        session = Session.getInstance(UpcomingCustomSurveyDetailsActivity.this);


        btnViewReport = findViewById(R.id.btnViewReport);
        btnDownloadReport = findViewById(R.id.btnDownloadReport);


        shimmerFrameLayout = findViewById(R.id.shimmerView);
        imvBack = findViewById(R.id.imvBack);
        tvDecline = findViewById(R.id.tvDecline);
        btnSlideToAccept = findViewById(R.id.btnSlideToAccept);
        txtSlideRightSide = findViewById(R.id.txtSlideRightSide);
        txtSurveyNumber = findViewById(R.id.txtSurveyNumber);
        cardViewUploadFile = findViewById(R.id.cardViewUploadFile);
        txtUploadFile = findViewById(R.id.txtUploadFile);
        edtNoDay = findViewById(R.id.edtNoDay);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        txtUploadStatus = findViewById(R.id.txtUploadStatus);
        llSurveyorDetails = findViewById(R.id.llSurveyorDetails);
        filePickerUtility = new FilePickerUtility(this, this);
        txtAverageInvoicePaymentTime = findViewById(R.id.txtAverageInvoicePaymentTime);
        txtOperatorCountry = findViewById(R.id.txtOperatorCountry);
        txtNoOfSurvey = findViewById(R.id.txtNoOfSurvey);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        txtOperatorName = findViewById(R.id.txtOperatorName);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyWebsite = findViewById(R.id.txtCompanyWebsite);
        txtSubmitReport = findViewById(R.id.txtSubmitReport);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        rlBottomView = findViewById(R.id.rlBottomView);
        txtSurveyor = findViewById(R.id.txtSurveyor);
        llBodyView = findViewById(R.id.llBodyView);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        rlBottomSlide = findViewById(R.id.rlBottomSlide);
        txtPrice = findViewById(R.id.txtPrice);
        txtDate = findViewById(R.id.txtDate);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtPort = findViewById(R.id.txtPort);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtFileName = findViewById(R.id.txtFileName);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtVesselName = findViewById(R.id.txtVesselName);
        cardViewInstructionAndDocument = findViewById(R.id.cardViewInstructionAndDocument);
        txtInstructionAndDocument = findViewById(R.id.txtInstructionAndDocument);
        txtIMO = findViewById(R.id.txtIMO);
        txtAgentContact = findViewById(R.id.txtAgentContact);
        txtAgentName = findViewById(R.id.txtAgentName);
        txtAgentEmail = findViewById(R.id.txtAgentEmail);
        rlbtnCancel = findViewById(R.id.rlbtnCancel);
        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        llInvoice = findViewById(R.id.llInvoice);

        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);

        imvBack.setOnClickListener(this);
        tvDecline.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);
        txtSubmitReport.setOnClickListener(this);
        llDownloadFile.setOnClickListener(this);
        btnSlideToAccept.setOnUnlockListener(() -> {
            txtSlideRightSide.setVisibility(View.VISIBLE);
            String type = "accept";
            if (surveyType.equals("Custom Occasional Survey")) {
                enterBidPopUp();
            } else {
                callAcceptAndDeclineApi(type);
            }

        });
        btnSlideToAccept.setOnLockListener(() -> txtSlideRightSide.setVisibility(View.GONE));

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void enterBidPopUp() {
        Log.d("1234", "enterBidPopUp: ");
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dailog_enter_bid, null, false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();
        ImageView imgViewClose = view.findViewById(R.id.imgViewClose);
        EditText edtAmount = view.findViewById(R.id.edtAmount);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSend = view.findViewById(R.id.btnSend);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String type = "accept";
                amount = edtAmount.getText().toString();
                callCustomAcceptAndDeclineApi(type, surveyType, amount);
            }
        });
        imgViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.tvDecline:
                declineDialog();
                break;
            case R.id.btnUploadFile:
                uploadFile();
                break;
            case R.id.txtSubmitReport:
                callApiUploadFile();
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

    private void callApiUploadFile() {

        MultipartBody.Part body = null;
        RequestBody no_of_days = null;
        if (file == null) {
            Toast.makeText(this, "Upload File", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody surveyorId = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
            RequestBody surveyId = RequestBody.create(MediaType.parse("multipart/form-data"), survey_id);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            String noOfDay = edtNoDay.getText().toString();
            no_of_days = RequestBody.create(MediaType.parse("multipart/form-data"), noOfDay);

             /*  if (noOfDay.isEmpty()){
                Toast.makeText(this, "Enter Day", Toast.LENGTH_SHORT).show();
            }else{
                 no_of_days = RequestBody.create(MediaType.parse("multipart/form-data"), noOfDay);
            }*/


            Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
            RxService apiService = App.getClient().create(RxService.class);
            Call<SurveyResponse> call = apiService.report_submit(surveyId, surveyorId, no_of_days, body);
            call.enqueue(new Callback<SurveyResponse>() {
                @Override
                public void onResponse(Call<SurveyResponse> call, Response<SurveyResponse> response) {
                    Util.dismissProDialog();
                    if (response.body() != null) {
                        SurveyResponse details = response.body();
                        if (details.getResponse() != null) {
                            if (details.getResponse().getStatus() == 1) {
                                Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingCustomSurveyDetailsActivity.this);
                             /*   Intent intent = new Intent();
                                intent.putExtra("responseData", details.getResponse().getSurveyData());
                                setResult(RESULT_OK, intent);
                                finish();
*/
                                Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, SurveyorHomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<SurveyResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                }
            });

        }

    }

    private void uploadFile() {
        filePickerUtility.chooseFileFromGallary();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (data != null) {
                filePickerUtility.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @Override
    public void onImagePickSuccess() {
        file = filePickerUtility.getImageFile();
        txtUploadStatus.setText("File Uploaded.");
    }

    @Override
    public void onImagePickError(String message) {
    }

    @Override
    public void onFilePickerSize(long size) {
        Log.d("1234", "onFilePickerSize: " + size);
        if (size >= 5) {
            Util.showErrorSnackBar(coordinatorLayout, "The file is too large, please upload the another file.", UpcomingCustomSurveyDetailsActivity.this);
        }
    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<SurveyRequestDetails> call = apiService.survey_details(hashMap);
        call.enqueue(new Callback<SurveyRequestDetails>() {
            @Override
            public void onResponse(Call<SurveyRequestDetails> call, Response<SurveyRequestDetails> response) {
                Util.hideShimmer(shimmerFrameLayout);
                if (response.body() != null) {
                    SurveyRequestDetails details = response.body();
                    if (details.getResponse().getStatus() == 1) {
                        llBodyView.setVisibility(View.VISIBLE);
                        txtNoDataFound.setVisibility(View.GONE);
                        setSurveyDetailsData(details);
                    } else {
                        llBodyView.setVisibility(View.GONE);
                        txtNoDataFound.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onFailure(Call<SurveyRequestDetails> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        /*    progressDialog = new ProgressDialog(UpcomingSurveyDetailsActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Downloading file...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }


        private File getFileDirectory() {
            File outputDir = null;
            String externalStorageStagte = Environment.getExternalStorageState();
            if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
                File photoDir = Environment.getExternalStorageDirectory();
                outputDir = new File(photoDir.getAbsolutePath(), UpcomingCustomSurveyDetailsActivity.this.getString(R.string.app_name));
                if (!outputDir.exists())
                    if (!outputDir.mkdirs()) {
                        outputDir = null;
                    }
            }
            return outputDir;
        }


        @Override
        protected String doInBackground(String... strings) {

            String fileUrl = strings[0];
            File folder = getFileDirectory();

            Log.d("1234", "doInBackground: " + fileUrl);
            Log.d("1234", "doInBackground: " + folder);

            File pdfFile = new File(folder, "" + fileUrl);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                long total = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    total += bufferLength;
                    //  publishProgress("" + (int) ((total * 100) / totalSize));
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

  /*      protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }*/

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //  progressDialog.cancel();
            Toast.makeText(getApplicationContext(), "Download file successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSurveyDetailsData(SurveyRequestDetails details) {
        if (details.getResponse().getData().size() != 0) {
            Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getSurveyorsName() + "port" + details.getResponse().getData().get(0).getPort());

            llBodyView.setVisibility(View.VISIBLE);
            survey_id = details.getResponse().getData().get(0).getId();
            surveyType = details.getResponse().getData().get(0).getSurveycateName();


            if (details.getResponse().getData().get(0).getFileData().equals("")) {
                txtInstructionAndDocument.setVisibility(View.GONE);
                cardViewInstructionAndDocument.setVisibility(View.GONE);
            } else {
                txtInstruction.setText(details.getResponse().getData().get(0).getInstruction());
                txtFileName.setText("doc file");
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


            //   if (details.getResponse().getData().get(0).getSurveyor_id().equals(session.getUserData().getUserId())) {

            if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                Log.d("1234", "setSurveyDetailsData: 12 ");
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
                if (details.getResponse().getData().get(0).getBid_accept_status().equals("0") || details.getResponse().getData().get(0).getBid_status().equals("0")) {
                    Log.d("1234", "setSurveyDetailsData:4 ");
                    rlBottomSlide.setVisibility(View.VISIBLE);
                    tvDecline.setVisibility(View.VISIBLE);
                } else {
                    Log.d("1234", "setSurveyDetailsData:5");
                    rlBottomSlide.setVisibility(View.GONE);
                    tvDecline.setVisibility(View.GONE);

                }

            } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlbtnCancel.setVisibility(View.VISIBLE);
                rlBottomSlide.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.VISIBLE);
                cardViewUploadFile.setVisibility(View.VISIBLE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.VISIBLE);
                cardViewUploadFile.setVisibility(View.VISIBLE);
                cardViewReportAndInvoice.setVisibility(View.GONE);

  /*              if (session.getUserData().getType().equals("2")) {
                    imgEditSurveyor.setVisibility(View.VISIBLE);
                    imgEditDate.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    rlBottomView.setVisibility(View.GONE);
                    rlbtnCancel.setVisibility(View.VISIBLE);
                } else if (session.getUserData().getType().equals("3")) {
                    imgEditSurveyor.setVisibility(View.GONE);
                    imgEditDate.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    rlBottomView.setVisibility(View.GONE);
                    rlbtnCancel.setVisibility(View.VISIBLE);
                }
                */

            } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlBottomSlide.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
            } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlBottomSlide.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                rlbtnCancel.setVisibility(View.GONE);
                llInvoice.setVisibility(View.GONE);
            }

            if (details.getResponse().getData().get(0).getBid_status().equals("1")) {
                rlBottomSlide.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.VISIBLE);
            }


            if (survey_category_type.equals("daily")) {
                edtNoDay.setVisibility(View.VISIBLE);
            } else {
                edtNoDay.setVisibility(View.GONE);
            }

            if (details.getResponse().getData().get(0).getSurveyor_id().equals("") || details.getResponse().getData().get(0).getSurveyor_id() == null) {
                llSurveyorDetails.setVisibility(View.GONE);
            } else {
                llSurveyorDetails.setVisibility(View.VISIBLE);
            }

            txtAgentEmail.setText(details.getResponse().getData().get(0).getAgentsemail());
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtSurveyor.setText(details.getResponse().getData().get(0).getSurveyorsName());
            txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing());
            txtDate.setText(Util.parseDateToddMMyyyy(details.getResponse().getData().get(0).getStartDate()) + " to " + Util.parseDateToddMMyyyy(details.getResponse().getData().get(0).getEndDate()));
            txtSurveyType.setText(details.getResponse().getData().get(0).getSurveycateName());
            txtPort.setText(details.getResponse().getData().get(0).getPort());
            txtOperator.setText(details.getResponse().getData().get(0).getOperator_name());
            txtVesselName.setText(details.getResponse().getData().get(0).getVesselsname());
            txtIMO.setText("#" + details.getResponse().getData().get(0).getImo_number());
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtAgentContact.setText(details.getResponse().getData().get(0).getAgentsmobile());
            txtAgentName.setText(details.getResponse().getData().get(0).getAgentName());
            txtAverageInvoicePaymentTime.setText(details.getResponse().getData().get(0).getOperator_average_invoice_payment_time());
            txtOperatorCountry.setText(details.getResponse().getData().get(0).getOperator_country_name());
            txtNoOfSurvey.setText(details.getResponse().getData().get(0).getOperator_survey_count());
            txtCompanyWebsite.setText(details.getResponse().getData().get(0).getOperator_company_website());
            txtOperatorName.setText(details.getResponse().getData().get(0).getOperator_company());
            txtCompanyName.setText(details.getResponse().getData().get(0).getOperator_company());
            reportUrl = details.getResponse().getData().get(0).getReportURL();
            uploadedFile = details.getResponse().getData().get(0).getFileData();

        }

    }

    private void declineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingCustomSurveyDetailsActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to decline this survey?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String type = "decline";
                callAcceptAndDeclineApi(type);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    private void callAcceptAndDeclineApi(String type) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("surveyors_id", session.getUserData().getUserId());
        hashMap.put("type", type);
        hashMap.put("survey_id", survey_id);
        Log.d("1234", "callAcceptAndDeclineApi: " + Arrays.asList(hashMap));
        Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
        RxService apiService = App.getClient().create(RxService.class);
        Call<SurveyResponse> call = apiService.custom_survey_accept_reject(hashMap);
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
                        Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, SurveyorHomeActivity.class);
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
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }


    private void callCustomAcceptAndDeclineApi(String type, String surveyType, String amount) {
        Util.showProDialog(UpcomingCustomSurveyDetailsActivity.this);
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("surveyors_id", session.getUserData().getUserId());
        hashMap.put("type", type);
        hashMap.put("amount", amount);
        hashMap.put("survey_id", survey_id);
        Log.d("1234", "callAcceptAndDeclineApi: " + Arrays.asList(hashMap));
        Call<SurveyResponse> call = apiService.custom_survey_accept_reject(hashMap);
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
                  /*      Intent intent = new Intent();
                        intent.putExtra("responseData", details.getResponse().getSurveyData());
                        setResult(RESULT_OK, intent);
                        finish();*/

                        Intent intent = new Intent(UpcomingCustomSurveyDetailsActivity.this, SurveyorHomeActivity.class);
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
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }


}
