package com.os.imars.surveyor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.surveyor.dao.ServiceData;

import java.util.List;

public class ServicesRVAdapter extends RecyclerView.Adapter<ServicesRVAdapter.ViewHolders> {

    public Context context;
    private List<ServiceData> serviceDataList;
    private View.OnClickListener onClickListener;
    private DisplayImageOptions options;


    public ServicesRVAdapter(Context context, List<ServiceData> serviceDataList) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.serviceDataList = serviceDataList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public ServicesRVAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services_row_items, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        ServicesRVAdapter.ViewHolders viewHolder = new ServicesRVAdapter.ViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServicesRVAdapter.ViewHolders holder, int position) {
        //ImageLoader.getInstance().displayImage(data.get(position).getProvider_profile_image(), holder.imvDriverProfile, options);
        holder.tvName.setText(serviceDataList.get(position).getName());
        holder.tvCostOfSurvey.setText(serviceDataList.get(position).getPrice());
        if (!serviceDataList.get(position).getPrice().equals("")) {
            holder.imgCheckIcon.setVisibility(View.VISIBLE);
            holder.imgCloseIcon.setVisibility(View.GONE);
            holder.llCostOfSurvey.setVisibility(View.VISIBLE);

        } else {
            if (serviceDataList.get(position).getName().equals("Custom Occasional Survey")){
                holder.imgCheckIcon.setVisibility(View.VISIBLE);
                holder.imgCloseIcon.setVisibility(View.GONE);
                holder.llCostOfSurvey.setVisibility(View.GONE);
            }else{
                holder.imgCheckIcon.setVisibility(View.GONE);
                holder.imgCloseIcon.setVisibility(View.VISIBLE);
                holder.llCostOfSurvey.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return serviceDataList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        private RelativeLayout rlDetailsView;
        private TextView tvName, tvCostOfSurvey;
        private LinearLayout llCostOfSurvey;
        private ImageView imgCheckIcon, imgCloseIcon;

        public ViewHolders(View itemView) {
            super(itemView);


            rlDetailsView = itemView.findViewById(R.id.rlDetailsView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCostOfSurvey = itemView.findViewById(R.id.tvCostOfSurvey);
            llCostOfSurvey = itemView.findViewById(R.id.llCostOfSurvey);
            imgCheckIcon = itemView.findViewById(R.id.imgCheckIcon);
            imgCloseIcon = itemView.findViewById(R.id.imgCloseIcon);
        }
    }
}
