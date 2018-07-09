package com.muhammadelsayed.bybike_rider.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.DriverTracking;
import com.muhammadelsayed.bybike_rider.Model.OrderInfoModel;
import com.muhammadelsayed.bybike_rider.Model.Orders;
import com.muhammadelsayed.bybike_rider.Model.TripModel;
import com.muhammadelsayed.bybike_rider.Model.TripResponse;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Maps;

import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * STACKOVERFLOW
 * https://stackoverflow.com/questions/22512833/create-listview-in-fragment-android
 */
public class OrdersAdapter extends BaseAdapter {

    private static final String TAG = OrdersAdapter.class.getSimpleName();
    private List<Orders> ordersList;
    private LayoutInflater inflater;
    private Context context;
    private SweetAlertDialog orderAlreadyTakenDialog;


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
        Log.wtf(TAG, "getView() has been instantiated");

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

        double senderLat = Double.valueOf(ordersList.get(position).getSender_Lat());
        double senderLng = Double.valueOf(ordersList.get(position).getSender_Lng());
        double receiverLat = Double.valueOf(ordersList.get(position).getReceiver_lat());
        double receiverLng = Double.valueOf(ordersList.get(position).getReceiver_lng());
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
//                DatabaseReference mOrderRef = FirebaseDatabase.getInstance().getReference("orders").child(childId).child("status");


                final Orders order = ordersList.get(position);


                final String riderToken = ((RiderApplication) context.getApplicationContext()).getCurrentRider().getToken();
//                Toast.makeText(context.getApplicationContext(), riderToken, Toast.LENGTH_SHORT).show();
                RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
                Call<TripResponse> call = service.takeOrder(new TripModel(riderToken, order.getUuid()));
                call.enqueue(new Callback<TripResponse>() {
                    @Override
                    public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {

                        Log.d(TAG, response.body().getMessage());
//                        Toast.makeText(context, "Trip Accepted", Toast.LENGTH_LONG).show();
                        if (!response.body().getStatus()) {
                            if (orderAlreadyTakenDialog != null)
                                orderAlreadyTakenDialog = null;
                            orderAlreadyTakenDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                            orderAlreadyTakenDialog.setCancelable(false);
                            orderAlreadyTakenDialog
                                    .setTitleText("Order")
                                    .setContentText("Order has been taken!")
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    });
                            orderAlreadyTakenDialog.show();
                        } else {
                            // perform order info call to the server.


                            RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
                            Call<OrderInfoModel> orderInfoCall = service.getOrderInfo(new TripModel(riderToken, order.getUuid()));
                            orderInfoCall.enqueue(new Callback<OrderInfoModel>() {
                                @Override
                                public void onResponse(Call<OrderInfoModel> call, Response<OrderInfoModel> response) {
                                    Log.d(TAG, response.body().toString());
//                                Toast.makeText(context.getApplicationContext(), "get order info Success", Toast.LENGTH_LONG).show();
                                    OrderInfoModel orderInfo = response.body();
                                    orderInfo.getTransporter().setApi_token(riderToken);
                                    Intent intent = new Intent(context, DriverTracking.class);
                                    intent.putExtra("order_info_model", orderInfo);
                                    context.startActivity(intent);
//                                ((Activity) context).finish();
                                }

                                @Override
                                public void onFailure(Call<OrderInfoModel> call, Throwable t) {

                                }
                            });
                        }
                        // Send notification to the user informing him that rider accepted his location
                    }

                    @Override
                    public void onFailure(Call<TripResponse> call, Throwable t) {

                    }
                });
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
