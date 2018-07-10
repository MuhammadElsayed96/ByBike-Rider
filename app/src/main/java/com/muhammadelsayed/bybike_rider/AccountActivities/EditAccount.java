package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.muhammadelsayed.bybike_rider.ConnectionReceiver;
import com.muhammadelsayed.bybike_rider.DriverTracking;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditAccount extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = EditAccount.class.getSimpleName();
    private LinearLayout mLlEditPhone;
    private LinearLayout mLlEditPassword;
    private TextView mTvPhoneInfo;
    private TextView mTvEmailInfo;
    private TextView mTvName;
    private CircularImageView mRiderImage;
    private RiderModel currentRider;
    private SweetAlertDialog connectionLossDialog;
    private static final int EDIT_PHONE_ACTIVITY_REQUEST_CODE = 2;
    private static final int EDIT_PASSWORD_ACTIVITY_REQUEST_CODE = 3;

    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), EditAccountPhone.class);
            startActivityForResult(intent, EDIT_PHONE_ACTIVITY_REQUEST_CODE);
            finish();
        }
    };
    private LinearLayout.OnClickListener mOnLlEditPasswordClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), EditAccountPassword.class);
            startActivityForResult(intent, EDIT_PASSWORD_ACTIVITY_REQUEST_CODE);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(EditAccount.this);

        setupWidgets();
    }

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLlEditPhone = findViewById(R.id.phone_layout);
        mLlEditPassword = findViewById(R.id.password_layout);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
        mLlEditPassword.setOnClickListener(mOnLlEditPasswordClickListener);
        mRiderImage = findViewById(R.id.profile_image);

        currentRider = ((RiderApplication) getApplication()).getCurrentRider();

        mTvPhoneInfo = findViewById(R.id.phone_info);
        mTvEmailInfo = findViewById(R.id.email_info);
        mTvName = findViewById(R.id.name_info);


        String name = currentRider.getRider().getName();
        mTvName.setText(name);

        mTvPhoneInfo.setText(currentRider.getRider().getPhone());
        mTvEmailInfo.setText(currentRider.getRider().getEmail());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf(TAG, "onStart() has been instantiated");

        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + currentRider.getRider().getImage())
                .placeholder(R.drawable.trump)
                .error(R.drawable.trump)
                .into(mRiderImage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.wtf(TAG, "onActivityResult() has been instantiated");

        if (requestCode == EDIT_PHONE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                currentRider = ((RiderApplication) getApplication()).getCurrentRider();
                mTvPhoneInfo.setText(currentRider.getRider().getPhone());
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        private SweetAlertDialog connectionLossDialog;
        //show a No Internet Alert or Dialog
        if (connectionLossDialog != null)
            connectionLossDialog = null;
        connectionLossDialog = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.WARNING_TYPE);
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
