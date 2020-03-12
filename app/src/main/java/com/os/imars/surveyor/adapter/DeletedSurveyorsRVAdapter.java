package com.os.imars.surveyor.adapter;

import android.content.Context;
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
import com.os.imars.operator.dao.userInfo.UserDataItem;

import java.util.List;

public class DeletedSurveyorsRVAdapter extends RecyclerView.Adapter<DeletedSurveyorsRVAdapter.AgentsViewHolders> {

    public Context context;
    private List<UserDataItem> list;

    private View.OnClickListener onClickListener;
    private DisplayImageOptions options;


    public DeletedSurveyorsRVAdapter(Context context, List<UserDataItem> list) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.list = list;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public DeletedSurveyorsRVAdapter.AgentsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_deleted_surveyor_row_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        DeletedSurveyorsRVAdapter.AgentsViewHolders viewHolder = new DeletedSurveyorsRVAdapter.AgentsViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeletedSurveyorsRVAdapter.AgentsViewHolders holder, int position) {
        //ImageLoader.getInstance().displayImage(data.get(position).getProvider_profile_image(), holder.imvDriverProfile, options);

        holder.txtEmail.setText(list.get(position).getEmail());
        Glide.with(context).load(list.get(position).getProfilePic()).placeholder(R.drawable.user_icon).into(holder.imvSurveyor);
        holder.txtName.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
        holder.txtMobile.setText(list.get(position).getMobileNumber());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AgentsViewHolders extends RecyclerView.ViewHolder {

        public RelativeLayout rlSurveyorView;
        private CircleImageView imvSurveyor;
        private TextView txtName, txtMobile, txtEmail;

        public AgentsViewHolders(View itemView) {
            super(itemView);
            rlSurveyorView = itemView.findViewById(R.id.rlSurveyorView);
            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            imvSurveyor = itemView.findViewById(R.id.imvSurveyor);
        }
    }
}
