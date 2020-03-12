package com.os.imars.operator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.os.imars.R;
import com.os.imars.operator.dao.appoint.PortDataItem;

import java.util.List;

public class PortSpinnerAdapter extends ArrayAdapter<PortDataItem> {

    private Context context;
    private List<PortDataItem> dataList;
    private int resource;

    public PortSpinnerAdapter(@NonNull Context context, int resource, List<PortDataItem> dataList) {
        super(context, resource, dataList);
        this.context = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.adapter_spinner_row, parent, false);
        TextView txtShip = (TextView) row.findViewById(R.id.cust_view);
        txtShip.setText(dataList.get(position).getPort());
        return row;
    }

}


