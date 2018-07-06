package com.muhammadelsayed.bybike_rider.AccountActivities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.muhammadelsayed.bybike_rider.Model.RiderInfoModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.grantland.widget.AutofitTextView;

public class RiderProfile extends AppCompatActivity {

    private static final String TAG = RiderProfile.class.getSimpleName();
    private AutofitTextView riderName;
    private AutofitTextView riderTotalOrders;
    private AutofitTextView riderRating;
    private AutofitTextView totalEmployingPeriod;
    private RiderInfoModel riderInfoModel;
    private CircularImageView riderImageView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_profile);
        Log.wtf(TAG, "onCreate() has been instantiated");

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
        riderName = findViewById(R.id.tv_user_name);
        riderTotalOrders = findViewById(R.id.tv_trips_number);
        riderRating = findViewById(R.id.tv_rider_rating);
        totalEmployingPeriod = findViewById(R.id.tv_rider_employing_period);
        riderImageView = findViewById(R.id.profile_image);
        riderInfoModel = (RiderInfoModel) getIntent().getSerializableExtra("rider_Info_Model");
        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + ((RiderApplication) getApplicationContext()).getCurrentRider().getRider().getImage())
                .placeholder(R.drawable.trump)
                .error(R.drawable.trump)
                .into(riderImageView);


        String fRiderName = Utils.splitName(riderInfoModel.getRider().getName())[0];
        riderName.setText(fRiderName);
        riderTotalOrders.setText(riderInfoModel.getTotal_trips());
        riderRating.setText(riderInfoModel.getRating());

        totalEmployingPeriod.setText("1");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getEmployingPeriod(String creationTime) throws ParseException {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date creationDate = originalFormat.parse(creationTime);

        LocalDate now = LocalDate.now();
        LocalDate date = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(date, now);

        return String.valueOf(period.getMonths());
    }
}
