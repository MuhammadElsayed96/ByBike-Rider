package com.muhammadelsayed.bybike_rider.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.muhammadelsayed.bybike_rider.CustomSpinnerAdapter;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.SignUpResponse;
import com.muhammadelsayed.bybike_rider.Model.Transportation;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.CustomToast;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExtendedSignUpFragment extends Fragment {

    private static final String TAG = ExtendedSignUpFragment.class.getSimpleName();
    List<Transportation> vehicles;
    private CardView mSignUpButton;
    private FragmentManager mFragmentManager;
    private Spinner transportationSpinner;
    private View view;
    private EditText mVehicleData;
    private CustomSpinnerAdapter spinnerAdapter;
    private String vehicleData;
    private CardView.OnClickListener onCvSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "SignUp clicked");

            if (checkValidation()) {

                final AlertDialog waitingDialog = new SpotsDialog(getContext(), R.style.Custom);
                waitingDialog.setCancelable(false);
                waitingDialog.setTitle("Loading...");
                waitingDialog.show();

                ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider().setVehicle_Data(vehicleData);


                RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(RiderClient.class);

                Rider rider = ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider();

                Call<SignUpResponse> call = service.signUpRider(rider);

                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        Log.d(TAG, response.body().toString());
                        waitingDialog.dismiss();
                        mFragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                                .replace(R.id.frameContainer, new LoginFragment(), Utils.SING_UP_FRAGMENT)
                                .commit();

                        Snackbar.make(view, "Signed Up Successfully", Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        waitingDialog.dismiss();
                        Snackbar.make(view, "Error!", Snackbar.LENGTH_LONG).show();
                    }
                });


            }
        }
    };

    private Spinner.OnItemSelectedListener onSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider().setVehicle_ID("1");
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


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
        Log.wtf(TAG, "onCreateView() has been instantiated");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_extended_sign_up, container, false);
        initViews();
        setListeners();
        return view;
    }


    private void initViews() {
        Log.wtf(TAG, "initViews() has been instantiated");
        mFragmentManager = getActivity().getSupportFragmentManager();
        mSignUpButton = view.findViewById(R.id.signUpBtn);
        transportationSpinner = view.findViewById(R.id.vehicle_spinner);
        mVehicleData = view.findViewById(R.id.input_vehicle_description);
        vehicles = Arrays.asList(new Transportation());
        vehicles.get(0).setTransType("Bicycle");
        vehicles.get(0).setTransImg(R.drawable.bicycle);
//        vehicles.get(1).setTransType("Motorcycle");
//        vehicles.get(0).setTransImg(R.drawable.motor);
//        vehicles.get(2).setTransType("Trike motorcycle");
        spinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_transprotaiton, vehicles);
        transportationSpinner.setAdapter(spinnerAdapter);
    }

    private void setListeners() {
        Log.wtf(TAG, "setListeners() has been instantiated");
        mSignUpButton.setOnClickListener(onCvSignUpListener);
        transportationSpinner.setOnItemSelectedListener(onSpinnerListener);
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
                if (event.getAction() == KeyEvent.ACTION_UP || keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Rider rider = ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider();
                    SignUpFragment fragment = SignUpFragment.newSignUpFragmentInstance(rider.getFirstName(), rider.getLastName(), rider.getEmail(), rider.getPhone());
                    mFragmentManager.popBackStack();
                    mFragmentManager
                            .beginTransaction()
                            //.setCustomAnimations(R.anim.left_out, R.anim.right_enter)
                            .replace(R.id.frameContainer, fragment, Utils.SING_UP_FRAGMENT)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Validates user input before logging him in
     *
     * @return false if input is not valid, true if valid
     */
    private boolean checkValidation() {
        boolean isValid = true;
        Log.wtf(TAG, "checkValidation() has been instantiated");

        vehicleData = mVehicleData.getText().toString();


        // Check for both field is empty or not
        if (vehicleData.length() == 0) {

            isValid = false;

            new CustomToast().showToast(getActivity(), view,
                    "Invalid vehicle Data");
        }

        return isValid;
    }


}
