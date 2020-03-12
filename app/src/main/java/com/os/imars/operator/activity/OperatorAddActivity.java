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

public class OperatorAddActivity extends BaseActivity implements ImagePickerUtility.ImagePickerCallback {

    private ImageView imvBack;
    private Button btnAdd;
    private CircleImageView imvOperator;
    private EditText edtEmail;
    private CoordinatorLayout coordinatorLayout;
    private ImagePickerUtility imagePickerUtility;
    private File fileImage = null;
    private Session session;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operator);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvBack = findViewById(R.id.imvBack);
        imvOperator = findViewById(R.id.imvOperator);
        edtEmail = findViewById(R.id.edtEmail);
        btnAdd = findViewById(R.id.btnAdd);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        imvOperator.setOnClickListener(this);
        imagePickerUtility = new ImagePickerUtility(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAdd:
                addOperator();
                break;
            case R.id.imvOperator:
                // showImageSelectionDialog();
                break;
        }
    }

    private void addOperator() {

        email = edtEmail.getText().toString();
        if (email.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter email", this);
        } else if (!Util.isEmailValid(email)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            if (Util.hasInternet(this)) {
                Util.showProDialog(this);
                RequestBody emailAddress = RequestBody.create(MediaType.parse("multipart/form-data"), email);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RxService apiService = App.getClient().create(RxService.class);
                Call<UserResponse> call = apiService.add_operator(user_id, emailAddress);
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        Log.d("1234", "onResponse: "+response.body());
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            UserResponse userResponse = response.body();
                            int status = userResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OperatorAddActivity.this);
                                builder.setMessage("You have added a new operator… An email is sent to the new operator to sign up. ");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", userResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + userResponse.getResponse().getMessage(), OperatorAddActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Log.d("1234", "onFailure: "+t.getMessage());
                        Toast.makeText(OperatorAddActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Util.dismissProDialog();
                noNetworkConnection();
            }
        }

    }

    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(OperatorAddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    imagePickerUtility.captureImageFromCamera();
                } else if (options[item].equals("Choose from Gallery")) {
                    imagePickerUtility.chooseImageFromGallary();
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

    private void noNetworkConnection() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OperatorAddActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOperator();

            }
        });
        builder.show();
    }

}


