package com.os.imars.operator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.os.imars.R;
import com.os.imars.operator.dao.surveyor.BiddingUserItem;

import java.util.List;

public class AcceptBidUserAdapter extends ArrayAdapter {

    public Context context;
    private List<BiddingUserItem> userDataItemList;
    private int resource;


    public AcceptBidUserAdapter(@NonNull Context context, int resource, List<BiddingUserItem> userDataItemList) {
        super(context, resource, userDataItemList);
        this.context = context;
        this.userDataItemList = userDataItemList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.survey_bid_user_row, null, false);
        final BiddingUserItem userDataItem = userDataItemList.get(position);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvPrice = view.findViewById(R.id.txtPrice);
        ImageView imvSelectTick = view.findViewById(R.id.imvSelectTick);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        if (userDataItem.getCompanyName().equals("")) {
            tvName.setText(userDataItem.getName());
        } else {
            tvName.setText(userDataItem.getName() + " - " + userDataItem.getCompanyName());
        }
        tvPrice.setText("$ " + userDataItem.getAmount());
        if (!userDataItem.getRating().equals("")){
            ratingBar.setRating(Float.parseFloat(userDataItem.getRating()));
        }


        if (userDataItem.isSelected())
            imvSelectTick.setVisibility(View.VISIBLE);
        else
            imvSelectTick.setVisibility(View.GONE);
        return view;
    }
}
