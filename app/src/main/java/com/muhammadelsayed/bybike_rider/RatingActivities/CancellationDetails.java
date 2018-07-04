package com.muhammadelsayed.bybike_rider.RatingActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.muhammadelsayed.bybike_rider.R;

import me.grantland.widget.AutofitTextView;

public class CancellationDetails extends AppCompatActivity {


    private AutofitTextView mTvCancellationRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_details);
        setupWidgets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvCancellationRate = findViewById(R.id.cancellation_rate);

        String cancellationRate = getIntent().getStringExtra("Rider_Cancellation_Rate");
        mTvCancellationRate.setText(cancellationRate + "%");
    }
}
