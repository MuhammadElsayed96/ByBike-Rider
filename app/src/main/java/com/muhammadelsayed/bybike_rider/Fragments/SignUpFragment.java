package com.muhammadelsayed.bybike_rider.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.CustomToast;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private static final String ARG_FNAME = "fName";
    private static final String ARG_LNAME = "lName";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PHONE = "phone";
    private View view;
    private EditText mEmail, mFirstName, mLastName, mPhone, mPassword;
    private FloatingActionButton fab;
    private LinearLayout mLogIn;
    private FragmentManager mFragmentManager;
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String password;

    public static SignUpFragment newSignUpFragmentInstance(String fName, String lName, String email, String phone) {
        Log.wtf(TAG, "newSignUpFragmentInstance() has been instantiated");

        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FNAME, fName);
        args.putString(ARG_LNAME, lName);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        Log.wtf(TAG, "onCreateView() has been instantiated");
        initViews();
        setListeners();

        if (getArguments() != null) {
            fName = getArguments().getString(ARG_FNAME);
            lName = getArguments().getString(ARG_LNAME);
            email = getArguments().getString(ARG_EMAIL);
            phone = getArguments().getString(ARG_PHONE);
            mFirstName.setText(fName);
            mLastName.setText(lName);
            mEmail.setText(email);
            mPhone.setText(phone);
        }

        return view;
    }


    private void initViews() {
        Log.wtf(TAG, "initViews() has been instantiated");
        mFragmentManager = getActivity().getSupportFragmentManager();
        fab = view.findViewById(R.id.continue_signUpBtn);
        mFirstName = view.findViewById(R.id.input_fname);
        mLastName = view.findViewById(R.id.input_lname);
        mEmail = view.findViewById(R.id.input_email);
        mPhone = view.findViewById(R.id.input_phone);
        mPassword = view.findViewById(R.id.input_password);
        mLogIn = view.findViewById(R.id.alreadyUser);
    }

    private void setListeners() {
        Log.wtf(TAG, "setListeners() has been instantiated");

        mLogIn.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    /**
     * Validates user input before logging him in
     *
     * @return false if input is not valid, true if valid
     */

    private boolean checkValidation() {
        Log.wtf(TAG, "checkValidation() has been instantiated");

        boolean isValid = true;


        fName = mFirstName.getText().toString();
        lName = mLastName.getText().toString();
        email = mEmail.getText().toString();
        phone = mPhone.getText().toString();
        password = mPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);

        // Check if all strings are null or not
        if (fName.length() == 0
                || email.length() == 0
                || phone.length() == 0
                || password.length() == 0
                || lName.length() == 0) {

            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "All fields are required.");
        }

        // Check if email id valid or not
        else if (!m.find()) {
            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "Your Email Id is Invalid.");
        }

        if (password.length() < 6) {
            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "Password is too short.");
        }

        if (phone.length() < 11) {
            isValid = false;
            new CustomToast().showToast(getActivity(), view,
                    "Invalid phone number");
        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyUser:
                Log.wtf(TAG, "already user view has been clicked");

                mFragmentManager.popBackStack();
                mFragmentManager
                        .beginTransaction()
                        .addToBackStack(TAG)
                        //.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.frameContainer, new LoginFragment(), Utils.LOGIN_FRAGMENT)
                        .commit();
                break;
            case R.id.continue_signUpBtn:
                Log.wtf(TAG, "continue sign up button has been clicked");

                if (checkValidation()) {

                    Rider rider = new Rider(fName, lName, email, phone, password);
                    ((RiderApplication) getActivity().getApplicationContext()).setCurrentRider(new RiderModel("", rider));
//                    mFragmentManager.popBackStack();
                    mFragmentManager
                            .beginTransaction()
                            ///.addToBackStack(TAG)
                            //.setCustomAnimations(R.anim.right_out, R.anim.left_enter)
                            .replace(R.id.frameContainer, new ExtendedSignUpFragment(), Utils.EXTENDED_SIGN_UP)
                            .commit();
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.wtf(TAG, "onResume() has been instantiated");

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    mFragmentManager.popBackStack();
                    mFragmentManager
                            .beginTransaction()
                            //.setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                            .replace(R.id.frameContainer, new LoginFragment(), Utils.LOGIN_FRAGMENT)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
}
