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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.dao.surveyor.SurveyorUserData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointSurveyorAdapter extends RecyclerView.Adapter<AppointSurveyorAdapter.AgentsViewHolders> {

    public Context context;
    private List<SurveyorUserData> surveyorUserDataList;
    private DisplayImageOptions options;
    // private SurveyorUserData surveyorUserData;
    private String seletedStaus = "0";

    RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    public AppointSurveyorAdapter(Context context, List<SurveyorUserData> surveyorUserDataList) {
        this.context = context;
        this.surveyorUserDataList = surveyorUserDataList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public AppointSurveyorAdapter.AgentsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appoint_surveyor_row_items, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        AppointSurveyorAdapter.AgentsViewHolders viewHolder = new AppointSurveyorAdapter.AgentsViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppointSurveyorAdapter.AgentsViewHolders holder, final int position) {
        final SurveyorUserData surveyorUserData = surveyorUserDataList.get(position);
        holder.txtUserName.setText(surveyorUserData.getFirstName() + " " + surveyorUserData.getLastName());
        holder.txtAvgResponse.setText(surveyorUserData.getAverageResponseTime());
        holder.txtJobAcceptance.setText(surveyorUserData.getPercentageJobAcceptance());
        holder.txtPrice.setText("$" + surveyorUserData.getPricing());
        holder.ratingBar.setRating(Float.parseFloat(surveyorUserData.getRating()));
        Glide.with(context).load(surveyorUserData.getImage()).load(holder.imvSurveyUser).placeholder(R.drawable.user_icon);

        switch (surveyorUserData.getIsStatus()) {

            case "0":
                holder.chkStatus.setChecked(false);
                holder.txtSurveyStatus.setVisibility(View.GONE);
                break;
            case "1":
                holder.chkStatus.setChecked(true);
                holder.txtSurveyStatus.setText("Primary");
                holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.chkStatus.setChecked(true);
                holder.txtSurveyStatus.setText("Substitute 1");
                holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.chkStatus.setChecked(true);
                holder.txtSurveyStatus.setText("Substitute 2");
                holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                break;
        }


        holder.chkStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {

            Log.d("1234", "status" + seletedStaus +" isChecked "+isChecked);

            if (isChecked) {
                switch (seletedStaus) {
                    case "0":
                        seletedStaus = "1";
                        surveyorUserData.setIsStatus("1");
                        holder.chkStatus.setChecked(true);
                        holder.txtSurveyStatus.setText("Primary");
                        holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        seletedStaus = "2";
                        surveyorUserData.setIsStatus("2");
                        holder.chkStatus.setChecked(true);
                        holder.txtSurveyStatus.setText("Substitute 1");
                        holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        seletedStaus = "3";
                        surveyorUserData.setIsStatus("3");
                        holder.chkStatus.setChecked(true);
                        holder.txtSurveyStatus.setText("Substitute 2");
                        holder.txtSurveyStatus.setVisibility(View.VISIBLE);
                        break;
                }

             //   notifyDataSetChanged();

            } else {
                switch (surveyorUserData.getIsStatus()) {
                    case "3":
                        int count1 = 0;
                        for (int i = 0; i < surveyorUserDataList.size(); i++) {
                            SurveyorUserData surveyorUserData2 = surveyorUserDataList.get(i);
                            switch (surveyorUserData2.getIsStatus()) {
                                case "1":
                                    count1++;
                                    break;
                                case "2":
                                    count1++;
                                    break;
                                case "3":
                                    surveyorUserData2.setIsStatus("0");
                                    holder.txtSurveyStatus.setVisibility(View.GONE);
                                    holder.chkStatus.setChecked(false);
                                    count1++;
                                    break;
                            }
                        }
                        seletedStaus = "" + (count1 - 1);
                        break;
                    case "2":
                        for (int i = 0; i < surveyorUserDataList.size(); i++) {
                            SurveyorUserData surveyorUserData2 = surveyorUserDataList.get(i);

                            switch (surveyorUserData2.getIsStatus()) {
                                case "2":
                                    surveyorUserData2.setIsStatus("0");
                                    holder.chkStatus.setChecked(false);
                                    holder.txtSurveyStatus.setVisibility(View.GONE);
                                    break;
                                case "3":
                                    surveyorUserData2.setIsStatus("0");
                                    holder.chkStatus.setChecked(false);
                                    holder.txtSurveyStatus.setVisibility(View.GONE);
                                    break;
                            }

                        }
                        seletedStaus = "1";
                        break;
                    case "1":
                        for (int i = 0; i < surveyorUserDataList.size(); i++) {
                            SurveyorUserData surveyorUserData2 = surveyorUserDataList.get(i);
                            surveyorUserData2.setIsStatus("0");
                            holder.chkStatus.setChecked(false);
                            holder.txtSurveyStatus.setVisibility(View.GONE);
                        }
                        notifyDataSetChanged();
                        seletedStaus = "0";
                        break;
                }
            }

            surveyorUserData.setChecked(isChecked);
          //  mRecyclerView.post(() -> notifyDataSetChanged());


        });

    }

    public String checkedValue() {

        HashMap<String, String> map = new HashMap<>();
        Log.d("1234", "checkedValue: map " + map);
        Log.d("1234", "checkedValue: size " + surveyorUserDataList.size());
        for (int i = 0; i < surveyorUserDataList.size(); i++) {
            if (surveyorUserDataList.get(i).isChecked()) {
                map.put(surveyorUserDataList.get(i).getIsStatus(), surveyorUserDataList.get(i).getId());

            }
        }
        Log.d("1234", "checkedValue: " + map);
        String selectedId = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            selectedId = selectedId.equals("") ? entry.getValue() : selectedId + "," + entry.getValue();
        }
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
