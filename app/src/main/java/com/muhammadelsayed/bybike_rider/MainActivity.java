package com.muhammadelsayed.bybike_rider;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.Fragments.AccountFragment;
import com.muhammadelsayed.bybike_rider.Fragments.EarningsFragment;
import com.muhammadelsayed.bybike_rider.Fragments.HomeFragment;
import com.muhammadelsayed.bybike_rider.Fragments.RatingFragment;
import com.muhammadelsayed.bybike_rider.Fragments.RequestsFragment;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Utils.RiderSharedPreferences;
import com.pusher.pushnotifications.PushNotifications;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    // Variables Declaration.
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_EARNINGS = "tag_frag_earnings";
    private static final String TAG_FRAGMENT_RATING = "tag_frag_rating";
    private static final String TAG_FRAGMENT_ACCOUNT = "tag_frag_account";
    private static final String TAG_FRAGMENT_REQUESTS = "tag_frag_request";
    private static final int INT_FRAGMENTS_COUNT = 5;
    private static final int TIME_OF_VIBRATION = 100;
    private static final int INT_FRAGMENT_HOME_POS = 0;
    private static final int INT_FRAGMENT_EARNINGS_POS = 1;
    private static final int INT_FRAGMENT_RATING_POS = 2;
    private static final int INT_FRAGMENT_ACCOUNT_POS = 3;
    private static final int INT_FRAGMENT_REQUESTS_POS = 4;
    public static final int BUTTON_STATUS_ONLINE = 1;
    public static final int BUTTON_STATUS_OFFLINE = 0;
    public static int buttonStatus = BUTTON_STATUS_OFFLINE;
    private static Button mActionbarButton;
    private ActionBar mActionBar;
    public static BottomNavigationView mBottomNavigation;
    private Toast mStateToast;
    private TextView mTvState;
    private DatabaseReference mOrdersRef;
    private QBadgeView badgeView;
    private Vibrator vibe;

    private List<Fragment> mFragmentsList = new ArrayList<>(INT_FRAGMENTS_COUNT);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchFragment(INT_FRAGMENT_HOME_POS, TAG_FRAGMENT_HOME);
                    return true;
                case R.id.navigation_earnings:
                    switchFragment(INT_FRAGMENT_EARNINGS_POS, TAG_FRAGMENT_EARNINGS);
                    return true;
                case R.id.navigation_rating:
                    switchFragment(INT_FRAGMENT_RATING_POS, TAG_FRAGMENT_RATING);
                    return true;
                case R.id.navigation_account:
                    switchFragment(INT_FRAGMENT_ACCOUNT_POS, TAG_FRAGMENT_ACCOUNT);
                    return true;
                case R.id.navigation_requests:
                    switchFragment(INT_FRAGMENT_REQUESTS_POS, TAG_FRAGMENT_REQUESTS);
                    if (badgeView != null)
                        badgeView.hide(true);
                    return true;
            }
            return false;
        }
    };
    private Button.OnClickListener mOnCheckedChangeListener = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            if (buttonStatus == BUTTON_STATUS_OFFLINE) {
                buttonStatus = BUTTON_STATUS_ONLINE;
                mActionbarButton.setText("GO OFFLINE");
                vibe.vibrate(TIME_OF_VIBRATION);
                //mActionbarButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.offline));
                mActionbarButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.offline)));
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_online), Toast.LENGTH_SHORT);
                mStateToast.show();
            } else {
                buttonStatus = BUTTON_STATUS_OFFLINE;
                mActionbarButton.setText("GO ONLINE");
                vibe.vibrate(TIME_OF_VIBRATION);
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_offline), Toast.LENGTH_SHORT);
                //mActionbarButton.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorAccent));
                mActionbarButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                mStateToast.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.wtf(TAG, "onCreate() has been instantiated");
        PushNotifications.start(getApplicationContext(), "f003204c-30be-49af-b706-928d5d51ed69");
        PushNotifications.subscribe("hello");
        fillRiderApplication();

        RiderModel currentRider = (RiderModel) getIntent().getSerializableExtra("current_rider");


        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mBottomNavigation = findViewById(R.id.bottom_navigation_view);
        mBottomNavigation.getMenu().getItem(0).setChecked(true);
        mBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(mBottomNavigation);
        buildFragmentsList();
        // Set the 0th Fragment to be displayed by default.
        switchFragment(0, TAG_FRAGMENT_HOME);

        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.switch_layout);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);


        mActionbarButton = findViewById(R.id.button_actionbar);
        mActionbarButton.setOnClickListener(mOnCheckedChangeListener);


        mOrdersRef = FirebaseDatabase.getInstance().getReference("orders");
        mOrdersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                badgeView = new QBadgeView(getApplicationContext());
                badgeView.bindTarget(MainActivity.mBottomNavigation).setBadgeGravity(Gravity.TOP | Gravity.END).setBadgeText("new");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillRiderApplication() {
        Log.wtf(TAG, "fillRiderApplication() has been instantiated");
        RiderModel riderModel = RiderSharedPreferences.ReadFromPreferences(this);
        ((RiderApplication) this.getApplication()).setCurrentRider(riderModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf(TAG, "onStart() has been instantiated");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.wtf(TAG, "onResume() has been instantiated");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.wtf(TAG, "onPause() has been instantiated");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.wtf(TAG, "onStop() has been instantiated");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.wtf(TAG, "onRestart() has been instantiated");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.wtf(TAG, "onDestroy() has been instantiated");
    }

    /**
     * Use this  method to create a new instance of
     * any of the fragments using the fragments' factory methods.
     * <p>
     * Then, the method puts the created fragments in the "mFragmentsList"
     */
    private void buildFragmentsList() {
        Log.wtf(TAG, "buildFragmentsList() has been instantiated");

        HomeFragment homeFragment = HomeFragment.homeFragmentInstance();
        EarningsFragment earningsFragment = EarningsFragment.earningsFragmentInstance("Earnings Fragment");
        RatingFragment ratingFragment = RatingFragment.ratingFragmentInstance("Rating Fragment");
        AccountFragment accountFragment = AccountFragment.accountFragmentInstance("Account Fragment");
        RequestsFragment requestsFragment = RequestsFragment.requestsFragmentInstance("Requests Fragment");

        mFragmentsList.add(homeFragment);
        mFragmentsList.add(earningsFragment);
        mFragmentsList.add(ratingFragment);
        mFragmentsList.add(accountFragment);
        mFragmentsList.add(requestsFragment);
    }

    /**
     * This generic method is used to handle switching between fragments.
     *
     * @param pos The position of fragment at the "mFragmentList".
     * @param tag The tag name for the fragment.
     */
    private void switchFragment(int pos, String tag) {
        Log.wtf(TAG, "switchFragment() has been instantiated");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_fragment_holder, mFragmentsList.get(pos), tag)
                .commit();
    }

    /**
     * This method will force the BottomNavigationView to show both the icon and the label
     * of each element in the BottomNavigationView, not only the highlighted element
     * <p>
     * I got this method from STACKOVERFLOW.com and here's the link
     * see <a href="https://stackoverflow.com/questions/41352934/force-showing-icon-and-title-in-bottomnavigationview-support-android/41374515"</a>
     *
     * @param view is the BottomNavigationView object on which the force showing will be applied
     */
    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        Log.wtf(TAG, "disableShiftMode() has been instantiated");

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            //Timber.e(e, "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            //Timber.e(e, "Unable to change value of shift mode");
        }
    }

}
