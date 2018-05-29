package com.muhammadelsayed.bybike_rider;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.Fragments.AccountFragment;
import com.muhammadelsayed.bybike_rider.Fragments.EarningsFragment;
import com.muhammadelsayed.bybike_rider.Fragments.HomeFragment;
import com.muhammadelsayed.bybike_rider.Fragments.RatingFragment;
import com.muhammadelsayed.bybike_rider.Fragments.RequestsFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variables Declaration.
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_EARNINGS = "tag_frag_earnings";
    private static final String TAG_FRAGMENT_RATING = "tag_frag_rating";
    private static final String TAG_FRAGMENT_ACCOUNT = "tag_frag_account";
    private static final String TAG_FRAGMENT_REQUESTS = "tag_frag_request";
    private static final int INT_FRAGMENTS_COUNT = 4;
    private static final int INT_FRAGMENT_HOME_POS = 0;
    private static final int INT_FRAGMENT_EARNINGS_POS = 1;
    private static final int INT_FRAGMENT_RATING_POS = 2;
    private static final int INT_FRAGMENT_ACCOUNT_POS = 3;
    private static final int INT_FRAGMENT_REQUESTS_POS = 4;

    private Switch mActionbarSwitch;
    private ActionBar mActionBar;
    private BottomNavigationView mBottomNavigation;
    private Toast mStateToast;
    private TextView mTvState;

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
                    return true;
            }
            return false;
        }
    };
    private Switch.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                if (mStateToast != null)
                    mStateToast.cancel();
                mTvState.setText(getResources().getString(R.string.state_online));
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_online), Toast.LENGTH_SHORT);
                mStateToast.show();
            } else {
                if (mStateToast != null)
                    mStateToast.cancel();
                mTvState.setText(getResources().getString(R.string.state_offline));
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_offline), Toast.LENGTH_SHORT);
                mStateToast.show();
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mTvState = findViewById(R.id.tv_state);

        mActionbarSwitch = findViewById(R.id.switch_actionbar);
        mActionbarSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    /**
     * Use this  method to create a new instance of
     * any of the fragments using the fragments' factory methods.
     * <p>
     * Then, the method puts the created fragments in the "mFragmentsList"
     */
    private void buildFragmentsList() {
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
