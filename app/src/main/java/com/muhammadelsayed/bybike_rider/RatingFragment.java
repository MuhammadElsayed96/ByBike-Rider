package com.muhammadelsayed.bybike_rider;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#ratingFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_TITLE = "Rating Fragment";
    private String mTitle;
    private TextView mTvRatingFragment;


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

        mTvRatingFragment = rootView.findViewById(R.id.tv_rating_fragment);
        String title = getArguments().getString(ARG_TITLE, "");
        mTvRatingFragment.setText(title);

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
