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
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.adapter.AgentsSpinnerAdapter;
import com.os.imars.operator.dao.SurveyRequestResponse;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;
import com.os.imars.utility.FilePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextAppointCustomSurveyorActivity extends BaseActivity implements FilePickerUtility.ImagePickerCallback {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack;
    private RecyclerView rvAppointSurvey;
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
    private String portId = "", categoryId = "", selectedId = "", vesselId = "", endDate = "", startDate = "", agentId = "", surveyType = "", agentName = "";
    private Session session;
    private EditText edtInstruction;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_appoint_custom_surveyor);
        init();
        callAgentApi();
    }

    private void init() {
        filePickerUtility = new FilePickerUtility(this, this);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        btnAppoint = findViewById(R.id.btnAppoint);
        spnAgent = findViewById(R.id.spnAgent);
        txtFileName = findViewById(R.id.txtFileName);
        edtInstruction = findViewById(R.id.edtInstruction);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        session = Session.getInstance(this);
        agentsDataItemList = new ArrayList<>();
        portId = getIntent().getStringExtra("portId");
        selectedId = getIntent().getStringExtra("selectedId");
        categoryId = getIntent().getStringExtra("categoryId");
        vesselId = getIntent().getStringExtra("vesselId");
        startDate = getIntent().getStringExtra("start_date");
        endDate = getIntent().getStringExtra("end_date");
        surveyType = getIntent().getStringExtra("surveyType");
        btnUploadFile.setOnClickListener(this);
        btnAppoint.setOnClickListener(this);
        spnAgent.setOnClickListener(this);

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

    private void callAgentApi() {
        if (Util.hasInternet(this)) {
            Util.showProDialog(this);
            HashMap<String, Object> myHashMap = new HashMap<String, Object>();
            myHashMap.put("user_id", session.getUserData().getUserId());
            RxService apiService = App.getClient().create(RxService.class);
            Call<AgentListResponse> call = apiService.agents_list(myHashMap);
            call.enqueue(new Callback<AgentListResponse>() {
                @Override
                public void onResponse(Call<AgentListResponse> call, Response<AgentListResponse> response) {
                    Util.dismissProDialog();
                    AgentListResponse listResponse = response.body();
                    if (listResponse != null) {
                        agentsDataItemList.addAll(listResponse.getResponse().getData());
                    }
                }

                @Override
                public void onFailure(Call<AgentListResponse> call, Throwable t) {
                    Util.dismissProDialog();
                }
            });
        } else {
            Util.showErrorSnackBar(coordinatorLayout, "No Internet Connection", this);
        }
    }

    private void callApiForAppointmentRequest() {

        String instruction = edtInstruction.getText().toString();
        MultipartBody.Part body = null;
        RequestBody instructionData = null;
        if (Util.hasInternet(this)) {
            if (file == null) {
                Util.showErrorSnackBar(coordinatorLayout, "Please select file", NextAppointCustomSurveyorActivity.this);
            } else if (instruction.equals("")) {
                Util.showErrorSnackBar(coordinatorLayout, "Please enter instructions", NextAppointCustomSurveyorActivity.this);
            } else if (selectedId.equals("")) {
                Util.showErrorSnackBar(coordinatorLayout, "Please check atleast one surveyor", NextAppointCustomSurveyorActivity.this);
            } else if (agentId.equals("") || agentId == null) {
                Util.showErrorSnackBar(coordinatorLayout, "Please select agent", NextAppointCustomSurveyorActivity.this);
            } else {
                Util.showProDialog(this);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("file_data", file.getName(), requestFile);
                instructionData = RequestBody.create(MediaType.parse("multipart/form-data"), instruction);
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
                Log.d("1234", "callApiForAppointmentRequest: " + session.getUserData().getUserId() + "port id " + portId + "vessel " + vesselId + "startDate" + startDate + "endDate" + endDate + "categoryId" + categoryId + "selectedId" + selectedId + "agentId" + agentId);
                Call<SurveyRequestResponse> call = apiService.custom_request_survey(user_id, port_data, ship_id, start_date, end_date, survey_type_id, surveyors_id, agent_id, instructionData, status, body);
                call.enqueue(new Callback<SurveyRequestResponse>() {
                    @Override
                    public void onResponse(Call<SurveyRequestResponse> call, Response<SurveyRequestResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            SurveyRequestResponse requestResponse = response.body();
                            Log.d("1234", "onResponse: " + requestResponse.getResponse().getMessage());
                            if (response.body().getResponse().getStatus() == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NextAppointCustomSurveyorActivity.this);
                                builder.setMessage("You have started the bidding process, check survey details to view quotes submitted… ");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent(NextAppointCustomSurveyorActivity.this, OperatorHomeActivity.class);
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
                        Toast.makeText(NextAppointCustomSurveyorActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            Util.dismissProDialog();
            noNetworkConnection();
        }
    }

    public void noNetworkConnection() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.hideShimmer(shimmerFrameLayout);
                callApiForAppointmentRequest();

            }
        });
        builder.show();
    }


    private void showAgentDialog(String title, EditText spnPort, List<AgentData> agentDataList) {
        final View view1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_vessel_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        Button btnAdd = view1.findViewById(R.id.btnAddVessel);
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
            Intent intent = new Intent(NextAppointCustomSurveyorActivity.this, AgentAddActivity.class);
            intent.putExtra("whereToCome", "Appoint");
            startActivityForResult(intent, 102);
        });
        dialog.show();

    }

    private void uploadFile() {
        filePickerUtility.chooseFileFromGallary();
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
            Util.showErrorSnackBar(coordinatorLayout, "The file is too large, please upload the another file.", NextAppointCustomSurveyorActivity.this);
        }
    }
}
