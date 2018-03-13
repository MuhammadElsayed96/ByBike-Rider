package com.muhammadelsayed.bybike_rider.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RatingActivities.AcceptanceDetails;
import com.muhammadelsayed.bybike_rider.RatingActivities.CancellationDetails;
import com.muhammadelsayed.bybike_rider.RatingActivities.RatingDetails;
import com.muhammadelsayed.bybike_rider.RatingActivities.RiderCompliments;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#ratingFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_TITLE = "Rating Fragment";
    private String mTitle;
    private TextView mTvFiveStareRatings;
    private LinearLayout mLlStarRating;
    private LinearLayout mLlAcceptanceRate;
    private LinearLayout mLlCancellationRate;
    private ConstraintLayout mClRiderCompliments;
    private TextView mTvNumOfCompliments;

    private LinearLayout.OnClickListener  mOnLlAcceptanceRateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), AcceptanceDetails.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlStarRatingClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), RatingDetails.class));
        }
    };
    private LinearLayout.OnClickListener mOnLlCancellationRateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), CancellationDetails.class));
        }
    };
    private ConstraintLayout.OnClickListener mOnClRiderComplimentsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), RiderCompliments.class));
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
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rating, container, false);

        mTvFiveStareRatings = rootView.findViewById(R.id.tv_five_star_ratings);
        mLlStarRating = rootView.findViewById(R.id.ll_star_rating);
        mLlAcceptanceRate = rootView.findViewById(R.id.ll_acceptance_rate);
        mLlCancellationRate = rootView.findViewById(R.id.ll_cancellation_rate);
        mClRiderCompliments = rootView.findViewById(R.id.cl_rider_profile);
        mTvNumOfCompliments = rootView.findViewById(R.id.tv_num_of_compliments);

        mLlAcceptanceRate.setOnClickListener(mOnLlAcceptanceRateClickListener);
        mLlCancellationRate.setOnClickListener(mOnLlCancellationRateClickListener);
        mLlStarRating.setOnClickListener(mOnLlStarRatingClickListener);
        mClRiderCompliments.setOnClickListener(mOnClRiderComplimentsClickListener);


        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
