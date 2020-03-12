package com.os.imars.operator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.os.imars.R;
import com.os.imars.operator.dao.agent.AgentData;

import java.util.List;

public class AgentsSpinnerAdapter extends ArrayAdapter<AgentData> {

    private Context context;
    private List<AgentData> dataList;
    private int resource;

    public AgentsSpinnerAdapter(@NonNull Context context, int resource, List<AgentData> dataList) {
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
        txtShip.setText(dataList.get(position).getFirst_name()+" "+dataList.get(position).getLast_name());
        return row;
    }



}

