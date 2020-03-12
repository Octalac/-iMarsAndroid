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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.operator.dao.vessel.VesselData;
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

public class VesselEditActivity extends BaseActivity {

    private ImageView imvBack, imvVessel;
    private Button btnVesselsEdit;
    private EditText edtVesselName, edtIMONumber, edtInvoiceCompanyName, edtStreetAddress,
            edCity, edtState, edtZip, edtEmailAddress, edtAdditionalEmailAddress;
    private File fileImage = null;
    private String vesselsName = "", imoNumber = "", companyName = "", invoiceAddress = "",
            emailAddress = "", additionalEmail = "", sameAsCompanyName = "", sameAsCompanyAddress = "", pinCode = "", city = "", state = "";
    private CoordinatorLayout coordinatorLayout;
    private Session session;
    private VesselData vesselData;
    private CheckBox chkSameCompanyName, chkSameCompanyAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vessels_details);
        initView();
        setData();

    }


    private void initView() {
        session = Session.getInstance(this);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvVessel = (ImageView) findViewById(R.id.imvVessel);
        btnVesselsEdit = (Button) findViewById(R.id.btnVesselsEdit);
        chkSameCompanyName = findViewById(R.id.chkSameCompanyName);
        chkSameCompanyAddress = findViewById(R.id.chkSameCompanyAddress);

        edtVesselName = findViewById(R.id.edtVesselName);
        edtIMONumber = findViewById(R.id.edtIMONumber);
        edtInvoiceCompanyName = findViewById(R.id.edtInvoiceCompanyName);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edCity = findViewById(R.id.edCity);
        edtState = findViewById(R.id.edtState);
        edtZip = findViewById(R.id.edtZip);
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        edtAdditionalEmailAddress = findViewById(R.id.edtAdditionalEmailAddress);

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imvBack.setOnClickListener(this);
        btnVesselsEdit.setOnClickListener(this);
        setSameCompanyInfo();

    }


    private void setData() {
        vesselData = (VesselData) getIntent().getSerializableExtra("vesselData");
        edtVesselName.setText(vesselData.getName());
        edtIMONumber.setText(vesselData.getImo_number());
        edtEmailAddress.setText(vesselData.getEmail());
        edtInvoiceCompanyName.setText(vesselData.getCompany());
        edtStreetAddress.setText(vesselData.getAddress());
        edCity.setText(vesselData.getCity());
        edtState.setText(vesselData.getState());
        edtZip.setText(vesselData.getPincode());
        edtAdditionalEmailAddress.setText(vesselData.getAdditionalEmail());

        if (!vesselData.getAdditionalEmail().equals("")) {
            Log.d("1234", "setData: ");

        }

        if (vesselData.getSame_as_company().equals("1")) {
            chkSameCompanyName.setChecked(true);
        }
        if (vesselData.getSame_as_company_address().equals("1")) {
            chkSameCompanyAddress.setChecked(true);
        }
    }


    private void setSameCompanyInfo() {
        chkSameCompanyName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtInvoiceCompanyName.setText(session.getUserData().getCompany());
            } else {
                edtInvoiceCompanyName.setText(null);
            }
        });
        chkSameCompanyAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                edtStreetAddress.setText(session.getUserData().getAddress());
                edCity.setText(session.getUserData().getCity());
                edtState.setText(session.getUserData().getState());
                edtZip.setText(session.getUserData().getPincode());
            } else {
                edtStreetAddress.setText(null);
                edCity.setText(null);
                edtState.setText(null);
                edtZip.setText(null);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
            case R.id.btnVesselsEdit:
                updateVessels();
                break;
        }
    }

    private void updateVessels() {
        vesselsName = edtVesselName.getText().toString();
        imoNumber = edtIMONumber.getText().toString();
        companyName = edtInvoiceCompanyName.getText().toString();
        invoiceAddress = edtStreetAddress.getText().toString();
        emailAddress = edtEmailAddress.getText().toString();
        additionalEmail = edtAdditionalEmailAddress.getText().toString();
        city = edCity.getText().toString();
        state = edtState.getText().toString();
        pinCode = edtZip.getText().toString();

        if (vesselsName.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Vessel Name", this);
        } else if (imoNumber.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter IMO Number", this);
        } else if (companyName.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Company Name", this);
        } else if (invoiceAddress.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Invoice Address", this);
        } else if (emailAddress.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Email Address", this);
        } else if (additionalEmail.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Additional Email Address", this);
        } else if (city.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter City", this);
        } else if (state.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter State", this);
        } else if (pinCode.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please enter Pin code", this);
        } else {
            Util.showProDialog(this);
            MultipartBody.Part body = null;
            if (fileImage != null) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), fileImage);
                body = MultipartBody.Part.createFormData("image", fileImage.getName(), requestFile);
            }
            if (chkSameCompanyName.isChecked()) sameAsCompanyName = "1";
            else sameAsCompanyName = "0";
            if (chkSameCompanyAddress.isChecked()) sameAsCompanyAddress = "1";
            else sameAsCompanyAddress = "0";

            if (Util.hasInternet(VesselEditActivity.this)) {
                Log.d("1234", "updateVessels: " + additionalEmail);
                RequestBody requestAdditionalEmail = RequestBody.create(MediaType.parse("multipart/form-data"), additionalEmail);
                RequestBody vessels = RequestBody.create(MediaType.parse("multipart/form-data"), vesselsName);
                RequestBody imo = RequestBody.create(MediaType.parse("multipart/form-data"), imoNumber);
                RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), companyName);
                RequestBody invoiceAdd = RequestBody.create(MediaType.parse("multipart/form-data"), invoiceAddress);
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailAddress);
                RequestBody same_as_company = RequestBody.create(MediaType.parse("multipart/form-data"), sameAsCompanyName);
                RequestBody same_as_company_address = RequestBody.create(MediaType.parse("multipart/form-data"), sameAsCompanyAddress);
                RequestBody user_id = RequestBody.create(MediaType.parse("multipart/form-data"), session.getUserData().getUserId());
                RequestBody vessel_id = RequestBody.create(MediaType.parse("multipart/form-data"), vesselData.getId());
                RequestBody cityName = RequestBody.create(MediaType.parse("multipart/form-data"), city);
                RequestBody stateName = RequestBody.create(MediaType.parse("multipart/form-data"), state);
                RequestBody pinCodeName = RequestBody.create(MediaType.parse("multipart/form-data"), pinCode);
                RxService apiService = App.getClient().create(RxService.class);
                Call<VesselResponse> call = apiService.edit_Vessel(user_id, vessels, imo, company, same_as_company, invoiceAdd, same_as_company_address, email, requestAdditionalEmail, vessel_id,cityName,stateName, pinCodeName,body);
                call.enqueue(new Callback<VesselResponse>() {
                    @Override
                    public void onResponse(Call<VesselResponse> call, Response<VesselResponse> response) {
                        Util.dismissProDialog();
                        if (response.body() != null) {
                            VesselResponse vesselResponse = response.body();
                            int status = vesselResponse.getResponse().getStatus();
                            if (status == 1) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(VesselEditActivity.this);
                                builder.setMessage("Vessel - edited successfully…");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.putExtra("responseData", vesselResponse.getResponse().getData());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                });
                                builder.show();
                            } else {
                                Util.showErrorSnackBar(coordinatorLayout, "" + vesselResponse.getResponse().getMessage(), VesselEditActivity.this);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<VesselResponse> call, Throwable t) {
                        Util.dismissProDialog();
                        Toast.makeText(VesselEditActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                noNetworkConnection();
            }
        }

    }


    private void noNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VesselEditActivity.this);
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateVessels();
            }
        });
        builder.show();
    }

}