package com.muhammadelsayed.bybike_rider.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.R;

import java.util.List;


/*
* STACKOVERFLOW
* https://stackoverflow.com/questions/22512833/create-listview-in-fragment-android
* */

public class OrdersAdapter extends BaseAdapter {

    private List<Orders> ordersList;
    private LayoutInflater inflater;

    public OrdersAdapter(@NonNull Context context, List<Orders> ordersList) {

        inflater = LayoutInflater.from(context);
        this.ordersList = ordersList;
    }

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_view, null);
            holder = new ViewHolder();
            holder.textid = convertView.findViewById(R.id.requestIdTv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textid.setText(String.valueOf(ordersList.get(position).getId()));

        return convertView;
    }

    static class ViewHolder {
        TextView textid;
    }
}
