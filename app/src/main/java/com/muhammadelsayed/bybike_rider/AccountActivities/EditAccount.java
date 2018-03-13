package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RatingActivities.AcceptanceDetails;

public class EditAccount extends AppCompatActivity {

    private LinearLayout mLlEditPhone;
    private LinearLayout mLlEditEmail;
    private LinearLayout mLlEditAddress;

    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditAccountPhone.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlEditEmailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditAccountEmail.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlEditAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), EditAccountAddress.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLlEditAddress = findViewById(R.id.address_layout);
        mLlEditPhone = findViewById(R.id.phone_layout);
        mLlEditEmail = findViewById(R.id.email_layout);
        mLlEditAddress.setOnClickListener(mOnLlEditAddressClickListener);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
        mLlEditEmail.setOnClickListener(mOnLlEditEmailClickListener);

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
}
