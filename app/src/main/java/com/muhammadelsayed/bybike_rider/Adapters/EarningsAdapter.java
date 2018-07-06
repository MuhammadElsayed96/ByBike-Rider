package com.muhammadelsayed.bybike_rider.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.Model.Earnings;
import com.muhammadelsayed.bybike_rider.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

class EarningsAdapter extends BaseAdapter {
    private static final String TAG = EarningsAdapter.class.getSimpleName();
    private List<Earnings> tripList;
    private LayoutInflater inflater;
    private Context context;

    public EarningsAdapter(Context context, List<Earnings> tripList) {
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


        String clientName = tripList.get(position).getOrder().getUser().getName();
        String tripCost = tripList.get(position).getTotal_cost();
        String tripTime = tripList.get(position).getCreated_at();
        String riderRate = tripList.get(position).getTransporterRate();


        holder.tripClientTv.setText(clientName);
        holder.tripCostTv.setText(tripCost);
        try {
            holder.tripDateTv.setText(formatTime(tripTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tripStarRatingBar.setNumStars(Integer.valueOf(riderRate));
        return convertView;
    }

    static class ViewHolder {
        TextView tripCostTv;   //trip_cost_tv
        TextView tripClientTv; //trip_client_tv
        TextView tripDateTv;   //trip_date_tv
        MaterialRatingBar tripStarRatingBar; // trip_rating_bar
    }


    private static String formatTime(String tripTime) throws ParseException {
        DateFormat formatter
                = new SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a");
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = originalFormat.parse(tripTime);
        return formatter.format(date);
    }

}



