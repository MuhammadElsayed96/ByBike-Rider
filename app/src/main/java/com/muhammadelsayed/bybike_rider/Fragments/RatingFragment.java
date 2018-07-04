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
import com.muhammadelsayed.bybike_rider.RatingActivities.AcceptanceDetails;
import com.muhammadelsayed.bybike_rider.RatingActivities.CancellationDetails;
import com.muhammadelsayed.bybike_rider.RatingActivities.RatingDetails;
import com.muhammadelsayed.bybike_rider.RiderApplication;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#ratingFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_TITLE = "Rating Fragment";
    private static final String TAG = "RatingFragment";

    private String mTitle;
    private TextView mTvFiveStareRatings;
    private LinearLayout mLlStarRating;
    private LinearLayout mLlAcceptanceRate;
    private LinearLayout mLlCancellationRate;
    private View rootView;
    private AlertDialog waitingDialog;
    private RiderRateModel riderRateModel;
    private TextView mTvStarRating;
    private TextView mTvAcceptanceRate;
    private TextView mTvCancellationRate;
    private Context context;


    private LinearLayout.OnClickListener mOnLlAcceptanceRateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), AcceptanceDetails.class);
            intent.putExtra("Rider_Acceptance_Rate", riderRateModel.getAcceptance_rate());
            startActivity(intent);
        }
    };
    private LinearLayout.OnClickListener mOnLlStarRatingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), RatingDetails.class);
            intent.putExtra("Rider_Star_Rating", riderRateModel.getRating());
            startActivity(intent);
        }
    };
    private LinearLayout.OnClickListener mOnLlCancellationRateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), CancellationDetails.class);
            intent.putExtra("Rider_Cancellation_Rate", getCancellationRate(riderRateModel.getAcceptance_rate()));
            startActivity(intent);
        }
    };


    public RatingFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title The title of fragment RatingFragment.
     * @return A new instance of fragment RatingFragment.
     */

    public static RatingFragment ratingFragmentInstance(String title) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
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
        mLlStarRating = rootView.findViewById(R.id.ll_star_rating);
        mLlAcceptanceRate = rootView.findViewById(R.id.ll_acceptance_rate);
        mLlCancellationRate = rootView.findViewById(R.id.ll_cancellation_rate);
        mTvStarRating = rootView.findViewById(R.id.tv_star_rating);
        mTvAcceptanceRate = rootView.findViewById(R.id.tv_acceptance_rate);
        mTvCancellationRate = rootView.findViewById(R.id.tv_cancellation_rate);
    }

    private void setListeners() {
        Log.wtf(TAG, "setListeners() has been instantiated");
        mLlAcceptanceRate.setOnClickListener(mOnLlAcceptanceRateClickListener);
        mLlCancellationRate.setOnClickListener(mOnLlCancellationRateClickListener);
        mLlStarRating.setOnClickListener(mOnLlStarRatingClickListener);
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
                    mTvStarRating.setText(riderRateModel.getRating());
                    mTvAcceptanceRate.setText(getAcceptanceRate(riderRateModel.getAcceptance_rate()) + "%");
                    mTvCancellationRate.setText((getCancellationRate(riderRateModel.getAcceptance_rate())) + "%");
                } else {
                    Toast.makeText(getActivity(), "Error!!", Toast.LENGTH_SHORT).show();
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<RiderRateModel> call, Throwable t) {
                hideProgressDialog();
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

    private String getCancellationRate(String acceptanceRate) {
        int acceptanceRateValue = (int) (100.0 * Float.valueOf(acceptanceRate));
        int CancellationRateValue = (100 - acceptanceRateValue);
        return String.valueOf(CancellationRateValue);
    }

    private String getAcceptanceRate(String acceptanceRate) {
        int acceptanceRateValue = (int) (100.0 * Float.valueOf(acceptanceRate));
        return String.valueOf(acceptanceRateValue);
    }

}
