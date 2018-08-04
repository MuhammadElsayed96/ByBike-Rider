package com.muhammadelsayed.bybike_rider.RatingActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.muhammadelsayed.bybike_rider.ConnectionReceiver;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.grantland.widget.AutofitTextView;

public class RatingDetails extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = RatingDetails.class.getSimpleName();
    private AutofitTextView mTvStarRating;
    private String starRating;
    private SweetAlertDialog connectionLossDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(RatingDetails.this);

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
        try {
            starRating = getIntent().getStringExtra("Rider_Star_Rating");
            mTvStarRating.setText(String.format("%.2f", Float.valueOf(starRating)));
        } catch (NullPointerException e) {
            mTvStarRating.setText("0");
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        private SweetAlertDialog connectionLossDialog;
        //show a No Internet Alert or Dialog
        if (connectionLossDialog != null)
            connectionLossDialog = null;
        connectionLossDialog = new SweetAlertDialog(RatingDetails.this, SweetAlertDialog.WARNING_TYPE);
        connectionLossDialog.setCancelable(false);
        connectionLossDialog.setTitleText("Connection Loss")
                .setContentText("Connect to Internet and try again")
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        connectionLossDialog.dismissWithAnimation();
                    }
                });
        connectionLossDialog.show();
    }
}
