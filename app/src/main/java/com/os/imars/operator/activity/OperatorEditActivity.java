package com.os.imars.operator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.utility.ImagePickerUtility;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorEditActivity extends BaseActivity implements ImagePickerUtility.ImagePickerCallback {
    private ImageView imvBack;
    Button btnSave;
    private String item[] = {"Capture Camera", "Choose from Gallery"};
    private ImagePickerUtility imagePickerUtility;
    private File fileImage = null;
    private String emailAddress, id = "";
    private CoordinatorLayout coordinatorLayout;
    private Session session;
    private EditText edtFirstName, edtLastName, edtEmail, edtMobile;
    private CircleImageView imvOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operator);
        initView();
        setData();
    }

    private void initView() {
        session = Session.getInstance(this);
        imagePickerUtility = new ImagePickerUtility(this, this);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvOperator = findViewById(R.id.imvOperator);
        btnSave = (Button) findViewById(R.id.btnSave);
        imvBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobile = findViewById(R.id.edtMobile);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvOperator.setOnClickListener(this);
    }

    private void setData() {
        UserDataItem data = (UserDataItem) getIntent().getSerializableExtra("UserDataItem");
        if (data != null) {
            id = data.getUserId();
            Glide.with(this).load(data.getProfilePic()).placeholder(R.drawable.user_icon).into(imvOperator);
            edtEmail.setText(data.getEmail());
            edtFirstName.setText(data.getFirstName());
            edtLastName.setText(data.getLastName());
            edtMobile.setText(data.getMobileNumber());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnSave:
                updateOperator();
                break;
            case R.id.imvOperator:
                try{
                    showImageSelectionDialog();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }

    private void updateOperator() {
        emailAddress = edtEmail.getText().toString();

        if (emailAddress.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter email", this);
        } else if (!Util.isEmailValid(emailAddress)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            if (Util.hasInternet(this)) {
                Util.showProDialog(this);
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailAddress);
                RequestBody edit_id = RequestBody.create(MediaType.parse("multipart/form-data"), id);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RxService apiService = App.getClient().create(RxService.class);
                Call<UserResponse> call = apiService.edit_operator(user_id, edit_id, email);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            UserResponse operatorResponse = response.body();
                            int status = operatorResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OperatorEditActivity.this);
                                builder.setMessage("Operator – edited successfully, and an email has been sent to the operator!");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", operatorResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + operatorResponse.getResponse().getMessage(), OperatorEditActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Log.d("1234", t.toString());
                        Toast.makeText(OperatorEditActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Util.dismissProDialog();
                noNetworkConnection();
            }
        }


    }

    private void noNetworkConnection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OperatorEditActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateOperator();

            }
        });
        builder.show();
    }

    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(OperatorEditActivity.this);
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
        Glide.with(this).load(imagePickerUtility.getImageUri()).placeholder(R.drawable.user_icon).into(imvOperator);

    }

    @Override
    public void onImagePickError(String message) {

    }
}



