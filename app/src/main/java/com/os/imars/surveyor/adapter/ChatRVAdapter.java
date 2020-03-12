package com.os.imars.surveyor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;

public class ChatRVAdapter extends RecyclerView.Adapter<ChatRVAdapter.ChatViewHolders> {

    public Context context;
    //private List<BidListData.ResponseBean.DataBean.BidUsersBean> data;
    private View.OnClickListener onClickListener;
    private DisplayImageOptions options;


    public ChatRVAdapter(Context context/*, List<BidListData.ResponseBean.DataBean.BidUsersBean> data,View.OnClickListener onClickListener*/) {
        this.context = context;
        this.onClickListener = onClickListener;
        //this.data = data;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public ChatRVAdapter.ChatViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = null;
        if (viewType == 0) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_row_left_items, null);
        } else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_row_right_items, null);
        }
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        ChatRVAdapter.ChatViewHolders viewHolder = new ChatRVAdapter.ChatViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChatRVAdapter.ChatViewHolders holder, int position) {
        //ImageLoader.getInstance().displayImage(data.get(position).getProvider_profile_image(), holder.imvDriverProfile, options);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;

        } else {
            return 1;
        }
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class ChatViewHolders extends RecyclerView.ViewHolder {


        public ChatViewHolders(View itemView) {
            super(itemView);
        }
    }
}

