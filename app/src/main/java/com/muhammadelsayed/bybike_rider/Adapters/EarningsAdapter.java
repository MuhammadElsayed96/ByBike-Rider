package com.muhammadelsayed.bybike_rider.Adapters;

import android.content.Context;
import android.text.TextUtils;
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

public class EarningsAdapter extends BaseAdapter {
    private static final String TAG = EarningsAdapter.class.getSimpleName();
    private List<Earnings> earningsList;
    private LayoutInflater inflater;
    private Context context;

    public EarningsAdapter(Context context, List<Earnings> earningsList) {
        inflater = LayoutInflater.from(context);
        this.earningsList = earningsList;
        this.context = context;
    }

    private static String formatTime(String tripTime) throws ParseException {
        DateFormat formatter
                = new SimpleDateFormat("MM/dd/yy 'at' h:mm a");
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = originalFormat.parse(tripTime);
        return formatter.format(date);
    }

    @Override
    public int getCount() {
        return earningsList.size();
    }

    @Override
    public Object getItem(int position) {
        return earningsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        Earnings earnings = earningsList.get(position);

        String clientName = earnings.getOrder().getUser().getName();
        String tripCost = earnings.getTotal_cost();
        String tripTime = earnings.getCreated_at();
        String riderRate = "";
        try {
            riderRate = earnings.getOrder().getRate().getRate();
        } catch (Exception e) {
            riderRate = "0";
        }


        holder.tripClientTv.setText(clientName);
        holder.tripCostTv.setText("L.E. " + tripCost);
        try {
            holder.tripDateTv.setText(formatTime(tripTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(riderRate))
            holder.tripStarRatingBar.setRating(0);
        else
            holder.tripStarRatingBar.setRating(Float.valueOf(riderRate));
        return convertView;
    }

    static class ViewHolder {
        TextView tripCostTv;   //trip_cost_tv
        TextView tripClientTv; //trip_client_tv
        TextView tripDateTv;   //trip_date_tv
        MaterialRatingBar tripStarRatingBar; // trip_rating_bar
    }

}



