package com.muhammadelsayed.bybike_rider.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.muhammadelsayed.bybike_rider.AccountActivities.EditAccount;
import com.muhammadelsayed.bybike_rider.AccountActivities.RiderProfile;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderInfoModel;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.StartActivity;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#accountFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    // the fragment initialization parameters
    private static final String ARG_TITLE = "Account Fragment";
    private static final int FIRST_NAME_INDEX = 0;
    private String mTitle;
    private TextView mTvAccountFragment;
    private TextView mTvUserName;
    private ConstraintLayout mClRiderProfile;
    private ConstraintLayout mClEditRiderProfile;
    private CardView mSignout;
    private CircularImageView mUserImage;
    private View rootView;
    private RiderInfoModel riderInfoModel;
    private RiderModel currentUser;

    private ConstraintLayout.OnClickListener mOnClEditRiderProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), EditAccount.class);
            RiderModel currentUser = (RiderModel) getActivity().getIntent().getSerializableExtra("current_rider");
            intent.putExtra("current_user", currentUser);
            startActivity(intent);
        }
    };
    private ConstraintLayout.OnClickListener mOnClRiderProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), RiderProfile.class);
            intent.putExtra("rider_Info_Model", riderInfoModel);
            startActivity(intent);
        }
    };
    private CardView.OnClickListener mOnClSignOut = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "Sign out Clicked");
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.clear();
            prefEditor.apply();
            Intent startActivityIntent = new Intent(getContext(), StartActivity.class);
            startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startActivityIntent);
        }
    };

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title The title of fragment RatingFragment.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment accountFragmentInstance(String title) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_account, container, false);

        setupWidgets();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated has been instantiated");
        getRiderInfo();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * sets up all activity widgets
     */
    private void setupWidgets() {
        mClEditRiderProfile = rootView.findViewById(R.id.cl_edit_rider_profile);
        mClRiderProfile = rootView.findViewById(R.id.cl_rider_profile);
        mUserImage = rootView.findViewById(R.id.profile_image);
        mUserImage.setImageResource(R.drawable.trump);
        mClRiderProfile.setOnClickListener(mOnClRiderProfile);
        mClEditRiderProfile.setOnClickListener(mOnClEditRiderProfile);
        mSignout = rootView.findViewById(R.id.cl_signout);
        mSignout.setOnClickListener(mOnClSignOut);
        mTvUserName = rootView.findViewById(R.id.tv_user_name);


        // getting current user
        currentUser = (RiderModel) getActivity().getIntent().getSerializableExtra("current_rider");

        String[] names = Utils.splitName(currentUser.getRider().getName());
        String fName = names[FIRST_NAME_INDEX];
        mTvUserName.setText(fName);

    }


    private void getRiderInfo() {
        Log.d(TAG, "getRiderInfo() has been instantiated");
        String riderToken = currentUser.getToken();

        RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                .create(RiderClient.class);
        RiderModel model = new RiderModel();
        model.setRider(new Rider(riderToken));


        Call<RiderInfoModel> call = service.getRiderInfo(model.getRider());
        call.enqueue(new Callback<RiderInfoModel>() {
            @Override
            public void onResponse(Call<RiderInfoModel> call, Response<RiderInfoModel> response) {
                if (response.body() != null) {
                    Log.wtf(TAG, "onResponse: " + response.body());
                    riderInfoModel = response.body();
                } else {
                    Toast.makeText(getActivity(), "I have no Idea what's happening\nbut, something is terribly wrong !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RiderInfoModel> call, Throwable t) {
                Toast.makeText(getActivity(), "network error !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
