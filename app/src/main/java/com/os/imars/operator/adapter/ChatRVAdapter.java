package com.os.imars.operator.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.dao.comman.ChatResponse;
import com.os.imars.utility.Util;
import com.vatsal.imagezoomer.ZoomAnimation;

import java.util.List;

public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<ChatResponse> data;
    private DisplayImageOptions options;
    private String senderId;

    public ChatRVAdapter(Context context, List<ChatResponse> data, String senderId) {
        this.context = context;
        this.data = data;
        this.senderId = senderId;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == 0) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_row_right_items, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            viewHolder = new ChatRVAdapter.RightChatViewHolders(itemLayoutView);
        } else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_row_left_items, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            viewHolder = new ChatRVAdapter.LeftChatViewHolders(itemLayoutView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            RightChatViewHolders rightChatViewHolders = (RightChatViewHolders) holder;
            rightChatViewHolders.txtMessage.setText(data.get(position).getMessage());
            // String time = data.get(position).getDate().substring(data.get(position).getDate().indexOf(' ') + 1);
            String time = data.get(position).getDate();
            if (data.get(position).getChatType().equals("Image")) {
                rightChatViewHolders.imvChatMessage.setVisibility(View.VISIBLE);
                rightChatViewHolders.txtMessage.setVisibility(View.GONE);
                if (data.get(position).getMessage().equals("")) {
                    rightChatViewHolders.progressbar.setVisibility(View.VISIBLE);
                } else {
                    rightChatViewHolders.progressbar.setVisibility(View.GONE);
                }
                Glide.with(context).load(data.get(position).getMessage()).into(rightChatViewHolders.imvChatMessage);
                rightChatViewHolders.imvChatMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZoomAnimation zoomAnimation = new ZoomAnimation((Activity) context);
                        zoomAnimation.zoom(v, 500);
                    }
                });
            } else {
                rightChatViewHolders.progressbar.setVisibility(View.GONE);
                rightChatViewHolders.txtMessage.setVisibility(View.VISIBLE);
                rightChatViewHolders.txtTime.setText(time);
                rightChatViewHolders.imvChatMessage.setVisibility(View.GONE);
            }
        } else {
            LeftChatViewHolders leftChatViewHolders = (LeftChatViewHolders) holder;
            leftChatViewHolders.txtMessage.setText(data.get(position).getMessage());
            // String time = data.get(position).getDate().substring(data.get(position).getDate().indexOf(' ') + 1);
            String time = data.get(position).getDate();
            if (data.get(position).getChatType().equals("Image")) {
                leftChatViewHolders.imvChatMessage.setVisibility(View.VISIBLE);
                leftChatViewHolders.txtMessage.setVisibility(View.GONE);
                Glide.with(context).load(data.get(position).getMessage()).into(leftChatViewHolders.imvChatMessage);
                if (data.get(position).getMessage().equals("")) {
                    leftChatViewHolders.progressbar.setVisibility(View.VISIBLE);
                } else {
                    leftChatViewHolders.progressbar.setVisibility(View.GONE);
                }
                leftChatViewHolders.imvChatMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ZoomAnimation zoomAnimation = new ZoomAnimation((Activity) context);
                        zoomAnimation.zoom(v, 500);
                    }
                });
            } else {
                leftChatViewHolders.progressbar.setVisibility(View.GONE);
                leftChatViewHolders.txtMessage.setVisibility(View.VISIBLE);
                leftChatViewHolders.txtTime.setText(time);
                leftChatViewHolders.imvChatMessage.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getSenderId().equals(senderId)) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class LeftChatViewHolders extends RecyclerView.ViewHolder {

        public TextView txtMessage, txtTime;
        public ImageButton imvChatMessage;
        public ProgressBar progressbar;

        public LeftChatViewHolders(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imvChatMessage = itemView.findViewById(R.id.imvChatMessage);
            progressbar = itemView.findViewById(R.id.progressbar);
        }
    }

    public class RightChatViewHolders extends RecyclerView.ViewHolder {
        public TextView txtMessage, txtTime;
        public ImageButton imvChatMessage;
        public ProgressBar progressbar;

        public RightChatViewHolders(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
            imvChatMessage = itemView.findViewById(R.id.imvChatMessage);
            progressbar = itemView.findViewById(R.id.progressbar);
        }
    }

}

