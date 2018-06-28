package com.muhammadelsayed.bybike_rider.Firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.Maps;

import java.io.IOException;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * STACKOVERFLOW
 * https://stackoverflow.com/questions/22512833/create-listview-in-fragment-android
 */
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;
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

        QBadgeView badgeFrom = new QBadgeView(context);
        badgeFrom.setBadgeText(" ").setBadgeBackgroundColor(Color.RED).bindTarget(holder.senderLocationTv).setBadgeGravity(Gravity.TOP | Gravity.START);
        QBadgeView badgeTo = new QBadgeView(context);
        badgeTo.setBadgeText(" ").setBadgeBackgroundColor(Color.GREEN).bindTarget(holder.receiverLocationTv).setBadgeGravity(Gravity.TOP | Gravity.START);

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

        holder.senderLocationTv.setText(context.getString(R.string.Space) + senderAddress);
        holder.receiverLocationTv.setText(context.getString(R.string.Space) + receiverAddress);
        holder.acceptOrderBtn.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                String childId = String.valueOf(ordersList.get(position).getId());
                DatabaseReference mOrderRef = FirebaseDatabase.getInstance().getReference("orders").child(childId);

                Orders order = ordersList.get(position);
                order.setStatus(1);
                mOrderRef.setValue(order);
            }
        });

        holder.acceptOrderBtn.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                holder.acceptOrderBtn.setEnabled(false);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView senderLocationTv;
        TextView receiverLocationTv;
        SwipeButton acceptOrderBtn;
    }
}
