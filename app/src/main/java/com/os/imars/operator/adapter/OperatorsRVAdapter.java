package com.os.imars.operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.activity.OperatorDetailsActivity;
import com.os.imars.operator.activity.OperatorEditActivity;
import com.os.imars.operator.activity.OperatorListActvitity;
import com.os.imars.operator.dao.userInfo.UserDataItem;
import com.os.imars.views.BaseView.Env;

import java.util.List;

public class OperatorsRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    private List<UserDataItem> list;
    private DisplayImageOptions options;
    private static int selectedPosition = -1;
    View.OnClickListener onClickListener;

    public OperatorsRVAdapter(Context context, List<UserDataItem> list, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public void updateItem(UserDataItem data) {
        if (selectedPosition >= 0) {
            list.set(selectedPosition, data);
            notifyDataSetChanged();
        }
    }

    public void addItem(UserDataItem data) {
        list.add(list.size(), data);
        notifyDataSetChanged();
    }

    public void deleteItem() {
        if (selectedPosition >= 0) {
            list.remove(selectedPosition);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == 4) {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_operator_row_item, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            viewHolder = new OperatorsRVAdapter.ViewHolders(itemLayoutView);
        } else {
            itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_operator_unverified_row_item, null);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            viewHolder = new OperatorsRVAdapter.ViewHolders1(itemLayoutView);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //ImageLoader.getInstance().displayImage(data.get(position).getProvider_profile_image(), holder.imvDriverProfile, options);

        if (holder.getItemViewType() == 1) {
            OperatorsRVAdapter.ViewHolders1 myHolder = (OperatorsRVAdapter.ViewHolders1) holder;
            myHolder.txtEmail.setText(list.get(position).getEmail());
            myHolder.txtEmailStatus.setText("Not Verified");
            myHolder.btnResend.setOnClickListener(onClickListener);
            myHolder.btnResend.setTag(position);
        } else if (holder.getItemViewType() == 2) {
            OperatorsRVAdapter.ViewHolders1 myHolder = (OperatorsRVAdapter.ViewHolders1) holder;
            myHolder.txtEmail.setText(list.get(position).getEmail());
            myHolder.txtEmailStatus.setText("Not Verified");
            myHolder.btnResend.setOnClickListener(onClickListener);
            myHolder.btnResend.setTag(position);
        } else if (holder.getItemViewType() == 3) {
            OperatorsRVAdapter.ViewHolders1 myHolder = (OperatorsRVAdapter.ViewHolders1) holder;
            myHolder.txtEmail.setText(list.get(position).getEmail());
            myHolder.txtEmailStatus.setText("Email Verified");
            myHolder.btnResend.setOnClickListener(onClickListener);
            myHolder.btnResend.setTag(position);
        } else if (holder.getItemViewType() == 4) {
            OperatorsRVAdapter.ViewHolders myHolder = (OperatorsRVAdapter.ViewHolders) holder;
            myHolder.txtEmail.setText(list.get(position).getEmail());
            Glide.with(context).load(list.get(position).getProfilePic()).placeholder(R.drawable.user_icon).into(myHolder.imvOperator);
            myHolder.txtName.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
            myHolder.txtMobile.setText(list.get(position).getMobileNumber());
            myHolder.imvForwordArrow.setVisibility(View.VISIBLE);
            myHolder.imvForwordArrow.setOnClickListener(v -> {
                selectedPosition = position;
                UserDataItem responseData = list.get(position);
                Intent intent = new Intent(Env.currentActivity, OperatorEditActivity.class);
                intent.putExtra("UserDataItem", responseData);
                ((OperatorListActvitity) context).startActivityForResult(intent, 101);
            });

            myHolder.rlOperatorView.setOnClickListener(v -> {
                selectedPosition = position;
                UserDataItem responseData = list.get(position);
                Intent intent = new Intent(Env.currentActivity, OperatorDetailsActivity.class);
                intent.putExtra("UserDataItem", responseData);
                ((OperatorListActvitity) context).startActivityForResult(intent, 102);
            });

        }


/*
        if (list.get(position).getEmailVerify().equals("1") && list.get(position).getIs_signup().equals("1")) {
            Glide.with(context).load(list.get(position).getProfilePic()).placeholder(R.drawable.user_icon).into(holder.imvOperator);
            holder.txtName.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
            holder.txtMobile.setText(list.get(position).getMobileNumber());
            holder.imvForwordArrow.setVisibility(View.VISIBLE);
            holder.txtEmailStatus.setVisibility(View.GONE);
        } else if (list.get(position).getEmailVerify().equals("0") && list.get(position).getIs_signup().equals("0")) {
            // Glide.with(context).load(list.get(position).getProfilePic()).placeholder(R.drawable.user_icon).into(holder.imvOperator);
            holder.txtName.setVisibility(View.GONE);
            holder.txtMobile.setVisibility(View.GONE);
            holder.imvForwordArrow.setVisibility(View.GONE);
            holder.imvMobile.setVisibility(View.GONE);
            holder.txtEmailStatus.setVisibility(View.VISIBLE);
            holder.txtEmailStatus.setText("Not Verified");
        } else if (list.get(position).getEmailVerify().equals("0") && list.get(position).getIs_signup().equals("1")) {
            Glide.with(context).load(list.get(position).getProfilePic()).placeholder(R.drawable.user_icon).into(holder.imvOperator);
            holder.txtName.setVisibility(View.GONE);
            holder.txtMobile.setVisibility(View.GONE);
            holder.imvForwordArrow.setVisibility(View.GONE);
            holder.imvMobile.setVisibility(View.GONE);
            holder.txtEmailStatus.setVisibility(View.VISIBLE);
            holder.txtEmailStatus.setText("Not Verified");
        } else {
            holder.txtName.setVisibility(View.GONE);
            holder.txtMobile.setVisibility(View.GONE);
            holder.imvForwordArrow.setVisibility(View.GONE);
            holder.imvMobile.setVisibility(View.GONE);
            holder.txtEmailStatus.setVisibility(View.VISIBLE);
            holder.txtEmailStatus.setText("Verified");
        }*/


    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getEmailVerify().equals("0") && list.get(position).getIs_signup().equals("1")) {
            return 1;
        } else if (list.get(position).getEmailVerify().equals("0") && list.get(position).getIs_signup().equals("0")) {
            return 2;
        } else if (list.get(position).getEmailVerify().equals("1") && list.get(position).getIs_signup().equals("0")) {
            return 3;
        } else if (list.get(position).getEmailVerify().equals("1") && list.get(position).getIs_signup().equals("1")) {
            return 4;
        }
        return 0;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {

        public RelativeLayout rlOperatorView;
        public ImageView imvForwordArrow, imvMobile;
        private CircleImageView imvOperator;
        private TextView txtName, txtMobile, txtEmail;

        public ViewHolders(View itemView) {
            super(itemView);
            rlOperatorView = itemView.findViewById(R.id.rlOperatorView);
            imvForwordArrow = itemView.findViewById(R.id.imvForwordArrow);
            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            imvOperator = itemView.findViewById(R.id.imvUserProfile);
            imvMobile = itemView.findViewById(R.id.imvMobile);
        }
    }

    public class ViewHolders1 extends RecyclerView.ViewHolder {

        public RelativeLayout rlOperatorView;
        public ImageView imvForwordArrow, imvMobile;
        private CircleImageView imvOperator;
        private TextView txtName, txtMobile, txtEmail, txtEmailStatus;
        private Button btnResend;

        public ViewHolders1(View itemView) {
            super(itemView);
            rlOperatorView = itemView.findViewById(R.id.rlOperatorView);
            imvForwordArrow = itemView.findViewById(R.id.imvForwordArrow);
            txtName = itemView.findViewById(R.id.txtName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            imvOperator = itemView.findViewById(R.id.imvUserProfile);
            imvMobile = itemView.findViewById(R.id.imvMobile);
            txtEmailStatus = itemView.findViewById(R.id.txtEmailStatus);
            btnResend = itemView.findViewById(R.id.btnResend);

        }
    }

}
