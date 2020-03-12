package com.os.imars.operator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.dao.vessel.VesselResponse;
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

public class VesselAddActivity extends BaseActivity implements ImagePickerUtility.ImagePickerCallback {

    private ImageView imvBack;
    private Button btnAddVessels;
    private String vesselsName = "", imoNumber = "", companyName = "", street = "", city = "", state = "", pinCode = "", emailAddress = "", whereToCome = "", sameAsCompanyName = "", sameAsCompanyAddress = "", additionEmailAddress = "";
    private EditText edtVesselName, edtIMONumber, edtInvoiceCompanyName,
            edtStreetAddress, edCity, edtState, edtZip, edtEmailAddress, edtAdditionalEmailAddress;
    private ImagePickerUtility imagePickerUtility;
    private File fileImage = null;
    private Session session;
    private CoordinatorLayout coordinatorLayout;
    private CheckBox chkSameCompanyName, chkSameCompanyAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vessels);
        initView();
    }

    private void initView() {
        session = Session.getInstance(this);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        whereToCome = getIntent().getStringExtra("whereToCome");
        btnAddVessels = (Button) findViewById(R.id.btnAddVessels);
        edtVesselName = findViewById(R.id.edtVesselName);
        edtIMONumber = findViewById(R.id.edtIMONumber);

        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edCity = findViewById(R.id.edCity);
        edtState = findViewById(R.id.edtState);
        edtZip = findViewById(R.id.edtZip);

        edtInvoiceCompanyName = findViewById(R.id.edtInvoiceCompanyName);
        edtAdditionalEmailAddress = findViewById(R.id.edtAdditionalEmailAddress);
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        chkSameCompanyName = findViewById(R.id.chkSameCompanyName);
        chkSameCompanyAddress = findViewById(R.id.chkSameCompanyAddress);

        imvBack.setOnClickListener(this);
        btnAddVessels.setOnClickListener(this);
        imagePickerUtility = new ImagePickerUtility(this, this);
        setSameCompanyInfo();
    }

    private void setSameCompanyInfo() {
        chkSameCompanyName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtInvoiceCompanyName.setText(session.getUserData().getCompany());
                } else {
                    edtInvoiceCompanyName.setText(null);
                }
            }
        });
        chkSameCompanyAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtStreetAddress.setText(session.getUserData().getStreet_address());
                    edCity.setText(session.getUserData().getCity());
                    edtState.setText(session.getUserData().getState());
                    edtZip.setText(session.getUserData().getPincode());

                } else {
                    edtStreetAddress.setText(null);
                    edCity.setText(null);
                    edtState.setText(null);
                    edtZip.setText(null);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnAddVessels:
                addVissels();
                break;
        }
    }

    private void addVissels() {
        vesselsName = edtVesselName.getText().toString();
        imoNumber = edtIMONumber.getText().toString();
        companyName = edtInvoiceCompanyName.getText().toString();
        street = edtStreetAddress.getText().toString();
        city = edCity.getText().toString();
        state = edtState.getText().toString();
        pinCode = edtZip.getText().toString();
        emailAddress = edtEmailAddress.getText().toString();
        additionEmailAddress = edtAdditionalEmailAddress.getText().toString();
        if (vesselsName.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter vessel name", this);
        } else if (imoNumber.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter imo number", this);
        } else if (companyName.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter invoice company name", this);
        } else if (street.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter street address", this);
        } else if (city.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter city", this);
        } else if (state.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter state", this);
        } else if (pinCode.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter zip number", this);
        } else if (!Util.isValidEmail(emailAddress)) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter valid email", this);
        } else {
            Util.showProDialog(this);
            MultipartBody.Part body = null;
            if (fileImage != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                body = MultipartBody.Part.createFormData("image", fileImage.getName(), requestFile);
            }

            if (Util.hasInternet(this)) {
                RequestBody requestBodyAdditionEmailAddress = RequestBody.create(MediaType.parse("multipart/form-data"), additionEmailAddress);
                if (chkSameCompanyName.isChecked()) sameAsCompanyName = "1";
                else sameAsCompanyName = "0";
                if (chkSameCompanyAddress.isChecked()) sameAsCompanyAddress = "1";
                else sameAsCompanyAddress = "0";
                RequestBody vessels = RequestBody.create(MediaType.parse("multipart/form-data"), vesselsName);
                RequestBody imo = RequestBody.create(MediaType.parse("multipart/form-data"), imoNumber);
                RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), companyName);
                RequestBody invoiceAdd = RequestBody.create(MediaType.parse("multipart/form-data"), street);
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailAddress);
                RequestBody same_as_company = RequestBody.create(MediaType.parse("multipart/form-data"), sameAsCompanyName);
                RequestBody same_as_company_address = RequestBody.create(MediaType.parse("multipart/form-data"), sameAsCompanyAddress);
                RequestBody myCity = RequestBody.create(MediaType.parse("multipart/form-data"), city);
                RequestBody myState = RequestBody.create(MediaType.parse("multipart/form-data"), state);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RequestBody myPincode = RequestBody.create(MediaType.parse("multipart/form-data"), pinCode);

                RxService apiService = App.getClient().create(RxService.class);
                Call<VesselResponse> call = apiService.add_Vessel(user_id, vessels, imo, company, same_as_company, invoiceAdd,
                        same_as_company_address, email, requestBodyAdditionEmailAddress, myCity, myState, myPincode, body);
                call.enqueue(new Callback<VesselResponse>() {
                    @Override
                    public void onResponse(Call<VesselResponse> call, Response<VesselResponse> response) {
                        Util.dismissProDialog();
                        Log.d("1234", "onResponse: " + response.body().getResponse());
                        if (response.body() != null) {
                            VesselResponse vesselResponse = response.body();
                            int status = vesselResponse.getResponse().getStatus();
                            Log.d("1234", "onResponse: " + vesselResponse.getResponse().getData());
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VesselAddActivity.this);
                                builder.setMessage("New vessel - added successfully…");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", vesselResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + vesselResponse.getResponse().getMessage(), VesselAddActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VesselResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(VesselAddActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                noNetworkConnection();
            }
        }

    }

    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VesselAddActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addVissels();
            }
        });
        builder.show();
    }


    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(VesselAddActivity.this);
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

    }

    @Override
    public void onImagePickError(String message) {

    }
}

