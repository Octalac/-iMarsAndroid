package com.os.imars.surveyor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.dao.reportIssue.DataItem;
import com.os.imars.dao.reportIssue.ReportSurveyListResponse;
import com.os.imars.utility.FilePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAnIssueActivity extends BaseActivity implements FilePickerUtility.ImagePickerCallback {

    private EditText edtReportComments;
    private Spinner spnSurveyNumber;
    private Button btnUploadFile, btnSubmit;
    private ImageView imvBack;
    private FilePickerUtility filePickerUtility;
    private File file;
    private String fileName = "", surveyId = "", surveyNumber = "";
    private TextView txtFileName;
    private CoordinatorLayout coordinatorLayout;
    private Session session;
    private List<DataItem> customSurveyList;
    private List<String> surveyList;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_an_issue);
        init();
        callGetSurveyNumberApi();
    }


    private void callGetSurveyNumberApi() {
        Util.showProDialog(this);
        if (Util.hasInternet(this)) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            Log.d("1234", "callApi: " + Arrays.asList(myHashMap));
            Call<ReportSurveyListResponse> call = apiService.report_survey_list(myHashMap);
            call.enqueue(new Callback<ReportSurveyListResponse>() {
                @Override
                public void onResponse(Call<ReportSurveyListResponse> call, Response<ReportSurveyListResponse> response) {
                    Util.dismissProDialog();
                    customSurveyList.clear();
                    if (response.body() != null) {
                        if (response.body().getResponse().getStatus().equals("1")) {
                            ReportSurveyListResponse listResponse = response.body();
                            customSurveyList.addAll(listResponse.getResponse().getData());
                            for (DataItem dataItem : customSurveyList) {
                                surveyList.add(dataItem.getSurveyNumber());
                            }
                            if (surveyList.size() > 0) {
                                surveyList.add(0, "Select Survey");
                            }
                            if (customSurveyList.size() > 0) {
                                customSurveyList.add(0, new DataItem());
                            }
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ReportSurveyListResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.dismissProDialog();
                    Toast.makeText(ReportAnIssueActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            callGetSurveyNumberApi();
        }
    }


    private void init() {
        session = Session.getInstance(this);
        surveyList = new ArrayList<>();
        customSurveyList = new ArrayList<>();
        edtReportComments = findViewById(R.id.edtReportComments);
        spnSurveyNumber = findViewById(R.id.spnSurveyNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        imvBack = findViewById(R.id.imvBack);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        txtFileName = findViewById(R.id.txtFileName);
        btnUploadFile.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        filePickerUtility = new FilePickerUtility(this, this);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, surveyList);
        spnSurveyNumber.setAdapter(arrayAdapter);
        spnSurveyNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                surveyId = customSurveyList.get(position).getId();
                surveyNumber = surveyList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnUploadFile:
                uploadFile();
                break;
            case R.id.btnSubmit:
                submitReportAnIssues();
                break;
        }
    }


    private void uploadFile() {
        filePickerUtility.chooseFileFromGallary();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePickerUtility.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onImagePickSuccess() {
        file = filePickerUtility.getImageFile();
        fileName = filePickerUtility.getFilename();
        txtFileName.setText("File Uploaded.");
    }

    @Override
    public void onImagePickError(String message) {
    }

    @Override
    public void onFilePickerSize(long size) {
        Log.d("1234", "onFilePickerSize: " + size);
        if (size >= 5) {
            Util.showErrorSnackBar(coordinatorLayout, "The file is too large, please upload the another file.", ReportAnIssueActivity.this);
        }
    }


    private void submitReportAnIssues() {

        String comments = edtReportComments.getText().toString();
        if (surveyNumber.equals("") || surveyNumber.equals("Select Survey")) {
            Util.showErrorSnackBar(coordinatorLayout, "Select Survey", this);
        } else if (comments.equals("")) {
            Util.showErrorSnackBar(coordinatorLayout, "Enter comment", this);
        } else {
            MultipartBody.Part body = null;
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            }
            if (Util.hasInternet(ReportAnIssueActivity.this)) {
                Util.showProDialog(this);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RequestBody survey_id = RequestBody.create(MediaType.parse("multipart/form-data"), surveyId);
                RequestBody comment = RequestBody.create(MediaType.parse("multipart/form-data"), comments);
                RxService apiService = App.getClient().create(RxService.class);
                Call<CommonResponse> call = apiService.report_issue_submit(user_id, survey_id, comment, body);
                call.enqueue(new Callback<CommonResponse>() {
                    @Override
                    public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                        Util.dismissProDialog();
                        Log.d("1234", "onResponse: " + response.body());
                        if (response.body() != null) {
                            CommonResponse agentResponse = response.body();
                            int status = agentResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReportAnIssueActivity.this);
                                builder.setMessage("You have successfully submitted your survey report… ");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + agentResponse.getResponse().getMessage(), ReportAnIssueActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<CommonResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Log.d("1234", t.toString());
                        Toast.makeText(ReportAnIssueActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                noNetworkConnection();
            }

        }

    }

    public void noNetworkConnection() {
        Util.dismissProDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitReportAnIssues();
            }
        });
        builder.show();
    }


}
