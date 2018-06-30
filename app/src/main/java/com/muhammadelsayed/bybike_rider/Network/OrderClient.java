package com.muhammadelsayed.bybike_rider.Network;

import com.muhammadelsayed.bybike_rider.Model.OrderModel;

import retrofit2.Call;
import retrofit2.http.POST;

public interface OrderClient {


    @POST("rider/order/take")
    Call<Void> takeOrder(OrderModel order);

    @POST("rider/order/approve")
    Call<Void> approveOrder(OrderModel order);

    @POST("rider/order/receive")
    Call<Void> recieveOrder(OrderModel order);


}
