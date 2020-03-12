package com.os.imars.operator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.os.imars.R;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

public class VesselDetailsActivity extends BaseActivity {

    private ImageView imvBack, imvVesselsEdit, imvVessel;
    private TextView txtName, txtAddress, txtInvoiceCompany, txtEmail, txtIMO, txtVesselName, txtAdditionalEmailAdress;
    private String id = "";
    private VesselData vesselData;
    private LinearLayout llAddition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vessels_details);
        initView();
    }

    private void initView() {
        Util.showProDialog(this);
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvVessel = (ImageView) findViewById(R.id.imvVessel);
        imvVesselsEdit = (ImageView) findViewById(R.id.imvVesselsEdit);
        txtName = findViewById(R.id.txtName);
        txtAdditionalEmailAdress = findViewById(R.id.txtAdditionalEmailAdress);
        llAddition = findViewById(R.id.llAddition);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtInvoiceCompany = findViewById(R.id.txtInvoiceCompany);
        txtIMO = findViewById(R.id.txtIMO);
        imvBack.setOnClickListener(this);
        imvVesselsEdit.setOnClickListener(this);
        vesselData = (VesselData) getIntent().getSerializableExtra("vesselData");
        setData(vesselData);
    }

    private void setData(VesselData vesselData) {
        Util.dismissProDialog();
        txtEmail.setText(vesselData.getEmail());
        txtName.setText(vesselData.getName());
        txtAddress.setText(vesselData.getAddress()+","+vesselData.getCity()+","+vesselData.getState()+","+vesselData.getPincode());
        txtInvoiceCompany.setText(vesselData.getCompany());
        txtIMO.setText("IMO Number: #" + vesselData.getImo_number());
        txtAdditionalEmailAdress.setText(vesselData.getAdditionalEmail());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                Intent intent1 = new Intent();
                intent1.putExtra("responseData", vesselData);
                setResult(RESULT_OK, intent1);
                finish();
                break;
            case R.id.imvVesselsEdit:
                Intent intent = new Intent(this, VesselEditActivity.class);
                intent.putExtra("vesselData", vesselData);
                startActivityForResult(intent, 102);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            vesselData = (VesselData) data.getSerializableExtra("responseData");
            setData(vesselData);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        intent1.putExtra("responseData", vesselData);
        setResult(RESULT_OK, intent1);
        finish();
    }
}

