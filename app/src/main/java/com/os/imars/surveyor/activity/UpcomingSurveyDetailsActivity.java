package com.os.imars.surveyor.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.dao.mySurvey.SurveyResponse;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.dao.mySurvey.SurveyUsersListResponse;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.activity.VesselAddActivity;
import com.os.imars.operator.adapter.VesselsSpinnerAdapter;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.surveyor.adapter.SurveyorListAdapter;
import com.os.imars.utility.FilePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpcomingSurveyDetailsActivity extends BaseActivity implements FilePickerUtility.ImagePickerCallback {

    private Calendar fromCalendar = Calendar.getInstance();

    private ImageView imvBack, imgEditDate, imgEditSurveyor;
    private TextView tvDecline, txtSlideRightSide, txtSurveyStatus, txtSubmitReport;
    private TextView txtSurveyNumber, txtViewFile, txtSurveyor, txtPrice, txtDate, txtSurveyType, txtPort, txtOperator, txtInstruction, txtFileName, txtVesselName, txtIMO,
            txtVesselCompanyName, txtVesselAddress, txtSurveyorCompany, txtUploadStatus, txtUploadFile, txtVesselEmail, txtNoDataFound, txtAgentEmail, txtAgentContact, txtAgentName, txtInstructionAndDocument, txtAverageInvoicePaymentTime, txtOperatorCountry, txtNoOfSurvey, txtCompanyWebsite, txtOperatorName, txtCompanyName;
    private CardView cardViewInstructionAndDocument, cardViewUploadFile, cardViewReportAndInvoice;
    private CustomSliderButton btnSlideToAccept;
    private ShimmerFrameLayout shimmerFrameLayout;
    private String id = "", survey_id = "", surveyorId = "";
    private ScrollView llBodyView;
    private RelativeLayout rlBottomSlide, rlBottomView, rlbtnCancel;
    private Session session;
    private CoordinatorLayout coordinatorLayout;
    private Button btnUploadFile, btnCancel, btnViewReport, btnDownloadReport, btnViewInvoice, btnDownloadInvoice;
    private File file;
    private FilePickerUtility filePickerUtility;
    private String surveyType = "", amount = "", survey_category_type = "", uploadedFile = "", invoiceURL = "", reportURL = "";
    private EditText edtNoDay;
    private LinearLayout llSurveyorCompany;
    private RelativeLayout llDownloadFile;
    private static final int MEGABYTE = 1024 * 1024;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private List<SurveyUserDataItem> userDataItemList;
    private List<String> userList = new ArrayList<>();
    private LinearLayout llInvoice, llReport, llNumberOfDay;
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
        setContentView(R.layout.activity_upcoming_surveyor_survey_details);
        initView();
        callApi();
        callSurveyorListApi();
    }


    private void initView() {
        id = getIntent().getStringExtra("id");
        session = Session.getInstance(UpcomingSurveyDetailsActivity.this);
        userDataItemList = new ArrayList<>();
        shimmerFrameLayout = findViewById(R.id.shimmerView);
        imvBack = findViewById(R.id.imvBack);
        imgEditDate = findViewById(R.id.imgEditDate);
        imgEditSurveyor = findViewById(R.id.imgEditSurveyor);
        tvDecline = findViewById(R.id.tvDecline);
        btnSlideToAccept = findViewById(R.id.btnSlideToAccept);
        txtSlideRightSide = findViewById(R.id.txtSlideRightSide);
        txtSurveyNumber = findViewById(R.id.txtSurveyNumber);
        cardViewUploadFile = findViewById(R.id.cardViewUploadFile);
        llDownloadFile = findViewById(R.id.llDownloadFile);
        txtUploadFile = findViewById(R.id.txtUploadFile);
        txtViewFile = findViewById(R.id.txtViewFile);
        edtNoDay = findViewById(R.id.edtNoDay);
        rlbtnCancel = findViewById(R.id.rlbtnCancel);
        btnCancel = findViewById(R.id.btnCancel);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        txtNoDataFound = findViewById(R.id.txtNoDataFound);
        txtUploadStatus = findViewById(R.id.txtUploadStatus);
        filePickerUtility = new FilePickerUtility(this, this);
        txtAverageInvoicePaymentTime = findViewById(R.id.txtAverageInvoicePaymentTime);
        txtOperatorCountry = findViewById(R.id.txtOperatorCountry);
        txtNoOfSurvey = findViewById(R.id.txtNoOfSurvey);
        txtOperatorName = findViewById(R.id.txtOperatorName);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        txtCompanyWebsite = findViewById(R.id.txtCompanyWebsite);
        txtSubmitReport = findViewById(R.id.txtSubmitReport);
        txtSurveyor = findViewById(R.id.txtSurveyor);
        llBodyView = findViewById(R.id.llBodyView);
        llSurveyorCompany = findViewById(R.id.llSurveyorCompany);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        rlBottomSlide = findViewById(R.id.rlBottomSlide);
        rlBottomView = findViewById(R.id.rlBottomView);
        txtPrice = findViewById(R.id.txtPrice);
        txtDate = findViewById(R.id.txtDate);
        txtSurveyType = findViewById(R.id.txtSurveyType);
        txtPort = findViewById(R.id.txtPort);
        txtOperator = findViewById(R.id.txtOperator);
        txtInstruction = findViewById(R.id.txtInstruction);
        txtFileName = findViewById(R.id.txtFileName);
        txtSurveyStatus = findViewById(R.id.txtSurveyStatus);
        txtVesselName = findViewById(R.id.txtVesselName);
        txtViewFile.setOnClickListener(this);
        btnViewReport = findViewById(R.id.btnViewReport);
        btnDownloadReport = findViewById(R.id.btnDownloadReport);
        btnViewInvoice = findViewById(R.id.btnViewInvoice);
        btnDownloadInvoice = findViewById(R.id.btnDownloadInvoice);

        llNumberOfDay = findViewById(R.id.llNumberOfDay);
        cardViewInstructionAndDocument = findViewById(R.id.cardViewInstructionAndDocument);
        txtInstructionAndDocument = findViewById(R.id.txtInstructionAndDocument);
        txtIMO = findViewById(R.id.txtIMO);
        txtAgentContact = findViewById(R.id.txtAgentContact);
        txtAgentName = findViewById(R.id.txtAgentName);
        txtAgentEmail = findViewById(R.id.txtAgentEmail);
        txtSurveyorCompany = findViewById(R.id.txtSurveyorCompany);

        cardViewReportAndInvoice = findViewById(R.id.cardViewReportAndInvoice);
        llInvoice = findViewById(R.id.llInvoice);
        llReport = findViewById(R.id.llReport);
        imvBack.setOnClickListener(this);
        imgEditDate.setOnClickListener(this);
        tvDecline.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);
        txtSubmitReport.setOnClickListener(this);
        llDownloadFile.setOnClickListener(this);
        imgEditSurveyor.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        btnViewReport.setOnClickListener(this);
        btnDownloadReport.setOnClickListener(this);
        btnViewInvoice.setOnClickListener(this);
        btnDownloadInvoice.setOnClickListener(this);

        btnSlideToAccept.setOnUnlockListener(() -> {
            txtSlideRightSide.setVisibility(View.VISIBLE);
            String type = "accept";
            callAcceptAndDeclineApi(type);

        });
        btnSlideToAccept.setOnLockListener(() -> txtSlideRightSide.setVisibility(View.GONE));

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.imgEditDate:
                DatePickerDialog fromDateDialog = new DatePickerDialog(Env.currentActivity, fromArrivalDate, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
                fromDateDialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()));
                fromDateDialog.show();
                break;
            case R.id.imgEditSurveyor:
                callSurveyorListDialog("Select Surveyor", userDataItemList, userList);
                break;
            case R.id.tvDecline:
                declineDialog();
                break;
            case R.id.btnUploadFile:
                uploadFile();
                break;
            case R.id.txtSubmitReport:
                if (survey_category_type.equals("fix")) {
                    callApiUploadFile();
                } else {
                    callApiForDaySurveyUploadFile();
                }
                break;
            case R.id.llDownloadFile:
                downloadFile(uploadedFile);
                break;
            case R.id.btnCancel:
                cancelSurvey();
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

    private void cancelSurvey() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this);
        builder.setMessage("Are you sure you want to cancel this survey? Cancelling too many surveys may result in penalties. Do you want to proceed? ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                        Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, SurveyorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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


    }


    private void callSurveyorListApi() {
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


    private void callSurveyorListDialog(String title, List<SurveyUserDataItem> userDataList, List<String> arrayList) {
        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_surveyor_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        Button btnUpdateSurveyor = view1.findViewById(R.id.btnUpdateSurveyor);
        Button btnCancel = view1.findViewById(R.id.btnCancel);
        TextView txtNoRecordFound = view1.findViewById(R.id.txtNoRecordFound);
        TextView txtTitle = view1.findViewById(R.id.txtTitle);
        txtTitle.setText(title);
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
            for (SurveyUserDataItem surveyUserDataItem : userDataList) {
                surveyUserDataItem.setSelectedValue(false);
            }
            userDataList.get(position).setSelectedValue(true);
            surveyorId = userDataList.get(position).getId();
            surveyorListAdapter.notifyDataSetChanged();

        });
        btnUpdateSurveyor.setOnClickListener(v -> {

            if (surveyorId.equals("")) {
                Toast.makeText(this, "Select Surveyor", Toast.LENGTH_SHORT).show();
            } else {
                dialog.cancel();
                callChangeSurveyorApi(surveyorId);
            }

        });

        btnCancel.setOnClickListener(v -> {
            dialog.cancel();
        });

        dialog.show();

    }

    private void callChangeSurveyorApi(String surveyorId) {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        hashMap.put("survey_id", id);
        hashMap.put("surveyor_id", surveyorId);
        Call<CommonResponse> call = apiService.assign_to_surveyor(hashMap);
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


    private void callApiForDaySurveyUploadFile() {

        MultipartBody.Part body = null;
        if (file == null) {
            Toast.makeText(this, "Upload File", Toast.LENGTH_SHORT).show();
        } else {
            RequestBody surveyorId = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
            RequestBody surveyId = RequestBody.create(MediaType.parse("multipart/form-data"), survey_id);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            String noOfDay = edtNoDay.getText().toString();
            RequestBody no_of_days = RequestBody.create(MediaType.parse("multipart/form-data"), noOfDay);

            if (noOfDay.isEmpty()) {
                Toast.makeText(this, "Enter Day", Toast.LENGTH_SHORT).show();
            } else {
                no_of_days = RequestBody.create(MediaType.parse("multipart/form-data"), noOfDay);
                Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                                    Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                                    Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, SurveyorHomeActivity.class);
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

            Util.showProDialog(UpcomingSurveyDetailsActivity.this);
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
                                Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);
                                Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, SurveyorHomeActivity.class);
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
        if (size >= 5) {
            Util.showErrorSnackBar(coordinatorLayout, "The file is too large, please upload the another file.", UpcomingSurveyDetailsActivity.this);
        }
    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        llBodyView.setVisibility(View.GONE);
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("user_id", session.getUserData().getUserId());
        Log.d("1234", "callApi: " + Arrays.asList(hashMap));
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
                        rlBottomView.setVisibility(View.GONE);
                        setSurveyDetailsData(details);
                    } else {
                        llBodyView.setVisibility(View.GONE);
                        txtNoDataFound.setVisibility(View.VISIBLE);
                        rlBottomView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SurveyRequestDetails> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

    }

    private void setSurveyDetailsData(SurveyRequestDetails details) {
        if (details.getResponse().getData().size() != 0) {
            Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getSurveyorsName() + "port" + details.getResponse().getData().get(0).getPort());

            llBodyView.setVisibility(View.VISIBLE);
            survey_id = details.getResponse().getData().get(0).getId();
            surveyType = details.getResponse().getData().get(0).getSurveycateName();


            if (details.getResponse().getData().get(0).getFileData().equals("") && details.getResponse().getData().get(0).getInstruction().equals("")) {
                txtInstructionAndDocument.setVisibility(View.GONE);
                cardViewInstructionAndDocument.setVisibility(View.GONE);
            } else if (details.getResponse().getData().get(0).getFileData().equals("")) {
                llDownloadFile.setVisibility(View.GONE);
                txtInstruction.setText(details.getResponse().getData().get(0).getInstruction());
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


            Log.d("1234", "setSurveyDetailsData: " + details.getResponse().getData().get(0).getStatus());

            Log.d("1234", "user status : " + session.getUserData().getType());


            if (session.getUserData().getType().equals("4")) {
                llSurveyorCompany.setVisibility(View.GONE);
            }

            if (details.getResponse().getData().get(0).getStatus().equals("0")) {
                Log.d("1234", "setSurveyDetailsData: 12 ");
                rlBottomSlide.setVisibility(View.VISIBLE);
                tvDecline.setVisibility(View.VISIBLE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                imgEditSurveyor.setVisibility(View.GONE);
                imgEditDate.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
            } else if (details.getResponse().getData().get(0).getStatus().equals("1")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlBottomSlide.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.VISIBLE);
                cardViewUploadFile.setVisibility(View.VISIBLE);
                rlBottomView.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
                if (session.getUserData().getType().equals("2")) {
                    imgEditSurveyor.setVisibility(View.VISIBLE);
                    imgEditDate.setVisibility(View.VISIBLE);
                    rlBottomView.setVisibility(View.GONE);
                } else if (session.getUserData().getType().equals("3")) {
                    imgEditSurveyor.setVisibility(View.GONE);
                    imgEditDate.setVisibility(View.VISIBLE);
                    rlBottomView.setVisibility(View.GONE);
                } else if (session.getUserData().getType().equals("4")) {
                    imgEditSurveyor.setVisibility(View.GONE);
                    imgEditDate.setVisibility(View.GONE);
                    rlBottomView.setVisibility(View.GONE);
                }

            } else if (details.getResponse().getData().get(0).getStatus().equals("2")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlBottomSlide.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                imgEditSurveyor.setVisibility(View.GONE);
                imgEditDate.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.GONE);
            } else if (details.getResponse().getData().get(0).getStatus().equals("3")) {
                Log.d("1234", "setSurveyDetailsData: 123");
                rlBottomSlide.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                imgEditSurveyor.setVisibility(View.GONE);
                imgEditDate.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
                cardViewReportAndInvoice.setVisibility(View.VISIBLE);
                llInvoice.setVisibility(View.GONE);
                llReport.setVisibility(View.VISIBLE);
            }

            if (details.getResponse().getData().get(0).getBid_status().equals("1")) {
                rlBottomSlide.setVisibility(View.GONE);
                tvDecline.setVisibility(View.GONE);
                txtUploadFile.setVisibility(View.GONE);
                cardViewUploadFile.setVisibility(View.GONE);
                rlBottomView.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                rlbtnCancel.setVisibility(View.GONE);
            }

            if (details.getResponse().getData().get(0).getSurvey_category_type().equals("daily")) {
                llNumberOfDay.setVisibility(View.VISIBLE);
            } else {
                llNumberOfDay.setVisibility(View.GONE);
            }

            survey_category_type = details.getResponse().getData().get(0).getSurvey_category_type();
            txtAgentEmail.setText(details.getResponse().getData().get(0).getAgentsemail());
            uploadedFile = details.getResponse().getData().get(0).getFileData();
            txtSurveyNumber.setText(details.getResponse().getData().get(0).getSurveyNumber());
            txtSurveyor.setText(details.getResponse().getData().get(0).getSurveyorsName());
            if (details.getResponse().getData().get(0).getTransportation_cost().equals("")) {
                txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing());

            } else {
                txtPrice.setText("$" + details.getResponse().getData().get(0).getPricing() + "+ $" + details.getResponse().getData().get(0).getTransportation_cost() + " Transportation Cost");

            }
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
            txtCompanyName.setText(details.getResponse().getData().get(0).getOperator_company());
            txtSurveyorCompany.setText(details.getResponse().getData().get(0).getSurveyor_company());
            reportURL = details.getResponse().getData().get(0).getReportURL();
            invoiceURL = details.getResponse().getData().get(0).getInvoice_url();
        }


    }

    private void declineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpcomingSurveyDetailsActivity.this);
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
                        Intent intent = new Intent(UpcomingSurveyDetailsActivity.this, SurveyorHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Util.showSuccessSnackBar(coordinatorLayout, "" + details.getResponse().getMessage(), UpcomingSurveyDetailsActivity.this);

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


    DatePickerDialog.OnDateSetListener fromArrivalDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //formated_date = year+ "-" +Util.setZeroBeforeNine(monthOfYear + 1)+"-"+Util.setZeroBeforeNine(dayOfMonth);
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            callChangeDateApi(Util.setZeroBeforeNine(dayOfMonth) + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" + Util.setZeroBeforeNine(year));
        }
    };

    private void callChangeDateApi(String changedDate) {
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("survey_id", id);
        hashMap.put("start_date", changedDate);
        Call<CommonResponse> call = apiService.change_start_date(hashMap);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);

    }

}
