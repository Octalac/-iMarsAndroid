package com.os.imars.operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.activity.AgentDetailsActivity;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.dao.agent.AgentData;
import com.os.imars.views.BaseView.Env;

import java.util.List;

public class AgentsRVAdapter extends RecyclerView.Adapter<AgentsRVAdapter.AgentsViewHolders> {

    public Context context;
    private List<AgentData> list;
    private DisplayImageOptions options;
    private AgentData responseData;
    private int selectedPosition = -1;


    public AgentsRVAdapter(Context context, List<AgentData> list) {
        this.context = context;
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public AgentsRVAdapter.AgentsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agents_row_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        AgentsRVAdapter.AgentsViewHolders viewHolder = new AgentsRVAdapter.AgentsViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AgentsRVAdapter.AgentsViewHolders holder, final int position) {
        //ImageLoader.getInstance().displayImage(data.get(position).getProvider_profile_image(), holder.imvDriverProfile, options);
     //   Glide.with(context).load(list.get(position).getImage()).placeholder(R.drawable.user_icon).into(holder.imvAgents);
        holder.txtName.setText(list.get(position).getFirst_name());
        holder.txtEmail.setText(list.get(position).getEmail());
        holder.txtMobile.setText(list.get(position).getMobile());

        holder.rlAgentView.setOnClickListener(v -> {
            selectedPosition = position;
            AgentData agentData = list.get(position);
            Intent intent = new Intent(Env.currentActivity, AgentDetailsActivity.class);
            intent.putExtra("agentData", agentData);
            ((OperatorHomeActivity) context).startActivityForResult(intent, 102);
        });
    }

    public void updateItem(AgentData data) {
        list.set(selectedPosition, data);
        notifyDataSetChanged();
    }

    public void addItem(AgentData data) {
        Log.d("1234","addItem agent adapter"+data.getEmail());
        list.add(list.size(), data);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AgentsViewHolders extends RecyclerView.ViewHolder {

        public RelativeLayout rlAgentView;
        private CircleImageView imvAgents;
        private TextView txtName, txtMobile, txtEmail;

        public AgentsViewHolders(View itemView) {
            super(itemView);
            rlAgentView = itemView.findViewById(R.id.rlAgentView);
            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            imvAgents = itemView.findViewById(R.id.imvAgents);
        }
    }

}
