package com.os.imars.operator.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.os.imars.R;
import com.os.imars.custom.CircleImageView;
import com.os.imars.operator.activity.OperatorHomeActivity;
import com.os.imars.operator.activity.VesselDetailsActivity;
import com.os.imars.operator.dao.vessel.VesselData;
import com.os.imars.operator.fragment.VesselsFragment;
import com.os.imars.views.BaseView.Env;

import java.util.ArrayList;
import java.util.List;

public class VesselsRVAdapter extends RecyclerView.Adapter<VesselsRVAdapter.VesselsViewHolders> implements Filterable {

    public Context context;
    private List<VesselData> list;
    private List<VesselData> filteredList;
    private int selectedPosition = -1;
    private View.OnClickListener onClickListener;
    private static boolean flag = true;


    public VesselsRVAdapter(Context context, List<VesselData> list, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.filteredList = list;
        this.onClickListener = onClickListener;
    }

    @Override
    public VesselsRVAdapter.VesselsViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vessels_row_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        VesselsRVAdapter.VesselsViewHolders viewHolder = new VesselsRVAdapter.VesselsViewHolders(itemLayoutView);
        return viewHolder;
    }

    public void updateItem(VesselData data) {
        Log.d("1234", "updateItem: " + data.getIsFavourite());
        filteredList.set(selectedPosition, data);
        notifyDataSetChanged();
    }

    public void addItem(VesselData data) {
        filteredList.add(list.size(), data);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(VesselsRVAdapter.VesselsViewHolders holder, final int position) {
        //  ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.imvVessel, options);

        VesselData vesselData = filteredList.get(position);

        if (filteredList != null) {
            if (vesselData.getIsFavourite().equals("1")) {
                holder.imgFav.setImageResource(R.drawable.vessels_wishlist);
            } else {
                holder.imgFav.setImageResource(R.drawable.heart_unselected);
            }
        }

        holder.imgFav.setTag(position);
        holder.imgFav.setOnClickListener(onClickListener);
        holder.txtName.setText(filteredList.get(position).getName());
        holder.txtIMO.setText("IMO Number: #" + list.get(position).getImo_number());
        holder.rlDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                VesselData vesselData = filteredList.get(position);
                Intent intent = new Intent(Env.currentActivity, VesselDetailsActivity.class);
                intent.putExtra("vesselData", vesselData);
                ((OperatorHomeActivity) context).startActivityForResult(intent, 102);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                Log.d("1234", "performFiltering: " + charSequence);
                String charString = "", type = "";
                String filterData = charSequence.toString();
                if (!filterData.equals("")) {
                    String str[] = filterData.split("/");
                    charString = str[0];
                    type = str[1];
                }
                filteredList = list;
                if (charString.isEmpty()) {
                    filteredList = list;
                } else {
                    List<VesselData> vesselList = new ArrayList<>();
                    Log.d("1234", "performFiltering: 1" + filteredList.size());
                    for (VesselData row : filteredList) {
                        Log.d("1234", "performFiltering:2 " + row.getIsFavourite());
                        if (type.equals("fav")) {
                            if (row.getIsFavourite().contains(charString)) {
                                Log.d("1234", "performFiltering:3 " + row.getIsFavourite());
                                vesselList.add(row);
                            }
                        } else {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                Log.d("1234", "performFiltering:4 " + row.getIsFavourite());
                                vesselList.add(row);
                            }
                        }
                    }

                    filteredList = vesselList;
                    Log.d("1234", "performFiltering: " + filteredList.size());
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<VesselData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class VesselsViewHolders extends RecyclerView.ViewHolder {
        private RelativeLayout rlDetailsView;
        private CircleImageView imvVessel;
        private TextView txtName, txtIMO;
        private ImageView imgFav, imgAppoint;

        public VesselsViewHolders(View itemView) {
            super(itemView);
            rlDetailsView = (RelativeLayout) itemView.findViewById(R.id.rlDetailsView);
            imvVessel = itemView.findViewById(R.id.imvVessels);
            txtIMO = itemView.findViewById(R.id.txtIMO);
            txtName = itemView.findViewById(R.id.txtName);
            imgFav = itemView.findViewById(R.id.imgFav);
            imgAppoint = itemView.findViewById(R.id.imgAppoint);
        }
    }
}
