package com.os.imars.operator.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.os.imars.R;
import com.os.imars.views.BaseView.BaseActivity;

public class SurveyInvoiceActivity extends BaseActivity {

    private ImageView imvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_invoice);
        initView();
    }

    private void initView() {
        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvBack:
                finish();
                break;
        }
    }
}
