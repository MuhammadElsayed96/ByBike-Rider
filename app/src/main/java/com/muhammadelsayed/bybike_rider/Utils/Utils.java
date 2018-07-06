package com.muhammadelsayed.bybike_rider.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.muhammadelsayed.bybike_rider.MainActivity;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderInfoModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.StartActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    // Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Fragments Tags
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String SING_UP_FRAGMENT = "SingUpFragment";
    public static final String FORGOT_PASSWORD_FRAGMENT = "FORGOT_PASSWORD_FRAGMENT";
    public static final String EXTENDED_SIGN_UP = "ExtendedSignUpFragment";


    /**
     * This method split any name to two names, first name, last name
     *
     * @param name is the name that will be split
     * @return Strign array holding the first name, and the last name.
     */
    public static String[] splitName(String name) {
        Log.wtf(TAG, "splitName() has been instantiated");

        String[] names = name.split(" ", 2);
        return names;

    }

    public static String splitAddress(String location) {
        Log.wtf(TAG, "splitName() has been instantiated");


        String[] address = location.split(", Mansoura", 2);
        return address[0];
    }


    public static void checkUserSession(final Context context) {
        Log.wtf(TAG, "checkUserSession() has been instantiated");

        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String token = prefs.getString("RIDER_TOKEN", "");
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
                    }
                }
                @Override
                public void onFailure(Call<RiderInfoModel> call, Throwable t) {
                    Log.d(TAG, "onFailure: Failed");
                    Intent intent = new Intent(context, StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
        } else {
            Log.d(TAG, "onFailure: TO TOKEN");
            Intent intent = new Intent(context, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }

}
