package com.os.imars.surveyor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.os.imars.R;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;
import com.os.imars.operator.dao.vessel.VesselData;

import java.util.List;

public class SurveyorListAdapter extends ArrayAdapter<SurveyUserDataItem> {

    private Context context;
    private List<SurveyUserDataItem> dataList;
    private int resource, selectedPosition;
    LayoutInflater layoutInflater;
    View.OnClickListener onClickListener;


    public SurveyorListAdapter(@NonNull Context context, int resource, List<SurveyUserDataItem> dataList) {
        super(context, resource, dataList);
        this.context = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.adapter_spinner_row, parent, false);

        TextView txtShip = (TextView) row.findViewById(R.id.cust_view);
        ImageView imvSelectTick = (ImageView) row.findViewById(R.id.imvSelectTick);
        txtShip.setText(dataList.get(position).getName());

        if (dataList.get(position).isSelectedValue())
            imvSelectTick.setVisibility(View.VISIBLE);
        else
            imvSelectTick.setVisibility(View.GONE);
        return row;
    }

}

