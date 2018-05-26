package com.muhammadelsayed.bybike_rider.Network;

import com.muhammadelsayed.bybike_rider.Model.User;
import com.muhammadelsayed.bybike_rider.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/rider/login/")
    Call<UserModel> loginUser(@Body User user);
}
