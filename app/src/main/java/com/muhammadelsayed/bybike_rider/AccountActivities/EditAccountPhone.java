package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.muhammadelsayed.bybike_rider.R;

public class EditAccountPhone extends AppCompatActivity {

    private static final String TAG = EditAccountPhone.class.getSimpleName();
    private EditText mEtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_phone);
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
        mEtPhone = findViewById(R.id.et_edit_phone);

        String phone = getIntent().getStringExtra("user_phone");
        mEtPhone.setText(phone);
    }

}
