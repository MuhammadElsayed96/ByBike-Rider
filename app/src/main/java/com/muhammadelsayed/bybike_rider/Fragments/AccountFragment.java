package com.muhammadelsayed.bybike_rider.Fragments;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import com.muhammadelsayed.bybike_rider.RiderApplication;
import com.muhammadelsayed.bybike_rider.StartActivity;
import com.muhammadelsayed.bybike_rider.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private static final int GALLERY_INTENT_REQUEST_CODE = 100;
    private String mTitle;
    private TextView mTvAccountFragment;
    private TextView mTvUserName;
    private ConstraintLayout mClRiderProfile;
    private ConstraintLayout mClEditRiderProfile;
    private CardView mSignout;
    private CircularImageView mRiderImage;
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
    private CircularImageView.OnClickListener mOnCivRiderImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            try {
                startActivityForResult(intent, GALLERY_INTENT_REQUEST_CODE);

            } catch (ActivityNotFoundException e) {

                e.printStackTrace();
            }
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
        mRiderImage = rootView.findViewById(R.id.profile_image);
        mRiderImage.setImageResource(R.drawable.trump);
        mRiderImage.setOnClickListener(mOnCivRiderImage);
        mClRiderProfile.setOnClickListener(mOnClRiderProfile);
        mClEditRiderProfile.setOnClickListener(mOnClEditRiderProfile);
        mSignout = rootView.findViewById(R.id.cl_signout);
        mSignout.setOnClickListener(mOnClSignOut);
        mTvUserName = rootView.findViewById(R.id.tv_user_name);


        // getting current user
        currentUser = ((RiderApplication) getContext().getApplicationContext()).getCurrentRider();

        String[] names = Utils.splitName(currentUser.getRider().getName());
        String fName = names[FIRST_NAME_INDEX];
        mTvUserName.setText(fName);

        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + currentUser.getRider().getImage())
                .placeholder(R.drawable.trump)
                .error(R.drawable.trump)
                .into(mRiderImage);

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
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RiderInfoModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ref : https://stackoverflow.com/questions/2789276/android-get-real-path-by-uri-getpath
     *
     * @param uri The uri of the file.
     * @return the real path of that uri
     */
    private String getRealPathFromURI(Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                wholeID = DocumentsContract.getDocumentId(uri);
            }

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } else {
            // image pick from gallery
            return getRealPathFromURI(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_INTENT_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                Log.d(TAG, "onActivityResult: URI = " + uri);

                File imageFile = new File(getRealPathFromURI(uri));

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

                MultipartBody.Part body = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);
                RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, currentUser.getRider().getApi_token());

                RiderClient service = RetrofitClientInstance.getRetrofitInstance()
                        .create(RiderClient.class);

                Call<RiderModel> call = service.updateRiderProfileImage(token, body);
                call.enqueue(new Callback<RiderModel>() {
                    @Override
                    public void onResponse(Call<RiderModel> call, Response<RiderModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Log.d(TAG, "onResponse: " + response.body());
                                Picasso.get()
                                        .load(RetrofitClientInstance.BASE_URL + response.body().getRider().getImage())
                                        .placeholder(R.drawable.trump)
                                        .error(R.drawable.trump)
                                        .into(mRiderImage);
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onResponse: RESPONSE BODY = " + response.body());
                            }
                        } else {
                            Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RiderModel> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }
    }
}
