package com.muhammadelsayed.bybike_rider;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.Fragments.AccountFragment;
import com.muhammadelsayed.bybike_rider.Fragments.EarningsFragment;
import com.muhammadelsayed.bybike_rider.Fragments.HomeFragment;
import com.muhammadelsayed.bybike_rider.Fragments.RatingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Variables Declaration.
    private static final String TAG_FRAGMENT_HOME = "tag_frag_home";
    private static final String TAG_FRAGMENT_EARNINGS = "tag_frag_earnings";
    private static final String TAG_FRAGMENT_RATING = "tag_frag_rating";
    private static final String TAG_FRAGMENT_ACCOUNT = "tag_frag_account";
    private static final int INT_FRAGMENTS_COUNT = 4;
    private static final int INT_FRAGMENT_HOME_POS = 0;
    private static final int INT_FRAGMENT_EARNINGS_POS = 1;
    private static final int INT_FRAGMENT_RATING_POS = 2;
    private static final int INT_FRAGMENT_ACCOUNT_POS = 3;

    private Switch mActionbarSwitch;
    private ActionBar mActionBar;
    private BottomNavigationView MBottomNavigation;
    private Toast mStateToast;

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
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_online), Toast.LENGTH_SHORT);
                mStateToast.show();
            } else{
                if (mStateToast != null)
                    mStateToast.cancel();
                mStateToast = Toast.makeText(getApplicationContext(), getString(R.string.state_offline), Toast.LENGTH_SHORT);
                mStateToast.show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MBottomNavigation = findViewById(R.id.bottom_navigation_view);
        MBottomNavigation.getMenu().getItem(0).setChecked(true);
        MBottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        buildFragmentsList();
        // Set the 0th Fragment to be displayed by default.
        switchFragment(0, TAG_FRAGMENT_HOME);

        mActionBar = getSupportActionBar();
        mActionBar.setCustomView(R.layout.switch_layout);
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mActionbarSwitch = findViewById(R.id.switch_actionbar);
        mActionbarSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    /**
     * Use this  method to create a new instance of
     * any of the fragments using the fragments' factory methods.
     *
     * Then, the method puts the created fragments in the "mFragmentsList"
     */
    private void buildFragmentsList() {
        HomeFragment homeFragment = HomeFragment.homeFragmentInstance("Home Fragment");
        EarningsFragment earningsFragment = EarningsFragment.earningsFragmentInstance("Earnings Fragment");
        RatingFragment ratingFragment = RatingFragment.ratingFragmentInstance("Rating Fragment");
        AccountFragment accountFragment = AccountFragment.accountFragmentInstance("Account Fragment");

        mFragmentsList.add(homeFragment);
        mFragmentsList.add(earningsFragment);
        mFragmentsList.add(ratingFragment);
        mFragmentsList.add(accountFragment);
    }

    /**
     * This generic method is used to handle switching between fragments.
     *
     * @param pos The position of fragment at the "mFragmentList".
     * @param tag The tag name for the fragment.
     */
    private void switchFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_fragment_holder, mFragmentsList.get(pos), tag)
                .commit();
    }
}
