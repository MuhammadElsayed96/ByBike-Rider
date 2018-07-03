package com.muhammadelsayed.bybike_rider.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import org.angmarch.views.NiceSpinner;


public class ExtendedSignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ExtendedSignUpFragment.class.getSimpleName();
    private CardView mSignUpButton;
    private FragmentManager mFragmentManager;
    private NiceSpinner niceSpinner;
    private View view;


    public ExtendedSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_extended_sign_up, container, false);
        initViews();
        setListeners();

        return view;
    }


    private void initViews() {
        Log.d(TAG, "initViews: initializing the view...");
        mFragmentManager = getActivity().getSupportFragmentManager();
        mSignUpButton = view.findViewById(R.id.signUpBtn);
    }

    private void setListeners() {
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Rider rider = ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider();
                    SignUpFragment fragment = SignUpFragment.newSignUpFragmentInstance(rider.getFirstName(), rider.getLastName(), rider.getEmail(), rider.getPhone());
                    mFragmentManager.popBackStack();
                    mFragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                            .replace(R.id.frameContainer, fragment, Utils.SING_UP_FRAGMENT)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}
