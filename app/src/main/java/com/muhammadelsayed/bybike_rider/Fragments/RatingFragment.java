package com.muhammadelsayed.bybike_rider.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.RiderModel;
import com.muhammadelsayed.bybike_rider.Model.RiderRateModel;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RatingActivities.RatingDetails;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TAG = "RatingFragment";
    private static final String ARG_TITLE = "RatingFragment";

    private TextView mTvFiveStareRatings;
    private View rootView;
    private AlertDialog waitingDialog;
    private RiderRateModel riderRateModel;
    private TextView mTvStarRating;
    private Context context;


    private TextView.OnClickListener mOnTvStarRatingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), RatingDetails.class);
            if (riderRateModel != null)
                intent.putExtra("Rider_Star_Rating", riderRateModel.getRating());
            else
                intent.putExtra("Rider_Star_Rating", "0");
            startActivity(intent);
        }
    };

    public static RatingFragment ratingFragmentInstance(String title) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public RatingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreateView() has been instantiated");

        context = getActivity();
        waitingDialog = new SpotsDialog(context, R.style.Custom);
        waitingDialog.setCancelable(false);
        waitingDialog.setTitle("Loading...");


        showProgressDialog();
        rootView = inflater.inflate(R.layout.fragment_rating, container, false);
        initViews();
        setListeners();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.wtf(TAG, "onViewCreated() has been instantiated");
        getRiderRatings();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.wtf(TAG, "onDetach() has been instantiated");
    }


    /**
     * sets up all Fragment Views
     */
    private void initViews() {
        Log.wtf(TAG, "initViews() has been instantiated");
        // Inflate the layout for this fragment
        mTvFiveStareRatings = rootView.findViewById(R.id.tv_five_star_ratings);
        mTvStarRating = rootView.findViewById(R.id.tv_star_rating);
    }

    private void setListeners() {
        Log.wtf(TAG, "setListeners() has been instantiated");
        mTvStarRating.setOnClickListener(mOnTvStarRatingListener);
    }

    private void getRiderRatings() {
        Log.wtf(TAG, "getRiderRatings() has been instantiated");

        // getting current user
        RiderModel currentUser = ((RiderApplication) context.getApplicationContext()).getCurrentRider();
        String riderToken = currentUser.getToken();

        RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);

        Rider rider = new Rider(riderToken);
        Call<RiderRateModel> call = service.getRiderRatings(rider);

        call.enqueue(new Callback<RiderRateModel>() {
            @Override
            public void onResponse(Call<RiderRateModel> call, Response<RiderRateModel> response) {
                if (response.body() != null) {
                    Log.e(TAG, "onResponse: " + response.body());
                    riderRateModel = response.body();
                    mTvFiveStareRatings.setText(riderRateModel.getFive_stars());
                    mTvStarRating.setText(String.format("%.2f", Float.valueOf(riderRateModel.getRating())));
                } else {
                    Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_SHORT).show();
                    mTvFiveStareRatings.setText("0");
                    mTvStarRating.setText("0");
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<RiderRateModel> call, Throwable t) {
                hideProgressDialog();
                mTvFiveStareRatings.setText("0");
                mTvStarRating.setText("0");
                Toast.makeText(getActivity(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        Log.wtf(TAG, "showProgressDialog() has been instantiated");
        waitingDialog.show();
    }

    private void hideProgressDialog() {
        Log.wtf(TAG, "hideProgressDialog() has been instantiated");
        waitingDialog.dismiss();
    }

}
