package com.muhammadelsayed.bybike_rider;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muhammadelsayed.bybike_rider.Fragments.LoginFragment;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

public class StartActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private Context mContext = StartActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            Utils.LoginFragment).commit();
        }
    }
}
