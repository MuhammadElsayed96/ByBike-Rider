package com.muhammadelsayed.bybike_rider.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SignUpFragment";
    private View view;
    private EditText mEmail, mFirstName, mLastName, mPhone, mPassword;
    private Button mSignUpButton;
    private TextView mLogIn;
    private FragmentManager mFragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        Log.d(TAG, "onCreateView: started !!");
        initViews();
        setListeners();
        return view;
    }


    private void initViews() {
        Log.d(TAG, "initViews: initializing the view...");
        mFragmentManager = getActivity().getSupportFragmentManager();

        mFirstName = view.findViewById(R.id.input_fname);
        mLastName = view.findViewById(R.id.input_lname);
        mEmail = view.findViewById(R.id.input_email);
        mPhone = view.findViewById(R.id.input_phone);
        mPassword = view.findViewById(R.id.input_password);
        mSignUpButton = view.findViewById(R.id.signUpBtn);
        mLogIn = view.findViewById(R.id.alreadyUser);
    }


    private void setListeners() {
        mSignUpButton.setOnClickListener(this);
        mLogIn.setOnClickListener(this);
    }

    /**
     * Validates user input before logging him in
     *
     * @return false if input is not valid, true if valid
     */

    private boolean checkValidation() {

        boolean isValid = true;
//
//        // Get all edittext texts
//        String fullName = mFullName.getText().toString();
//        String email = mEmail.getText().toString();
//        String phoneNumber = mPhoneNumber.getText().toString();
//        String password = mPassword.getText().toString();
//        String confirmPassword = mConfirmPassword.getText().toString();
//
//        // Pattern match for email id
//        Pattern p = Pattern.compile(Utils.regEx);
//        Matcher m = p.matcher(email);
//
//        // Check if all strings are null or not
//        if (fullName.length() == 0
//                || email.length() == 0
//                || phoneNumber.length() == 0
//                || password.length() == 0
//                || confirmPassword.length() == 0) {
//
//            isValid = false;
//            new CustomToast().showToast(getActivity(), view,
//                    "All fields are required.");
//        }
//
//        // Check if email id valid or not
//        else if (!m.find()) {
//
//            isValid = false;
//            new CustomToast().showToast(getActivity(), view,
//                    "Your Email Id is Invalid.");
//        }
//        // Check if both password should be equal
//        else if (!confirmPassword.equals(password)) {
//
//            isValid = false;
//
//            new CustomToast().showToast(getActivity(), view,
//                    "Both password doesn't match.");
//        }
//        // Make sure user should check Terms and Conditions checkbox
//        else if (!terms_conditions.isChecked()) {
//
//            isValid = false;
//
//            new CustomToast().showToast(getActivity(), view,
//                    "Please select Terms and Conditions.");
//        }

        return isValid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alreadyUser:
                mFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.frameContainer, new LoginFragment(), Utils.LoginFragment)
                        .commit();
                break;
        }
    }
}
