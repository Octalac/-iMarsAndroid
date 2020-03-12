package com.os.imars.surveyor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.os.imars.R;
import com.os.imars.surveyor.dao.PortData;

import java.util.List;

public class PortsRVAdapter extends RecyclerView.Adapter<PortsRVAdapter.ViewHolders> {

    public Context context;
    private View.OnClickListener onClickListener;
    private DisplayImageOptions options;
    private List<PortData> portDataList;


    public PortsRVAdapter(Context context, List<PortData> portDataList) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.portDataList = portDataList;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public PortsRVAdapter.ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("1234", "onBindViewHolder: "+portDataList.size());
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ports_row_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        PortsRVAdapter.ViewHolders viewHolder = new PortsRVAdapter.ViewHolders(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PortsRVAdapter.ViewHolders holder, int position) {
        Log.d("1234", "onBindViewHolder: "+portDataList.size());
        holder.tvCity.setText(portDataList.get(position).getName());
        holder.tvTransportationCost.setText("$"+portDataList.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return portDataList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        private RelativeLayout rlDetailsView;
        private TextView tvTransportationCost, tvCity;

        public ViewHolders(View itemView) {
            super(itemView);
            rlDetailsView = (RelativeLayout) itemView.findViewById(R.id.rlDetailsView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            tvTransportationCost = (TextView) itemView.findViewById(R.id.tvTransportationCost);
        }
    }
}
