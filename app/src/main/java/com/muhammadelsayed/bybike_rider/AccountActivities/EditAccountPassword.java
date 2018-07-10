package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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

public class EditAccountPassword extends AppCompatActivity implements ConnectionReceiver.ConnectionReceiverListener {

    private static final String TAG = EditAccountPassword.class.getSimpleName();
    private EditText mEtPassword;
    private CardView updatePasswordCv;
    private String password;
    private Rider rider;
    private RiderModel currentRider;
    private ProgressDialog dialog;
    private SweetAlertDialog connectionLossDialog;

    private CardView.OnClickListener mOnCvUpdatePassword = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "Update Password Clicked");
            if (checkValidation()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);

                dialog.show();
                RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(RiderClient.class);

                String token = ((RiderApplication) getApplication()).getCurrentRider().getToken();
                rider = ((RiderApplication) getApplication()).getCurrentRider().getRider();
                rider.setApi_token(token);
                rider.setPassword(password);

                Call<RiderModel> call = service.updateRiderPassword(rider);

                Log.d(TAG, "onClick: Token = " + rider.getApi_token());
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
                            Toast.makeText(getApplicationContext(), "Password updated successfully", Toast.LENGTH_LONG).show();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<RiderModel> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to update password!", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_edit_account_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(EditAccountPassword.this);


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

        mEtPassword = findViewById(R.id.et_edit_password);
        updatePasswordCv = findViewById(R.id.update_password_cardview);
        updatePasswordCv.setOnClickListener(mOnCvUpdatePassword);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
    }


    private boolean checkValidation() {
        Log.wtf(TAG, "checkValidation() has been instantiated");

        boolean isValid = true;

        // Get all edittext texts

        password = mEtPassword.getText().toString();


        // Check if all strings are null or not
        if (password.length() == 0) {

            isValid = false;
            new CustomToast().showToast(this, mEtPassword,
                    "Password is required.");
        }

        // Check if password id valid or not
        else if (password.length() < 6) {

            isValid = false;
            new CustomToast().showToast(this, mEtPassword,
                    "Password is too small!.");
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
