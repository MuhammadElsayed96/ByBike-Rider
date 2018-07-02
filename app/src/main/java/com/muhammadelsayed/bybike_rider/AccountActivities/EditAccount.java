package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

public class EditAccount extends AppCompatActivity {

    private LinearLayout mLlEditPhone;
    private LinearLayout mLlEditPassword;
    private TextView mTvPhoneInfo;
    private TextView mTvEmailInfo;
    private TextView mTvFname;
    private TextView mTvLname;
    private RiderModel currentRider;

    private static final int FIRST_NAME_INDEX = 0;
    private static final int LAST_NAME_INDEX = 1;
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

        setupWidgets();

    }

    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLlEditPhone = findViewById(R.id.phone_layout);
        mLlEditPassword = findViewById(R.id.password_layout);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
        mLlEditPassword.setOnClickListener(mOnLlEditPasswordClickListener);

        currentRider = ((RiderApplication) getApplication()).getCurrentRider();

        mTvPhoneInfo = findViewById(R.id.phone_info);
        mTvEmailInfo = findViewById(R.id.email_info);
        mTvFname = findViewById(R.id.first_name_info);
        mTvLname = findViewById(R.id.last_name_info);

        String[] names = Utils.splitName(currentRider.getRider().getName());
        String fName = names[FIRST_NAME_INDEX];
        String lName = names[LAST_NAME_INDEX];

        mTvFname.setText(fName);
        mTvLname.setText(lName);

        mTvPhoneInfo.setText(currentRider.getRider().getPhone());
        mTvEmailInfo.setText(currentRider.getRider().getEmail());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_PHONE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                currentRider = ((RiderApplication) getApplication()).getCurrentRider();
                mTvPhoneInfo.setText(currentRider.getRider().getPhone());
            }
        }
    }
}
