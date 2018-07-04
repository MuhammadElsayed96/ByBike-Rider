package com.muhammadelsayed.bybike_rider.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EarningsFragment#earningsFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class EarningsFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TAG = EarningsFragment.class.getSimpleName();
    private static final String ARG_TITLE = "Earning Fragment";
    private String mTitle;
    private TextView mTvEarningsFragment;


    public EarningsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title The title of fragment EarningsFragment.
     * @return A new instance of fragment EarningsFragment.
     */
    public static EarningsFragment earningsFragmentInstance(String title) {
        EarningsFragment fragment = new EarningsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreateView() has been instantiated");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_earnings, container, false);

        mTvEarningsFragment = rootView.findViewById(R.id.tv_earnings_fragment);
        String title = getArguments().getString(ARG_TITLE, "");
        mTvEarningsFragment.setText(title);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.wtf(TAG, "onDetach() has been instantiated");

    }
}
