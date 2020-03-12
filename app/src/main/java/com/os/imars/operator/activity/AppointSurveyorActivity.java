package com.os.imars.operator.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.adapter.AgentsSpinnerAdapter;
import com.os.imars.operator.adapter.AppointSurveyorAdapter;
import com.os.imars.operator.dao.SurveyRequestResponse;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.surveyor.SurveyorUseListResponse;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;
import com.os.imars.utility.FilePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;
import com.os.imars.views.BaseView.Env;

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

public class AppointSurveyorActivity extends BaseActivity implements FilePickerUtility.ImagePickerCallback {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack;
    private RecyclerView rvAppointSurvey;
    private AppointSurveyorAdapter appointSurveyorRVAdapter;
    private Button btnAppoint;
    private List<SurveyorUserData> userDataList;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Button btnUploadFile;
    private FilePickerUtility filePickerUtility;
    private File file;
    private String fileName = "";
    private TextView txtFileName;
    private List<AgentData> agentsDataItemList;
    private EditText spnAgent;
    private AgentsSpinnerAdapter agentsSpinnerAdapter;
    private String portId = "", categoryId = "", vesselId = "", endDate = "", startDate = "", agentId = "", surveyType = "", agentName = "";
    private Session session;
    private EditText edtInstruction;
    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoint_surveyor);
        initView();
        callApi();
        callAgentApi();
    }


    private void initView() {
        try {
            portId = getIntent().getStringExtra("portId");
            categoryId = getIntent().getStringExtra("categoryId");
            vesselId = getIntent().getStringExtra("vesselId");
            startDate = getIntent().getStringExtra("start_date");
            endDate = getIntent().getStringExtra("end_date");
            surveyType = getIntent().getStringExtra("surveyType");
            session = Session.getInstance(this);
            userDataList = new ArrayList<>();
            agentsDataItemList = new ArrayList<>();

            shimmerFrameLayout = findViewById(R.id.shimmerView);
            nestedScrollView = findViewById(R.id.nestedScrollView);
            shimmerFrameLayout = findViewById(R.id.shimmerView);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            rvAppointSurvey = (RecyclerView) findViewById(R.id.rvAppointSurvey);
            imvBack = (ImageView) findViewById(R.id.imvBack);
            btnAppoint = (Button) findViewById(R.id.btnAppoint);
            btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
            txtFileName = findViewById(R.id.txtFileName);
            spnAgent = findViewById(R.id.spnAgent);
            edtInstruction = findViewById(R.id.edtInstruction);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Env.currentActivity.getApplicationContext());
            rvAppointSurvey.setLayoutManager(mLayoutManager);
            rvAppointSurvey.setItemAnimator(new DefaultItemAnimator());
            appointSurveyorRVAdapter = new AppointSurveyorAdapter(Env.currentActivity, userDataList);
            rvAppointSurvey.setAdapter(appointSurveyorRVAdapter);
            filePickerUtility = new FilePickerUtility(this, this);

            imvBack.setOnClickListener(this);
            btnAppoint.setOnClickListener(this);
            btnUploadFile.setOnClickListener(this);
            spnAgent.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApi() {
        Util.showShimmer(shimmerFrameLayout);
        if (Util.hasInternet(this)) {
            RxService apiService = App.getClient().create(RxService.class);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("survey_category_id", categoryId);
            myHashMap.put("start_date", startDate);
            myHashMap.put("end_date", endDate);
            myHashMap.put("port_id", portId);
            Log.d("1234", "callApi: " + Arrays.asList(myHashMap));
            Call<SurveyorUseListResponse> call = apiService.survey_user_list(myHashMap);
            call.enqueue(new Callback<SurveyorUseListResponse>() {
                @Override
                public void onResponse(Call<SurveyorUseListResponse> call, Response<SurveyorUseListResponse> response) {
                    Util.hideShimmer(shimmerFrameLayout);
                    if (response.body() != null) {
                        SurveyorUseListResponse listResponse = response.body();
                        userDataList.addAll(listResponse.getResponse().getData());
                        if (userDataList.size() > 0) {
                            nestedScrollView.setVisibility(View.VISIBLE);
                        } else {
                            nestedScrollView.setVisibility(View.GONE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(AppointSurveyorActivity.this);
                            builder.setMessage("No Surveyor Found");
                            builder.setCancelable(false);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                            builder.show();
                        }
                    }

                    appointSurveyorRVAdapter.notifyDataSetChanged();


                }

                @Override
                public void onFailure(Call<SurveyorUseListResponse> call, Throwable t) {
                    Log.d("1234", "onFailure: " + t.getMessage());
                    Util.hideShimmer(shimmerFrameLayout);
                    Toast.makeText(AppointSurveyorActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            noNetworkConnection("survey_user_list");
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
                        agentsDataItemList.addAll(listResponse.getResponse().getData());
                    }
                }

                @Override
                public void onFailure(Call<AgentListResponse> call, Throwable t) {
                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAppoint:
                callApiForAppointmentRequest();
                break;
            case R.id.btnUploadFile:
                uploadFile();
                break;
            case R.id.spnAgent:
                showAgentDialog("Select an Agent", spnAgent, agentsDataItemList);
                break;
        }
    }

    public void noNetworkConnection(String requestType) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.hideShimmer(shimmerFrameLayout);
                if (requestType.equals("survey_user_list")) {
                    callApi();
                } else {
                    callApiForAppointmentRequest();
                }

            }
        });
        builder.show();
    }


    private void showAgentDialog(String title, EditText spnPort, List<AgentData> agentDataList) {
        Log.d("1234", "showAgentDialog: " + agentDataList.get(1).getFirst_name());
        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_agent_without_button_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view1).create();

        ListView lv = view1.findViewById(R.id.listViewDialog);
        Button btnAdd = view1.findViewById(R.id.btnAddAgent);
        agentsSpinnerAdapter = new AgentsSpinnerAdapter(this, R.layout.adapter_spinner_row, agentDataList);
        lv.setAdapter(agentsSpinnerAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            agentId = agentsDataItemList.get(position).getId();
            agentName = agentsDataItemList.get(position).getFirst_name() + " " + agentsDataItemList.get(position).getLast_name();
            spnPort.setText(agentName);
            dialog.cancel();
        });
        btnAdd.setOnClickListener(v -> {
            dialog.cancel();
            Intent intent = new Intent(AppointSurveyorActivity.this, AgentAddActivity.class);
            intent.putExtra("whereToCome", "Appoint");
            startActivityForResult(intent, 102);
        });
        dialog.show();

    }

    private void uploadFile() {
        filePickerUtility.chooseFileFromGallary();
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
            Util.showErrorSnackBar(coordinatorLayout, "The file is too large, please upload the another file.", AppointSurveyorActivity.this);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePickerUtility.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            if (data != null) {
                AgentData agentsDataItem = (AgentData) data.getSerializableExtra("responseData");
                agentsDataItemList.add(agentsDataItemList.size(), agentsDataItem);
            }
        }
    }


    private void callApiForAppointmentRequest() {

        String selectedId = appointSurveyorRVAdapter.checkedValue();
        Log.d("1234", "callApiForAppointmentRequest: " + selectedId);

        String instruction = edtInstruction.getText().toString();
        if (Util.hasInternet(this)) {
            MultipartBody.Part body = null;
            RequestBody instructionData = null;
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("file_data", file.getName(), requestFile);
            }
            if (instruction != null) {
                instructionData = RequestBody.create(MediaType.parse("multipart/form-data"), instruction);
            }
            if (selectedId.equals("")) {
                Util.showErrorSnackBar(coordinatorLayout, "Please check atleast one surveyor", AppointSurveyorActivity.this);
            } else if (instruction == null || instruction.equals("")) {
                Util.showErrorSnackBar(coordinatorLayout, "Please enter instructions", AppointSurveyorActivity.this);
            } else if (agentId.equals("") || agentId == null) {
                Util.showErrorSnackBar(coordinatorLayout, "Please select agent", AppointSurveyorActivity.this);
            } else {
                Util.showProDialog(this);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RequestBody port_data = RequestBody.create(MediaType.parse("multipart/form-data"), portId);
                RequestBody ship_id = RequestBody.create(MediaType.parse("multipart/form-data"), vesselId);
                RequestBody start_date = RequestBody.create(MediaType.parse("multipart/form-data"), startDate);
                RequestBody end_date = RequestBody.create(MediaType.parse("multipart/form-data"), endDate);
                RequestBody survey_type_id = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId);
                RequestBody surveyors_id = RequestBody.create(MediaType.parse("multipart/form-data"), selectedId);
                RequestBody agent_id = RequestBody.create(MediaType.parse("multipart/form-data"), agentId);
                RequestBody status = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
                RxService apiService = App.getClient().create(RxService.class);
                Call<SurveyRequestResponse> call = apiService.request_survey(user_id, port_data, ship_id, start_date, end_date, survey_type_id, surveyors_id, agent_id, instructionData, status, body);
                call.enqueue(new Callback<SurveyRequestResponse>() {
                    @Override
                    public void onResponse(Call<SurveyRequestResponse> call, Response<SurveyRequestResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            SurveyRequestResponse requestResponse = response.body();
                            Log.d("1234", "onResponse: " + requestResponse.getResponse());
                            if (response.body().getResponse().getStatus() == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AppointSurveyorActivity.this);
                                builder.setMessage("Your request has been sent and is now listed in “Pending” tab…");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent(AppointSurveyorActivity.this, OperatorHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                });
                                builder.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SurveyRequestResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(AppointSurveyorActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        } else {
            noNetworkConnection("request_survey");
        }
    }


}
