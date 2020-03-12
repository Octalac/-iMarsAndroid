package com.os.imars.operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.comman.activity.ChatActivity;
import com.os.imars.dao.mySurvey.SurveyData;
import com.os.imars.dao.mySurveyors.ActiveItem;
import com.os.imars.operator.activity.UpcomingCustomSurveyDetailsActivity;
import com.os.imars.operator.activity.UpcomingSurveyDetailsActivity;
import com.os.imars.utility.Util;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.List;

public class UpcomingSurveysRVAdapter extends RecyclerView.Adapter<UpcomingSurveysRVAdapter.AgentsViewHolders>{

    public Context context;
    private DisplayImageOptions options;
    private List<ActiveItem> surveyorDataList;
    private List<ActiveItem> surveyorListFiltered;
    private int selectedPosition = -1;

    public UpcomingSurveysRVAdapter(Context context, List<ActiveItem> surveyorDataList) {
        this.context = context;
        this.surveyorDataList = surveyorDataList;
        this.surveyorListFiltered = surveyorDataList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public UpcomingSurveysRVAdapter.AgentsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_upcoming_row_items, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        UpcomingSurveysRVAdapter.AgentsViewHolders viewHolder = new UpcomingSurveysRVAdapter.AgentsViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpcomingSurveysRVAdapter.AgentsViewHolders holder, final int position) {
        final ActiveItem surveyData = surveyorListFiltered.get(position);
        holder.tvName.setText(surveyData.getSurveytypeName());
        holder.tvCity.setText(surveyData.getPort());
        String startDate = surveyData.getStartDate();
        String endDate = surveyData.getEndDate();
        Log.d("1234", "onBindViewHolder: " + startDate + "end date:" + endDate);

        holder.tvDate.setText(Util.parseDateToddMMyyyy(startDate) + "-" + Util.parseDateToddMMyyyy(endDate));
        holder.tvVesselName.setText(surveyData.getVesselsname());
        switch (surveyData.getStatus()) {
            case "0":
                holder.tvStatus.setText("Pending");
                holder.imvChatStatus.setVisibility(View.GONE);
                break;
            case "1":
                holder.tvStatus.setText("Upcoming");
                holder.imvChatStatus.setVisibility(View.VISIBLE);
                break;
            case "2":
                holder.tvStatus.setText("Cancelled");
                holder.imvChatStatus.setVisibility(View.VISIBLE);
                break;
            case "3":
                holder.tvStatus.setText("Report Submitted");
                holder.imvChatStatus.setVisibility(View.VISIBLE);
                break;

        }

        holder.rlDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                if (surveyorDataList.get(position).getSurveytypeName().equals("Custom Occasional Survey")) {
                    Intent intent = new Intent(Env.currentActivity, UpcomingCustomSurveyDetailsActivity.class);
                    intent.putExtra("id", surveyorListFiltered.get(position).getId());
                    intent.putExtra("surveyor_id", surveyorListFiltered.get(position).getSurveyorId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(Env.currentActivity, UpcomingSurveyDetailsActivity.class);
                    intent.putExtra("id", surveyorListFiltered.get(position).getId());
                    intent.putExtra("surveyor_id", surveyorListFiltered.get(position).getSurveyorId());
                    context.startActivity(intent);
                }

                // ((OperatorHomeActivity) context).startActivityForResult(intent, 105);

            }
        });

        holder.imvChatStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveItem surveyorData1 = surveyorListFiltered.get(position);
                Log.d("1234", "onClick:  " + surveyorData1.getOperatorId());/*
                Log.d("1234", "onClick:1 " + surveyorData1.getSurveytypeName());
                Log.d("1234", "onClick:2 " + surveyorData1.getSurveyorId());*/
                Intent intent = new Intent(Env.currentActivity, ChatActivity.class);
                intent.putExtra("receiver_id", surveyorData1.getSurveyorId());
                intent.putExtra("receiver_name", surveyorData1.getSurveyorsName());
                intent.putExtra("user_id", surveyorData1.getOperatorId());
                intent.putExtra("surveyId", surveyorData1.getId());
                intent.putExtra("status", surveyorData1.getStatus());
                intent.putExtra("deviceId", surveyorData1.getDevice_id());
                context.startActivity(intent);
            }
        });

    }

    public void updateItem(ActiveItem data) {
        surveyorListFiltered.set(selectedPosition, data);
        Log.d("1234", "updateItem:5 " + data.getStatus());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return surveyorListFiltered.size();
    }

  /*  @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("1234", "performFiltering: " + charSequence);
                String charString = "", type = "";
                String filterData = charSequence.toString();
                if (!filterData.equals("")) {
                    String str[] = filterData.split("/");
                    charString = str[0];
                    type = str[1];
                }
                surveyorListFiltered = surveyorDataList;
                if (charString.isEmpty()) {
                    Log.d("1234", "filter data if:2 ");
                    surveyorListFiltered = surveyorDataList;
                } else {
                    List<SurveyData> filteredList = new ArrayList<>();
                    for (SurveyData row : surveyorListFiltered) {
                        Log.d("1234", "performFiltering: " + charString + "type:" + type);
                        if (type.equals("filter")) {
                            if (row.getStatus().contains(charString)) {
                                Log.d("1234", "performFiltering: ");
                                filteredList.add(row);
                            }
                        } else {
                            if (row.getSurvey_number().contains(charString) || row.getPort().toLowerCase().contains(charString.toLowerCase()) || row.getVesslesName().toLowerCase().contains(charString.toLowerCase()) || row.getSurveytypeName().toLowerCase().contains(charString.toLowerCase())) {
                                Log.d("1234", "performFiltering: else if 3 ");
                                filteredList.add(row);
                            } else if (row.getUsername().contains(charString)) {
                                Log.d("1234", "performFiltering: else if 3 ");
                                filteredList.add(row);
                            } else if (charString.equals("All Users")) {
                                Log.d("1234", "performFiltering: else if 4 ");
                                filteredList.add(row);
                            }
                        }
                    }

                    surveyorListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = surveyorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                surveyorListFiltered = (ArrayList<SurveyData>) filterResults.values;
                SurveysFragment.txtUpcomingCount.setText("(" + surveyorListFiltered.size() + ")");
                notifyDataSetChanged();
            }
        };
    }*/

    public class AgentsViewHolders extends RecyclerView.ViewHolder {

        private RelativeLayout rlDetailsView;
        private TextView tvName, tvCity, tvDate, tvStatus, tvVesselName;
        private LinearLayout llStatus;
        private ImageView imvChatStatus;

        public AgentsViewHolders(View itemView) {
            super(itemView);
            rlDetailsView = itemView.findViewById(R.id.rlDetailsView);
            tvName = itemView.findViewById(R.id.tvName);
            tvVesselName = itemView.findViewById(R.id.tvVesselName);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            llStatus = itemView.findViewById(R.id.llStatus);
            imvChatStatus = itemView.findViewById(R.id.imvChatStatus);
        }
    }
}
