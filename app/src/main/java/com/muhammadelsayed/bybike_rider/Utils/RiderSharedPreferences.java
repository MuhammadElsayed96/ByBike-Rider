package com.muhammadelsayed.bybike_rider.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;

public class RiderSharedPreferences {
    private static final String TAG = RiderSharedPreferences.class.getSimpleName();

    public static void SaveToPreferences(Context context, RiderModel rider) {
        Log.wtf(TAG, "SaveToPreferences() has been instantiated");

        String currentRiderToken = rider.getToken();
        Rider currentRider = rider.getRider();

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putString("RIDER_TOKEN", currentRiderToken);
        prefEditor.putString("RIDER_ID", currentRider.getId());
        prefEditor.putString("RIDER_UUID", currentRider.getUuid());
        prefEditor.putString("RIDER_NAME", currentRider.getName());
        prefEditor.putString("RIDER_EMAIL", currentRider.getEmail());
        prefEditor.putString("RIDER_PASSWORD", currentRider.getPassword());
        prefEditor.putString("RIDER_PHONE", currentRider.getPhone());
        prefEditor.putString("RIDER_IMAGE", currentRider.getImage());
        prefEditor.putString("RIDER_VEHICLE_ID", currentRider.getVehicle_ID());
        prefEditor.putString("RIDER_VEHICLE_DATA", currentRider.getVehicle_Data());
        prefEditor.putString("RIDER_API_TOKEN", currentRider.getApi_token());
        prefEditor.putString("RIDER_CREATED_AT", currentRider.getCreated_at());
        prefEditor.putString("RIDER_UPDATED_AT", currentRider.getUpdated_at());
        prefEditor.apply();

        ((RiderApplication) context.getApplicationContext()).setCurrentRider(rider);
    }

    public static RiderModel ReadFromPreferences(Context context) {
        Log.wtf(TAG, "ReadFromPreferences() has been instantiated");

        RiderModel riderModel;
        String riderToken;
        String id;
        String uuid;
        String name;
        String email;
        String password;
        String phone;
        String image;
        String vehicle_ID;
        String vehicle_Data;
        String api_token;
        String created_at;
        String updated_at;
        Rider rider;

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        riderToken = sharedPref.getString("RIDER_TOKEN", "");
        id = sharedPref.getString("RIDER_ID", "");
        uuid = sharedPref.getString("RIDER_UUID", "");
        name = sharedPref.getString("RIDER_NAME", "");
        email = sharedPref.getString("RIDER_EMAIL", "");
        password = sharedPref.getString("RIDER_PASSWORD", "");
        phone = sharedPref.getString("RIDER_PHONE", "");
        image = sharedPref.getString("RIDER_IMAGE", "");
        vehicle_ID = sharedPref.getString("RIDER_VEHICLE_ID", "");
        vehicle_Data = sharedPref.getString("RIDER_VEHICLE_DATA", "");
        api_token = sharedPref.getString("RIDER_API_TOKEN", "");
        created_at = sharedPref.getString("RIDER_CREATED_AT", "");
        updated_at = sharedPref.getString("RIDER_UPDATED_AT", "");

        rider = new Rider(id, uuid, name, email, phone, image, api_token, created_at, updated_at);

        riderModel = new RiderModel(riderToken, rider);

        if (riderToken == "")
            return null;
        return riderModel;
    }
}
