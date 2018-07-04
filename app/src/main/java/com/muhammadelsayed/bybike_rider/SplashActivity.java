package com.muhammadelsayed.bybike_rider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.wtf(TAG, "onCreate() has been instantiated");

        // Start home activity

        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        // close splash activity

        finish();
    }
}
