package com.os.imars.operator.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.os.imars.R;

public class CustomShipSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    //private List<RatingsCategoryData.ResponseBean.DataBean> dataBeans;

    public CustomShipSpinnerAdapter(Context context/*, List<RatingsCategoryData.ResponseBean.DataBean> dataBeans*/) {
       // this.dataBeans = dataBeans;
        activity = context;
    }


    public int getCount() {
        //return dataBeans.size();
        return 10;
    }

    public Object getItem(int i) {
        //return dataBeans.get(i);
        return 0;
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(14, 14, 14, 14);
        txt.setTextSize(14);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(/*dataBeans.get(position).getCategory()*/"Ship name");
        txt.setTextColor(activity.getResources().getColor(R.color.grey_700)/*Color.parseColor("#BF616161")*/);
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.CENTER | Gravity.LEFT);
        txt.setPadding(14, 14, 14, 14);
        txt.setTextSize(14);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.drop_down, 0);
        txt.setText(/*dataBeans.get(i).getCategory()*/"Ship Name");
        txt.setCompoundDrawablePadding(10);
        txt.setTextColor(activity.getResources().getColor(R.color.grey_700/*Color.parseColor("#BDBDBD")*/));
        return txt;
    }

}