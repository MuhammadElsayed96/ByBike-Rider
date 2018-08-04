package com.muhammadelsayed.bybike_rider.Network;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    public static final String BASE_URL = "http://52.224.66.22/abdullah/bel3agal/bel3agal/public/";
    private static final String TAG = RetrofitClientInstance.class.getSimpleName();
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        Log.wtf(TAG, "getRetrofitInstance() has been instantiated");

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
