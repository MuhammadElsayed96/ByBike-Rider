package com.muhammadelsayed.bybike_rider.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.Maps;

import java.io.IOException;
import java.util.List;

/**
 * STACKOVERFLOW
 * https://stackoverflow.com/questions/22512833/create-listview-in-fragment-android
 * */
public class OrdersAdapter extends BaseAdapter {

    private List<Orders> ordersList;
    private LayoutInflater inflater;
    private Context context;

    public OrdersAdapter(@NonNull Context context, List<Orders> ordersList) {
        inflater = LayoutInflater.from(context);
        this.ordersList = ordersList;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_view, null);
            holder = new ViewHolder();
            holder.senderLocationTv = convertView.findViewById(R.id.sender_location_tv);
            holder.receiverLocationTv = convertView.findViewById(R.id.receiver_location_tv);
            holder.acceptOrderBtn = convertView.findViewById(R.id.accept_order_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        double senderLat = ordersList.get(position).getSender_Lat();
        double senderLng = ordersList.get(position).getSender_Lng();
        double receiverLat = ordersList.get(position).getReceiver_lat();
        double receiverLng = ordersList.get(position).getReceiver_lng();
        String senderAddress = "";
        String receiverAddress = "";
        try {
            senderAddress = Maps.getAddressFromCoordinates(context, senderLat, senderLng);
            receiverAddress = Maps.getAddressFromCoordinates(context, receiverLat, receiverLng);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.senderLocationTv.setText(senderAddress);
        holder.receiverLocationTv.setText(receiverAddress);

        holder.acceptOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String childId = String.valueOf(ordersList.get(position).getId());
                DatabaseReference mOrderRef = FirebaseDatabase.getInstance().getReference("orders").child(childId);

                Orders order = ordersList.get(position);
                order.setStatus(1);
                mOrderRef.setValue(order);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView senderLocationTv;
        TextView receiverLocationTv;
        Button acceptOrderBtn;
    }
}
