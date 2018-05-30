package com.muhammadelsayed.bybike_rider.Network;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Model.RiderRateModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RiderClient {

    @POST("rider/login")
    Call<RiderModel> loginRider(@Body Rider rider);

    @POST("rider/rate")
    Call<RiderRateModel> getRiderRatings(@Body Rider riderToken);
}
