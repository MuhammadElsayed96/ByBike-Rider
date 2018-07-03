package com.muhammadelsayed.bybike_rider.Network;

import com.muhammadelsayed.bybike_rider.Model.OrderInfoModel;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderInfoModel;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Model.RiderRateModel;
import com.muhammadelsayed.bybike_rider.Model.SignUpResponse;
import com.muhammadelsayed.bybike_rider.Model.TripModel;
import com.muhammadelsayed.bybike_rider.Model.TripResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RiderClient {

    @POST("api/rider/login")
    Call<RiderModel> loginRider(@Body Rider rider);

    @POST("api/rider/rate")
    Call<RiderRateModel> getRiderRatings(@Body Rider riderToken);

    @POST("api/rider/info")
    Call<RiderInfoModel> getRiderInfo(@Body Rider riderToken);

    @POST("api/rider/order/take")
    Call<TripResponse> takeOrder(@Body TripModel tripModel);

    @POST("api/rider/order/approve")
    Call<TripResponse> approveOrder(@Body TripModel tripModel);

    @POST("api/rider/order/receive")
    Call<TripResponse> receiveOrder(@Body TripModel tripModel);

    @POST("api/rider/order/info")
    Call<OrderInfoModel> getOrderInfo(@Body TripModel tripModel);

    @POST("api/rider/update")
    Call<RiderModel> updateRiderData(@Body Rider rider);

    @POST("api/rider/update/password")
    Call<RiderModel> updateRiderPassword(@Body Rider rider);

    @Multipart
    @POST("api/rider/update")
    Call<RiderModel> updateRiderProfileImage(@Part("api_token") RequestBody api_token, @Part MultipartBody.Part image);

    @POST("api/user")
    Call<SignUpResponse> signUpRider(@Body Rider user);

}
