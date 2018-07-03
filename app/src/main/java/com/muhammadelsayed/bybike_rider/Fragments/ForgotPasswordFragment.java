package com.muhammadelsayed.bybike_rider.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.Utils.CustomToast;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "FORGOT_PASSWORD_FRAGMENT";
    private View mView;
    private EditText mEmail;
    private TextView mSubmit, mBack;
    private static FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.forgot_password_layout, container, false);
        initViews();
        setListeners();
        return mView;
    }

    // Initialize the views
    private void initViews() {
        mEmail = mView.findViewById(R.id.registered_emailid);
        mSubmit = mView.findViewById(R.id.forgot_button);
        mBack = mView.findViewById(R.id.backToLoginBtn);

        mFragmentManager = getActivity().getSupportFragmentManager();
    }

    // Set Listeners over buttons
    private void setListeners() {
        mSubmit.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                mFragmentManager.popBackStack();
                mFragmentManager
                        .beginTransaction()
                        //.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.frameContainer,
                                new LoginFragment(),
                                Utils.LOGIN_FRAGMENT).commit();
                break;

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }
    }

    private void submitButtonTask() {
        String email = mEmail.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(email);

        // First check if email id is not null else show error toast
        if (email.equals("") || email.length() == 0)

            new CustomToast().showToast(getActivity(), mView,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().showToast(getActivity(), mView,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch password or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

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