package com.os.imars.operator.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.operator.dao.notification.NotificationItem;

import java.util.List;

public class NotificationRVAdapter extends RecyclerView.Adapter<NotificationRVAdapter.MyViewHolder> {

    public Context context;
    private List<NotificationItem> notificationItemList;
    private DisplayImageOptions options;
    private View.OnClickListener onClickListener;


    public NotificationRVAdapter(Context context, List<NotificationItem> notificationItemList, View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.notificationItemList = notificationItemList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public NotificationRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification_row_items, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        NotificationRVAdapter.MyViewHolder viewHolder = new NotificationRVAdapter.MyViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationRVAdapter.MyViewHolder holder, final int position) {
        NotificationItem notificationItem = notificationItemList.get(position);
        if (notificationItem.getIsRead() == 1) {
            holder.view_foreground.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.view_foreground.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }
        holder.view_foreground.setTag(position);
        holder.view_foreground.setOnClickListener(onClickListener);
        holder.tvTitle.setText(notificationItem.getNotiType());
        holder.tvMessage.setText(notificationItem.getNotification());
        holder.txt_Date.setText(notificationItem.getCreatedAt());
    }






    @Override
    public int getItemCount() {
        return notificationItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout view_foreground;
        private TextView tvTitle, tvMessage, txt_Date;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.txt_title);
            tvMessage = itemView.findViewById(R.id.txt_message);
            txt_Date = itemView.findViewById(R.id.txt_Date);
            view_foreground = itemView.findViewById(R.id.view_foreground);

        }
    }
}


