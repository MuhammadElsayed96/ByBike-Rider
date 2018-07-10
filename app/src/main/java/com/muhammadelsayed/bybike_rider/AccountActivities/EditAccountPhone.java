package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.ConnectionReceiver;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.CustomToast;
import com.muhammadelsayed.bybike_rider.Utils.RiderSharedPreferences;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccountPhone extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = EditAccountPhone.class.getSimpleName();
    private EditText mEtPhone;
    private String phone;
    private CardView updatePhoneCv;
    private Rider rider;
    private RiderModel currentRider;
    private ProgressDialog dialog;
    private SweetAlertDialog connectionLossDialog;
    private CardView.OnClickListener mOnCvUpdatePhone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "Update Phone Clicked");
            if (checkValidation()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtPhone.getWindowToken(), 0);

                dialog.show();
                RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(RiderClient.class);

                String token = ((RiderApplication) getApplication()).getCurrentRider().getToken();
                rider = ((RiderApplication) getApplication()).getCurrentRider().getRider();
                rider.setApi_token(token);
                rider.setPhone(phone);

                Call<RiderModel> call = service.updateRiderData(rider);

                Log.d(TAG, "onClick: Token = " + rider.getApi_token());
                Log.d(TAG, "onClick: Phone = " + rider.getPhone());
                call.enqueue(new Callback<RiderModel>() {
                    @Override
                    public void onResponse(Call<RiderModel> call, Response<RiderModel> response) {
                        dialog.dismiss();
                        if (response.body() != null) {
                            Log.wtf(TAG, "onResponse: " + response.body());
                            rider = response.body().getRider();
                            String riderToken = ((RiderApplication) getApplication()).getCurrentRider().getToken();
                            currentRider = new RiderModel(riderToken, rider);
                            RiderSharedPreferences.SaveToPreferences(getApplicationContext(), currentRider);
                            Toast.makeText(getApplicationContext(), "Phone updated successfully", Toast.LENGTH_LONG).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<RiderModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to update phone!", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onFailure: Exception == " + t.getMessage() + "\nCause == " + t.getCause());
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(EditAccountPhone.this);

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

        mEtPhone = findViewById(R.id.et_edit_phone);
        updatePhoneCv = findViewById(R.id.update_phone_cardview);
        updatePhoneCv.setOnClickListener(mOnCvUpdatePhone);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

    }


    private boolean checkValidation() {
        Log.wtf(TAG, "checkValidation() has been instantiated");

        boolean isValid = true;

        // Get all edittext texts
        phone = mEtPhone.getText().toString();

        // Check if all strings are null or not
        if (phone.length() == 0) {
            isValid = false;
            new CustomToast().showToast(this, mEtPhone,
                    "Phone is required.");
        }
        if (phone.length() < 11) {
            isValid = false;
            new CustomToast().showToast(this, mEtPhone,
                    "Your phone number is Invalid..");
        }
        return isValid;
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