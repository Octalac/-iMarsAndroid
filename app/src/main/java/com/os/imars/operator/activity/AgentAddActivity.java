package com.os.imars.operator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.agent.AgentResponse;
import com.os.imars.utility.ImagePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentAddActivity extends BaseActivity implements ImagePickerUtility.ImagePickerCallback {

    private ImageView imvBack;
    private CircleImageView imvAgents;
    private Button btnAdd;
    private EditText edtCompanyName, edtEmail, edtMobile;
    private CoordinatorLayout coordinatorLayout;
    private ImagePickerUtility imagePickerUtility;
    private File fileImage = null;
    private Session session;
    private String companyName="", email="", mobile="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvBack = findViewById(R.id.imvBack);
        imvAgents = findViewById(R.id.imvAgents);
        edtCompanyName = findViewById(R.id.edtCompanyName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        btnAdd = findViewById(R.id.btnAdd);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        imvAgents.setOnClickListener(this);
        imagePickerUtility = new ImagePickerUtility(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAdd:
                addAgent();
                break;
            case R.id.imvAgents:
            //    showImageSelectionDialog();
                break;
        }
    }

    private void addAgent() {
        companyName = edtCompanyName.getText().toString();
        email = edtEmail.getText().toString();
        mobile = edtMobile.getText().toString();
        if (companyName.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Company name", this);
        } else if (mobile.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter mobile", this);
        } else if (!Util.isValidMobile(mobile)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid mobile", this);
        } else if (!Util.isValidEmail(email)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            Util.showProDialog(this);
            MultipartBody.Part body = null;
            if (fileImage != null) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                body = MultipartBody.Part.createFormData("image", fileImage.getName(), requestFile);
            }
            if (Util.hasInternet(AgentAddActivity.this)) {
                RequestBody company_name = RequestBody.create(MediaType.parse("multipart/form-data"), companyName);
                RequestBody emailAddress = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody mobileNumber = RequestBody.create(MediaType.parse("multipart/form-data"), mobile);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RxService apiService = App.getClient().create(RxService.class);
                Call<AgentResponse> call = apiService.add_agent(user_id, company_name, emailAddress, mobileNumber, body);
                call.enqueue(new Callback<AgentResponse>() {
                    @Override
                    public void onResponse(Call<AgentResponse> call, Response<AgentResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            AgentResponse agentResponse = response.body();
                            int status = agentResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AgentAddActivity.this);
                                builder.setMessage("New agent - added successfully…");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", agentResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + agentResponse.getResponse().getMessage(), AgentAddActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AgentResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(AgentAddActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                        Log.d("1234", t.toString());
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
                addAgent();
            }
        });
        builder.show();
    }

    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AgentAddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try{
                        imagePickerUtility.captureImageFromCamera();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    try{
                        imagePickerUtility.chooseImageFromGallary();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePickerUtility.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onImagePickSuccess() {
        fileImage = imagePickerUtility.getImageFile();
        Glide.with(this).load(imagePickerUtility.getImageUri()).placeholder(R.drawable.user_icon).into(imvAgents);

    }

    @Override
    public void onImagePickError(String message) {

    }

}


