package com.os.imars.operator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;

import java.util.List;

public class AppointCustomSurveyorAdapter extends RecyclerView.Adapter<AppointCustomSurveyorAdapter.AgentsViewHolders> {

    public Context context;
    private List<SurveyorUserData> surveyorUserDataList;
    private static String seletedStaus = "0";
    RecyclerView mRecyclerView;

    public AppointCustomSurveyorAdapter(Context context, List<SurveyorUserData> surveyorUserDataList) {
        this.context = context;
        this.surveyorUserDataList = surveyorUserDataList;
    }

    @Override
    public AppointCustomSurveyorAdapter.AgentsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appoint_surveyor_row_items, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        AppointCustomSurveyorAdapter.AgentsViewHolders viewHolder = new AppointCustomSurveyorAdapter.AgentsViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppointCustomSurveyorAdapter.AgentsViewHolders holder, final int position) {
        final SurveyorUserData surveyorUserData = surveyorUserDataList.get(position);
        holder.txtUserName.setText(surveyorUserData.getFirstName() + " " + surveyorUserData.getLastName());
        holder.txtAvgResponse.setText(surveyorUserData.getAverageResponseTime());
        holder.txtJobAcceptance.setText(surveyorUserData.getPercentageJobAcceptance()+"%");
        holder.txtPrice.setText("$" + surveyorUserData.getPricing());
        if (!surveyorUserData.getRating().equals("0")){
            holder.ratingBar.setRating(Float.parseFloat(surveyorUserData.getRating()));
        }
        Glide.with(context).load(surveyorUserData.getImage()).load(holder.imvSurveyUser).placeholder(R.drawable.user_icon);
        holder.chkStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                surveyorUserData.setChecked(isChecked);
            } else {
                surveyorUserData.setChecked(isChecked);
            }
        });
      checkedValue();
    }

    public String checkedValue() {
        String selectedId = "";
        Log.d("1234", "checkedValue: " + surveyorUserDataList.size());
        for (int i = 0; i < surveyorUserDataList.size(); i++) {
            Log.d("1234", "checkedValue: for ");
            if (surveyorUserDataList.get(i).isChecked()) {
                selectedId = selectedId.equals("") ? surveyorUserDataList.get(i).getId() : selectedId + "," + surveyorUserDataList.get(i).getId();
            }
        }
        Log.d("1234", "checkedValue: " + selectedId);
        return selectedId;
    }

    @Override
    public int getItemCount() {
        return surveyorUserDataList.size();
    }

    public class AgentsViewHolders extends RecyclerView.ViewHolder {

        private RelativeLayout rlDetailsView;
        private TextView txtUserName, txtJobAcceptance, txtPrice, txtAvgResponse, txtSurveyStatus;
        private CircleImageView imvSurveyUser;
        private CheckBox chkStatus;
        private RatingBar ratingBar;

        public AgentsViewHolders(View itemView) {
            super(itemView);
            rlDetailsView = itemView.findViewById(R.id.rlDetailsView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtJobAcceptance = itemView.findViewById(R.id.txtJobAcceptance);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtAvgResponse = itemView.findViewById(R.id.txtAvgResponse);
            txtSurveyStatus = itemView.findViewById(R.id.txtSurveyStatus);
            imvSurveyUser = itemView.findViewById(R.id.imvSurveyUser);
            chkStatus = itemView.findViewById(R.id.chkStatus);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

