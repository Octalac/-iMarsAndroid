package com.os.imars.surveyor.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.dao.finance.FinanceResponse;
import com.os.imars.dao.finance.PaidItem;
import com.os.imars.dao.finance.UnpaidItem;
import com.os.imars.surveyor.fragment.PendingPaymentFragment;
import com.os.imars.surveyor.fragment.PaymentReceivedFragment;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.views.BaseView.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinanceActivity extends BaseActivity {


    private Fragment selectedFragment = null;
    private LinearLayout llUnpaid, llPaid;
    private TextView txtUnpaid, txtPaid, txtPaidCount, txtUnPaidCount;
    private ImageView imvBack;
    private Session session;
    public List<PaidItem> paidItemList = new ArrayList<>();
    public List<UnpaidItem> unPaidItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        init();
        callFinanceApi();
    }

    private void init() {
        session = Session.getInstance(this);
        selectedFragment = new PendingPaymentFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, selectedFragment)
                .commit();
        llUnpaid = findViewById(R.id.llUnpaid);
        llPaid = findViewById(R.id.llPaid);
        txtUnpaid = findViewById(R.id.txtUnpaid);
        imvBack = findViewById(R.id.imvBack);
        txtPaid = findViewById(R.id.txtPaid);
        txtPaidCount = findViewById(R.id.txtPaidCount);
        txtUnPaidCount = findViewById(R.id.txtUnpaidCount);
        llPaid.setOnClickListener(this);
        llUnpaid.setOnClickListener(this);
        imvBack.setOnClickListener(this);
    }

    public void callFinanceApi() {
        Log.d("1234", "callUpcomingSurveyApi: vinod ");
        RxService apiService = App.getClient().create(RxService.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", session.getUserData().getUserId());
        Call<FinanceResponse> call = apiService.finance(hashMap);
        call.enqueue(new Callback<FinanceResponse>() {
            @Override
            public void onResponse(Call<FinanceResponse> call, Response<FinanceResponse> response) {
                if (response.body() != null) {
                    paidItemList.clear();
                    unPaidItemList.clear();
                    FinanceResponse listResponse = response.body();
                    if (listResponse.getResponse().getStatus() == 1){
                        paidItemList.addAll(listResponse.getResponse().getData().getPaid());
                        unPaidItemList.addAll(listResponse.getResponse().getData().getUnpaid());
                    }
                }
                txtUnPaidCount.setText("(" + paidItemList.size() + ")");
                txtPaidCount.setText("(" + unPaidItemList.size() + ")");
            }

            @Override
            public void onFailure(Call<FinanceResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });

        manageTabs(0, true, false);

    }


    public void manageTabs(int position, boolean upcoming, boolean past) {
        try {
            switch (position) {
                case 0:
                    selectedFragment = new PendingPaymentFragment();
                    break;
                case 1:
                    selectedFragment = new PaymentReceivedFragment();
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            if (upcoming) {
                llPaid.setEnabled(false);
                txtPaid.setTextColor(getResources().getColor(R.color.white));
                txtUnPaidCount.setTextColor(getResources().getColor(R.color.white));
                txtPaidCount.setTextColor(getResources().getColor(R.color.white));
                txtUnpaid.setTextColor(getResources().getColor(R.color.white));
                llPaid.setBackgroundColor(getResources().getColor(R.color.baseColor));
            } else {
                llPaid.setEnabled(true);
                txtPaid.setTextColor(getResources().getColor(R.color.baseColor));
                txtPaidCount.setTextColor(getResources().getColor(R.color.baseColor));
                txtUnpaid.setTextColor(getResources().getColor(R.color.baseColor));
                txtUnPaidCount.setTextColor(getResources().getColor(R.color.baseColor));
                llPaid.setBackgroundColor(getResources().getColor(R.color.white));
            }
            if (past) {
                llUnpaid.setEnabled(false);
                txtPaid.setTextColor(getResources().getColor(R.color.baseColor));
                txtUnpaid.setTextColor(getResources().getColor(R.color.white));
                llUnpaid.setBackgroundColor(getResources().getColor(R.color.baseColor));
                txtPaidCount.setTextColor(getResources().getColor(R.color.baseColor));
                txtUnPaidCount.setTextColor(getResources().getColor(R.color.white));
            } else {
                llUnpaid.setEnabled(true);
                txtPaid.setTextColor(getResources().getColor(R.color.white));
                txtUnpaid.setTextColor(getResources().getColor(R.color.baseColor));
                llUnpaid.setBackgroundColor(getResources().getColor(R.color.white));
                txtPaidCount.setTextColor(getResources().getColor(R.color.white));
                txtUnPaidCount.setTextColor(getResources().getColor(R.color.baseColor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPaid:
                manageTabs(0, true, false);
                break;
            case R.id.llUnpaid:
                manageTabs(1, false, true);
                break;
            case R.id.imvBack:
                finish();
                break;

        }

    }
}
