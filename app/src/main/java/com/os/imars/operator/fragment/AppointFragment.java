package com.os.imars.operator.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.os.imars.R;
import com.os.imars.application.App;
import com.os.imars.comman.activity.NotificationActivity;
import com.os.imars.operator.activity.AppointCustomSurveyorActivity;
import com.os.imars.operator.activity.AppointSurveyorActivity;
import com.os.imars.operator.activity.CallApiCallback;
import com.os.imars.operator.activity.VesselAddActivity;
import com.os.imars.operator.adapter.SurveyTypesSpinnerAdapter;
import com.os.imars.operator.adapter.VesselsSpinnerAdapter;
import com.os.imars.operator.dao.appoint.AgentsDataItem;
import com.os.imars.operator.dao.appoint.AppointData;
import com.os.imars.operator.dao.appoint.CategoryDataItem;
import com.os.imars.operator.dao.appoint.PortDataItem;
import com.os.imars.operator.dao.appoint.SurveyTypeResponse;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.operator.dao.vessel.VesselListResponse;
import com.os.imars.utility.RxService;
import com.os.imars.utility.Session;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.BaseFragment;
import com.os.imars.views.BaseView.Env;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppointFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {

    private View.OnClickListener onClickListener;
    private CoordinatorLayout coordinatorLayout;
    private Calendar fromCalendar = Calendar.getInstance();
    private int mYear, mMonth, mDay, mHour, mMinute;
    private RelativeLayout rlArrivalDate, rlDepartureDate;
    private EditText txtArrivalDate, txtDepartureDate, spnPort, spnShip;
    private Button btnNext;
    private Spinner spnSurveyType;
    private String shipName = "", surveyType = "", portName = "";
    private Session session;
    private List<String> vesselsList, categoryList, portList;
    private ArrayAdapter<String> portSpinnerAdapter;
    private VesselsSpinnerAdapter vesselsSpinnerAdapter;
    private SurveyTypesSpinnerAdapter surveyorSpinnerAdapter;
    private CallApiCallback callApiCallback;
    private String portId = "", categoryId = "", vesselId = "";
    private List<String> agentsList;
    private List<AgentsDataItem> agentsDataItemList;
    private List<CategoryDataItem> categoryDataItemList;
    private List<VesselData> vesselDataItemList;
    private List<PortDataItem> portDataItemList;
    private RelativeLayout rlnotification;
    private int notificationCount = 0;
    private TextView txtNotificationCount;
    private HashMap<String, String> hashMap = new HashMap<>();

    @Override
    public View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appoint_surveyor, container, false);
        init(view);
        callVesselsApi();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallApiCallback) {
            callApiCallback = (CallApiCallback) context;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callApiCallback.callServerApi(false);
    }


    private void init(View view) {
        try {
            Util.showProDialog(getActivity());
            onClickListener = this;
            session = Session.getInstance(getActivity());
            vesselDataItemList = new ArrayList<>();
            vesselsList = new ArrayList<>();
            categoryDataItemList = new ArrayList<>();
            categoryList = new ArrayList<>();
            agentsDataItemList = new ArrayList<>();
            agentsList = new ArrayList<>();
            portList = new ArrayList<>();
            portDataItemList = new ArrayList<>();

            coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
            rlArrivalDate = (RelativeLayout) view.findViewById(R.id.rlArrivalDate);
            rlDepartureDate = (RelativeLayout) view.findViewById(R.id.rlDepartureDate);
            btnNext = view.findViewById(R.id.btnNext);

            txtArrivalDate = view.findViewById(R.id.txtArrivalDate);
            txtDepartureDate = view.findViewById(R.id.txtDepartureDate);
            spnSurveyType = (Spinner) view.findViewById(R.id.spnSurveyType);
            spnShip = view.findViewById(R.id.spnShip);
            spnPort = view.findViewById(R.id.spnPort);
            rlnotification = view.findViewById(R.id.rlnotification);
            txtNotificationCount = view.findViewById(R.id.txtNotificationCount);
            surveyorSpinnerAdapter = new SurveyTypesSpinnerAdapter(getActivity(), R.layout.adapter_spinner_row, categoryDataItemList);
            spnSurveyType.setAdapter(surveyorSpinnerAdapter);
            portSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.adapter_spinner_row, portList);
            spnSurveyType.setOnItemSelectedListener(this);
            rlnotification.setOnClickListener(this);
            spnPort.setOnClickListener(this);
            spnShip.setOnClickListener(this);
            rlArrivalDate.setOnClickListener(onClickListener);
            rlDepartureDate.setOnClickListener(onClickListener);
            btnNext.setOnClickListener(onClickListener);
            get_Notification_list();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlArrivalDate:
                DatePickerDialog fromDateDialog = new DatePickerDialog(Env.currentActivity, fromArrivalDate, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
                fromDateDialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()));
                fromDateDialog.show();
                txtDepartureDate.setText(null);
                break;
            case R.id.rlDepartureDate:
                DatePickerDialog fromDepartureDateDialog = new DatePickerDialog(Env.currentActivity, fromDepartureDate, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
                fromDepartureDateDialog.getDatePicker().setMinDate((long) (System.currentTimeMillis()));
                fromDepartureDateDialog.show();
                break;
            case R.id.btnNext:
                appointForSurveyor();
                break;
            case R.id.rlnotification:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivityForResult(intent, 101);
                break;
            case R.id.spnPort:
                showPortDialog("Select Port", spnPort, portList);
                break;
            case R.id.spnShip:
                Log.d("1234", "onClick: ");
                showVesselDialog("Select Vessel", spnShip, vesselDataItemList);
                break;

        }
    }


    private void showVesselDialog(String title, EditText spnPort, List<VesselData> vesselsList) {
        final View view1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_vessel_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view1).create();

        ListView lv = (ListView) view1.findViewById(R.id.listViewDialog);
        Button btnAddVessel = view1.findViewById(R.id.btnAddVessel);
        TextView txtNoRecordFound = view1.findViewById(R.id.txtNoRecordFound);
        vesselsSpinnerAdapter = new VesselsSpinnerAdapter(getActivity(), R.layout.adapter_spinner_row, vesselsList);
        lv.setAdapter(vesselsSpinnerAdapter);

        if (vesselsList.size() == 0) {
            txtNoRecordFound.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        } else {
            txtNoRecordFound.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }
        lv.setOnItemClickListener((parent, view, position, id) -> {
            vesselId = vesselDataItemList.get(position).getId();
            shipName = vesselDataItemList.get(position).getName();
            spnPort.setText(shipName);
            dialog.cancel();
        });
        btnAddVessel.setOnClickListener(v -> {
            dialog.cancel();
            Intent intent = new Intent(getActivity(), VesselAddActivity.class);
            intent.putExtra("whereToCome", "Appoint");
            startActivityForResult(intent, 102);
        });
        dialog.show();

    }

    private void showPortDialog(String title, EditText spnPort, List<String> portList) {
        final View view1 = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view1).create();
        ListView lv = view1.findViewById(R.id.listViewDialog);
        EditText inputSearch = view1.findViewById(R.id.editTextText);
        portSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, portList);
        lv.setAdapter(vesselsSpinnerAdapter);
        lv.setAdapter(portSpinnerAdapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            String entry = parent.getAdapter().getItem(position).toString();
            spnPort.setText(entry);
            portId = hashMap.get(entry);
            portName = portDataItemList.get(position).getPort();
            dialog.cancel();

        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                portSpinnerAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        dialog.show();

    }


    private void appointForSurveyor() {
        String arrivalDate = Util.convertDateFormat(txtArrivalDate.getText().toString(), "dd-mm-yyyy", "yyyy-mm-dd");
        String departureDate = Util.convertDateFormat(txtDepartureDate.getText().toString(), "dd-mm-yyyy", "yyyy-mm-dd");

        if (portName.equals("")) {
            Util.showErrorSnackBar(coordinatorLayout, "Please select port", getActivity());
        } else if (vesselId.equals("") || vesselId == null) {
            Util.showErrorSnackBar(coordinatorLayout, "Please select vessel", getActivity());
        } else if (arrivalDate.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please select start date", getActivity());
        } else if (departureDate.isEmpty()) {
            Util.showErrorSnackBar(coordinatorLayout, "Please select end date", getActivity());
        } else if (surveyType.equals("Select an option")) {
            Util.showErrorSnackBar(coordinatorLayout, "Please select survey type", getActivity());
        } else {

            if (surveyType.trim().equals("Custom Occasional Survey")) {
                Intent intent = new Intent(getActivity(), AppointCustomSurveyorActivity.class);
                intent.putExtra("agentsDataItemList", (Serializable) agentsDataItemList);
                intent.putExtra("portId", portId);
                intent.putExtra("vesselId", vesselId);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("start_date", arrivalDate);
                intent.putExtra("end_date", departureDate);
                intent.putExtra("surveyType", surveyType);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), AppointSurveyorActivity.class);
                intent.putExtra("agentsDataItemList", (Serializable) agentsDataItemList);
                intent.putExtra("portId", portId);
                intent.putExtra("vesselId", vesselId);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("start_date", arrivalDate);
                intent.putExtra("end_date", departureDate);
                intent.putExtra("surveyType", surveyType);
                startActivity(intent);
            }


        }
    }

    DatePickerDialog.OnDateSetListener fromArrivalDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //formated_date = year+ "-" +Util.setZeroBeforeNine(monthOfYear + 1)+"-"+Util.setZeroBeforeNine(dayOfMonth);
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            txtArrivalDate.setText(Util.setZeroBeforeNine(dayOfMonth) + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" + Util.setZeroBeforeNine(year));

        }
    };

    DatePickerDialog.OnDateSetListener fromDepartureDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            fromCalendar.set(Calendar.YEAR, year);
            fromCalendar.set(Calendar.MONTH, monthOfYear);
            fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //formated_date = year+ "-" +Util.setZeroBeforeNine(monthOfYear + 1)+"-"+Util.setZeroBeforeNine(dayOfMonth);
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            txtDepartureDate.setText(Util.setZeroBeforeNine(dayOfMonth) + "-" + Util.setZeroBeforeNine(monthOfYear + 1) + "-" + Util.setZeroBeforeNine(year));
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spnSurveyType:
                categoryId = categoryDataItemList.get(position).getId();
                surveyType = categoryDataItemList.get(position).getName();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void noNetworkConnection() {
        Util.dismissProDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Error");
        builder.setMessage("Please check your internet connection.");
        builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Util.hasInternet(getActivity())) {
                    Util.showProDialog(getActivity());
                    agentsDataItemList.clear();
                    vesselDataItemList.clear();
                    categoryDataItemList.clear();
                    portDataItemList.clear();
                    callApiCallback.callServerApi(true);
                } else {
                    agentsDataItemList.clear();
                    vesselDataItemList.clear();
                    categoryDataItemList.clear();
                    portDataItemList.clear();
                    callApiCallback.callServerApi(true);
                }
            }
        });
        builder.show();
    }

    private void callVesselsApi() {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<VesselListResponse> call = apiService.vessels_list(myHashMap);
        call.enqueue(new Callback<VesselListResponse>() {
            @Override
            public void onResponse(Call<VesselListResponse> call, Response<VesselListResponse> response) {
                if (response.body() != null) {
                    VesselListResponse listResponse = response.body();
                    if (listResponse != null) {
                        Log.d("1234", "onResponse: ");
                        vesselDataItemList.addAll(listResponse.getResponse().getData());
                    }
                }

            }

            @Override
            public void onFailure(Call<VesselListResponse> call, Throwable t) {

            }
        });

    }


    public void setListData(AppointData appointData) {

        if (appointData.getAgentData() != null) {
            agentsDataItemList.addAll(appointData.getAgentData());
        }
        if (appointData.getCategoryData() != null) {
            categoryDataItemList.addAll(appointData.getCategoryData());
        }
        if (appointData.getPortData() != null) {
            portDataItemList.addAll(appointData.getPortData());
        }

        if (categoryDataItemList.size() > 0) {
            CategoryDataItem dataItem = new CategoryDataItem();
            dataItem.setId("");
            dataItem.setName("Select an option");
            categoryDataItemList.add(0, dataItem);
        }
        surveyorSpinnerAdapter.notifyDataSetChanged();

        if (appointData.getPortData() != null) {
            for (PortDataItem portDataItem : appointData.getPortData()) {
                portList.add(portDataItem.getPort());
                hashMap.put(portDataItem.getPort(), portDataItem.getId());

            }
        }
        portSpinnerAdapter.notifyDataSetChanged();
        if (categoryDataItemList.size() > 0) {
            Log.d("1234", "setListData: ");
            Util.dismissProDialog();
        }
    }

    public void get_Notification_list() {
        HashMap<String, Object> myHashMap = new HashMap<String, Object>();
        myHashMap.put("user_id", session.getUserData().getUserId());
        RxService apiService = App.getClient().create(RxService.class);
        Call<NotificationResponse> call = apiService.notification_list(myHashMap);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.body() != null) {
                    NotificationResponse notificationResponse = response.body();
                    if (notificationResponse.getResponse().getStatus() == 1) {
                        if (notificationResponse.getResponse().getData().getUnreadNotificationCount() != 0) {
                            notificationCount = notificationResponse.getResponse().getData().getUnreadNotificationCount();
                            txtNotificationCount.setVisibility(View.VISIBLE);
                            txtNotificationCount.setText(notificationCount + "");
                        } else {
                            txtNotificationCount.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Log.d("1234", "onFailure: " + t.getMessage());
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            get_Notification_list();
        } else if (requestCode == 102) {
            if (data != null) {
                VesselData vesselData = (VesselData) data.getSerializableExtra("responseData");
                addVesselItem(vesselData);
            }
        }
    }

    private void addVesselItem(VesselData vesselData) {
        Log.d("1234", "addVesselItem: ");
        vesselDataItemList.add(vesselDataItemList.size(), vesselData);

    }
}

