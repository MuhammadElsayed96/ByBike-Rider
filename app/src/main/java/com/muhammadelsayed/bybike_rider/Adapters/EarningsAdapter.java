package com.muhammadelsayed.bybike_rider.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebanx.swipebtn.SwipeButton;
import com.muhammadelsayed.bybike_rider.Model.Orders;
import com.muhammadelsayed.bybike_rider.R;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

class EarningsAdapter extends BaseAdapter {
    private static final String TAG = EarningsAdapter.class.getSimpleName();
    private List<Orders> tripList;
    private LayoutInflater inflater;
    private Context context;

    public EarningsAdapter(Context context, List<Orders> tripList) {
        this.tripList = tripList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.wtf(TAG, "getView() has been instantiated");
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.earnings_list_item_view, null);
            holder = new EarningsAdapter.ViewHolder();
            holder.tripCostTv = convertView.findViewById(R.id.trip_cost_tv);
            holder.tripClientTv = convertView.findViewById(R.id.trip_client_tv);
            holder.tripDateTv = convertView.findViewById(R.id.trip_date_tv);
            holder.tripStarRatingBar = convertView.findViewById(R.id.trip_rating_bar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }






        return convertView;
    }

    static class ViewHolder {
        TextView tripCostTv;   //trip_cost_tv
        TextView tripClientTv; //trip_client_tv
        TextView tripDateTv;   //trip_date_tv
        MaterialRatingBar tripStarRatingBar; // trip_rating_bar
    }
}
