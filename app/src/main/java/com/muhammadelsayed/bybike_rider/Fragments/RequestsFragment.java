package com.muhammadelsayed.bybike_rider.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhammadelsayed.bybike_rider.Firebase.Orders;
import com.muhammadelsayed.bybike_rider.Firebase.OrdersAdapter;
import com.muhammadelsayed.bybike_rider.MainActivity;
import com.muhammadelsayed.bybike_rider.R;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";
    private String mTitle;
    private View rootView;
    private ListView requestListView;
    private List<Orders> orderList;
    private DatabaseReference mOrdersRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.wtf(TAG, "onCreateView() has been instantiated");

        rootView = inflater.inflate(R.layout.simple_requests_list_view, container, false);

        setupWidgets();
        getOrdersInfo();

        return rootView;
    }

    public static RequestsFragment requestsFragmentInstance(String title) {
        Log.wtf(TAG, "requestsFragmentInstance() has been instantiated");

        RequestsFragment fragment = new RequestsFragment();
        Bundle args = new Bundle();
        args.putString(TAG, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf(TAG, "onCreate() has been instantiated");

        if (getArguments() != null) {
            mTitle = getArguments().getString(TAG);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.wtf(TAG, "onViewCreated() has been instantiated");
    }

    private void setupWidgets() {
        Log.d(TAG, "setupWidgets() has been instantiated");

        requestListView = rootView.findViewById(R.id.simpleListView);
        orderList = new ArrayList<>();

        mOrdersRef = FirebaseDatabase.getInstance().getReference("orders");
    }

    private void getOrdersInfo() {
        Log.d(TAG, "getOrdersInfo() has been instantiated");

        mOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Log.d(TAG, child.toString());
                    Orders orders = child.getValue(Orders.class);
                    if (orders.getStatus() != 0)
                        continue;
                    orderList.add(orders);
                }

                if (getActivity() != null) {
                    requestListView.setAdapter(new OrdersAdapter(getActivity(), orderList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
