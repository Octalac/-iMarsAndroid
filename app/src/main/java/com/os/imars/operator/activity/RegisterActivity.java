package com.os.imars.operator.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.views.BaseView.BaseActivity;

public class RegisterActivity extends BaseActivity {

    private ImageView imvBack;
    private WebView webViewSignup;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {

        imvBack = (ImageView) findViewById(R.id.imvBack);
        imvBack.setOnClickListener(this);
        webViewSignup = (WebView) findViewById(R.id.webViewSignup);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imvBack:
                finish();
                break;
        }
    }
}
