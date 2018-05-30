package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.Model.UserModel;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

public class EditAccount extends AppCompatActivity {

    private LinearLayout mLlEditPhone;
    private LinearLayout mLlEditEmail;
    private TextView mTvPhoneInfo;
    private TextView mTvEmailInfo;
    private TextView mTvFname;
    private TextView mTvLname;
    private UserModel currentUser;

    private static final int FIRST_NAME_INDEX = 0;
    private static final int LAST_NAME_INDEX = 1;


    private LinearLayout.OnClickListener mOnLlEditPhoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), EditAccountPhone.class);
            intent.putExtra("user_phone", currentUser.getUser().getPhone());
            startActivity(intent);
        }
    };
    private LinearLayout.OnClickListener mOnLlEditEmailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), EditAccountEmail.class);
            intent.putExtra("user_email", currentUser.getUser().getEmail());
            startActivity(intent);
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
        mLlEditEmail = findViewById(R.id.email_layout);
        mLlEditPhone.setOnClickListener(mOnLlEditPhoneClickListener);
        mLlEditEmail.setOnClickListener(mOnLlEditEmailClickListener);

        currentUser = (UserModel) getIntent().getSerializableExtra("current_user");

        mTvPhoneInfo = findViewById(R.id.phone_info);
        mTvEmailInfo = findViewById(R.id.email_info);
        mTvFname = findViewById(R.id.first_name_info);
        mTvLname = findViewById(R.id.last_name_info);

        String[] names = Utils.splitName(currentUser.getUser().getName());
        String fName = names[FIRST_NAME_INDEX];
        String lName = names[LAST_NAME_INDEX];

        mTvFname.setText(fName);
        mTvLname.setText(lName);

        mTvPhoneInfo.setText(currentUser.getUser().getPhone());
        mTvEmailInfo.setText(currentUser.getUser().getEmail());
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
