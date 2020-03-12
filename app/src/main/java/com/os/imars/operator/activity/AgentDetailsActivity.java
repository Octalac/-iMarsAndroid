package com.os.imars.operator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseActivity;

public class AgentDetailsActivity extends BaseActivity {

    private CoordinatorLayout coordinatorLayout;
    private ImageView imvBack, imvAgentsEdit;
    private String first_name, last_name, email, mobile, imageUrl, id;
    private TextView txtName, txtEmail, txtMobile;
    private CircleImageView imvAgents;
    private AgentData agentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_details);
        initView();
    }

    private void initView() {
        imvBack = findViewById(R.id.imvBack);
        imvAgents = findViewById(R.id.imvAgents);
        imvAgentsEdit = findViewById(R.id.imvAgentsEdit);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        imvBack.setOnClickListener(this);
        imvAgentsEdit.setOnClickListener(this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        agentData = (AgentData) getIntent().getSerializableExtra("agentData");
        setData(agentData);
    }

    private void setData(AgentData agentData) {
        Util.dismissProDialog();
        Glide.with(this).load(agentData.getImage()).placeholder(R.drawable.user_icon).into(imvAgents);
        txtEmail.setText(agentData.getEmail());
        txtName.setText(agentData.getFirst_name());
        txtMobile.setText(agentData.getMobile());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                Intent intent1 = new Intent();
                intent1.putExtra("responseData", agentData);
                setResult(RESULT_OK, intent1);
                finish();
                break;
            case R.id.imvAgentsEdit:
                Intent intent = new Intent(this, AgentEditActivity.class);
                intent.putExtra("agentData", agentData);
                startActivityForResult(intent, 102);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            agentData = (AgentData) data.getSerializableExtra("responseData");
            setData(agentData);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        intent1.putExtra("responseData", agentData);
        setResult(RESULT_OK, intent1);
        finish();
    }
}

