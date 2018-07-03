package com.muhammadelsayed.bybike_rider;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muhammadelsayed.bybike_rider.Fragments.LoginFragment;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Utils.RiderSharedPreferences;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

public class StartActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private Context mContext = StartActivity.this;
    private static final String TAG = StartActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fragmentManager = getSupportFragmentManager();

        RiderModel riderModel = RiderSharedPreferences.ReadFromPreferences(this);

        if (riderModel != null) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("current_rider", riderModel);
            startActivity(intent);
            this.finish();
        }

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            Utils.LOGIN_FRAGMENT).commit();
        }
    }

    @Override
    public void onBackPressed() {

        int count = fragmentManager.getBackStackEntryCount();

        if (count == 0 ){
            super.onBackPressed();
        }else {

            fragmentManager.popBackStackImmediate();
        }
    }
}