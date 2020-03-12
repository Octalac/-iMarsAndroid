package com.os.imars.operator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.dao.mySurvey.SurveyUserDataItem;

import java.util.List;

public class SurveysListAdapter extends ArrayAdapter {

    public Context context;
    private DisplayImageOptions options;
    private List<SurveyUserDataItem> userDataItemList;
    private List<SurveyUserDataItem> userDataItemFilteredList;
    private int resource;

    public SurveysListAdapter(@NonNull Context context, int resource, List<SurveyUserDataItem> userDataItemList) {
        super(context, resource, userDataItemList);
        this.context = context;
        this.userDataItemList = userDataItemList;
        this.userDataItemFilteredList = userDataItemList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.survey_list_data, null, false);
        final SurveyUserDataItem userDataItem = userDataItemFilteredList.get(position);
        TextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(userDataItem.getName());
        return view;
    }
}
