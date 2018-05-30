package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.muhammadelsayed.bybike_rider.R;

public class EditAccountEmail extends AppCompatActivity {

    private static final String TAG = EditAccountEmail.class.getSimpleName();
    private EditText mEtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_email);
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
        mEtEmail = findViewById(R.id.et_edit_email);

        String email = getIntent().getStringExtra("user_email");
        mEtEmail.setText(email);
    }
}
