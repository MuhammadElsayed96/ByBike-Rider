package com.muhammadelsayed.bybike_rider.Fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.muhammadelsayed.bybike_rider.Adapters.EarningsAdapter;
import com.muhammadelsayed.bybike_rider.Model.Earnings;
import com.muhammadelsayed.bybike_rider.Model.EarningsModel;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.TripModel;
import com.muhammadelsayed.bybike_rider.Model.TripResponse;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.muhammadelsayed.bybike_rider.R;
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EarningsFragment#earningsFragmentInstance} factory method to
 * create an instance of this fragment.
 */
public class EarningsFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TAG = EarningsFragment.class.getSimpleName();

    private View rootView;
    private ListView earningsListView;
    private List<Earnings> earningsList;


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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");
        Utils.checkUserSession(getActivity());

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreateView() has been instantiated");

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_earnings, container, false);
        setupWidgets();
        getRiderEarnings();

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.wtf(TAG, "onDetach() has been instantiated");

    }

    private void setupWidgets() {
        earningsListView = rootView.findViewById(R.id.earningsListView);
        earningsList = new ArrayList<>();
    }


    private void getRiderEarnings() {
        Log.wtf(TAG, "getRiderEarnings() has been instantiated");

        final AlertDialog waitingDialog = new SpotsDialog(getActivity(), R.style.Custom);
        waitingDialog.setCancelable(false);
        waitingDialog.setTitle("Loading...");
        waitingDialog.show();

        RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
        Rider rider = ((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getRider();
        rider.setApi_token(((RiderApplication) getActivity().getApplicationContext()).getCurrentRider().getToken());
        Call<EarningsModel> call = service.getRiderEarnings(rider);
        call.enqueue(new Callback<EarningsModel>() {
            @Override
            public void onResponse(Call<EarningsModel> call, Response<EarningsModel> response) {
                Log.wtf(TAG, "getRiderEarnings() onResponse");
                if (response.body() != null) {
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                    if (earningsList != null)
                        earningsList.clear();
                    earningsList = extractEarningsData(response.body());
                    earningsListView.setAdapter(new EarningsAdapter(getActivity().getApplicationContext(), earningsList));
                    waitingDialog.dismiss();
                } else {
                    Log.wtf(TAG, "getRiderEarnings() onResponse failure!");
                    Toast.makeText(getActivity(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EarningsModel> call, Throwable t) {
                waitingDialog.dismiss();
                Log.wtf(TAG, "getRiderEarnings() onFailure");
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Earnings> extractEarningsData(EarningsModel model) {
        List<Earnings> earningsList = new ArrayList<>();
        for (Earnings trip : model.getEarnings()) {
            earningsList.add(trip);
        }
        return earningsList;
    }
}
