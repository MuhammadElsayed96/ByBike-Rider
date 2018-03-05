package com.muhammadelsayed.bybike_rider.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muhammadelsayed.bybike_rider.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#homeFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // The fragment initialization parameters
    public static final String ARG_TITLE = "Home Fragment";
    private String mTitle;
    private TextView mTvHomeFragment;

    public HomeFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title The title of fragment HomeFragment.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment homeFragmentInstance(String title) {
        HomeFragment fragment = new HomeFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mTvHomeFragment = rootView.findViewById(R.id.tv_home_fragment);
        String title = getArguments().getString(ARG_TITLE, "");
        mTvHomeFragment.setText(title);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
