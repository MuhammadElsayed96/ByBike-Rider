package com.muhammadelsayed.bybike_rider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderInfoModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.wtf(TAG, "onCreate() has been instantiated");

        checkUserSession();
    }


    private void checkUserSession() {
        Log.wtf(TAG, "checkUserSession() has been instantiated");

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString("RIDER_TOKEN", "");

        Log.d(TAG, "checkUserSession: TOKEN = " + token);
        if (!token.equals("")) {

            Rider rider = new Rider();
            rider.setApi_token(token);

            RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                    .create(RiderClient.class);
            Call<RiderInfoModel> call = service.getRiderInfo(rider);

            call.enqueue(new Callback<RiderInfoModel>() {
                @Override
                public void onResponse(@NonNull Call<RiderInfoModel> call, Response<RiderInfoModel> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "onResponse: " + response.body());

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<RiderInfoModel> call, Throwable t) {

                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}