package com.muhammadelsayed.bybike_rider.RatingActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.muhammadelsayed.bybike_rider.R;

import me.grantland.widget.AutofitTextView;

public class RatingDetails extends AppCompatActivity {

    private static final String TAG = RatingDetails.class.getSimpleName();
    private AutofitTextView mTvStarRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        Log.wtf(TAG, "onCreate() has been instantiated");

        setupWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.wtf(TAG, "onOptionsItemSelected() has been instantiated");

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvStarRating = findViewById(R.id.star_rate);

        String starRating = getIntent().getStringExtra("Rider_Star_Rating");
        mTvStarRating.setText(starRating);
    }
}
