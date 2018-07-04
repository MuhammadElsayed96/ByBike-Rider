package com.muhammadelsayed.bybike_rider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.AccountActivities.RiderProfile;
import com.muhammadelsayed.bybike_rider.Model.Order;
import com.muhammadelsayed.bybike_rider.Model.OrderInfoModel;
import com.muhammadelsayed.bybike_rider.Model.Rider;
import com.muhammadelsayed.bybike_rider.Model.TripModel;
import com.muhammadelsayed.bybike_rider.Model.TripResponse;
import com.muhammadelsayed.bybike_rider.Network.RetrofitClientInstance;
import com.muhammadelsayed.bybike_rider.Network.RiderClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverTracking extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        RoutingListener {

    private static final String TAG = DriverTracking.class.getSimpleName();
    private static final int GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST = 101;
    private static final int RIDER_ON_THE_WAY = 1;
    private static final int PACKAGE_RECEIVED = 2;
    private static final int ORDER_DELIVERED = 3;
    private static final String RIDER_RECEIVED_PACKAGE = "I have the package";
    private static final String RIDER_DELIVERED_PACKAGE = "I delivered the package";
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.error};
    private static int tripStatus = RIDER_ON_THE_WAY;
    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private static LatLng origin, destination;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private View rootView;
    private GoogleMap mMap;
    private OrderInfoModel orderInfo;
    private List<Polyline> polylines; // for getting the route
    private Marker mRiderMarker;
    private DatabaseReference ref;
    private GeoFire geoFire;
    // widgets
    private Button btnCallClient, btnTripStatus, btnCancelTrip;
    private TextView txtClientName;
    private ImageView clientProfilePhoto;
    // Listeners
    private Button.OnClickListener btnCancelTripListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "Cancel trip btn has been clicked");
            Snackbar.make(btnCancelTrip, "Trip canceled successfully", Snackbar.LENGTH_LONG).show();

            String riderToken = orderInfo.getTransporter().getApi_token();
            final String orderId = String.valueOf(orderInfo.getOrder().getId());
            String cancel = "Rider canceled order";
            TripModel tripModel = new TripModel(riderToken, orderId, cancel);

            // Updating Firebase db.
            Order order = orderInfo.getOrder();
            order.setStatus(4);
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId);
            orderRef.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e(TAG, "Trip canceled successfully, Order status: 4");
                }
            });

            RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
            Call<TripResponse> call = service.cancelOrder(tripModel);
            call.enqueue(new Callback<TripResponse>() {
                @Override
                public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                    if (response.body() != null) {
                        Log.e(TAG, response.body().toString());
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<TripResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                }
            });


        }
    };

    private Button.OnClickListener btnCallClientListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.e(TAG, "Call client btn has been clicked");

            try {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + orderInfo.getOrder().getUser().getPhone()));
                if (ActivityCompat.checkSelfPermission(DriverTracking.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // requesting permission
                    ActivityCompat.requestPermissions(DriverTracking.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            10);
                    return;
                }
                startActivity(callIntent);
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
        }
    };

    private Button.OnClickListener btnTripStatusListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            Log.wtf(TAG, "Trip btn has been clicked");
            Log.wtf(TAG, "Trip status:" + String.valueOf(RIDER_ON_THE_WAY));
            tripStatus++;
            if (tripStatus == PACKAGE_RECEIVED) {
                Log.wtf(TAG, "Trip status:" + String.valueOf(PACKAGE_RECEIVED));
                btnTripStatus.setBackground(getDrawable(R.drawable.transparent_button_red));
                btnCancelTrip.setVisibility(View.GONE);
                btnTripStatus.setText(RIDER_DELIVERED_PACKAGE);

                String riderToken = orderInfo.getTransporter().getApi_token();
                final String orderId = String.valueOf(orderInfo.getOrder().getId());
                TripModel tripModel = new TripModel(riderToken, orderId);

                RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
                Call<TripResponse> call = service.approveOrder(tripModel);
                call.enqueue(new Callback<TripResponse>() {
                    @Override
                    public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                        if (response.body() != null) {
                            Log.wtf(TAG, response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<TripResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    }
                });

                // Updating Firebase db.
                Order order = orderInfo.getOrder();
                order.setStatus(PACKAGE_RECEIVED);
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId);
                orderRef.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.wtf(TAG, "Rider received the package, Order status: 2");
                    }
                });


            } else if (tripStatus == ORDER_DELIVERED) {
                Log.wtf(TAG, "Trip status:" + String.valueOf(ORDER_DELIVERED));

                String riderToken = orderInfo.getTransporter().getApi_token();
                final String orderId = String.valueOf(orderInfo.getOrder().getId());
                TripModel tripModel = new TripModel(riderToken, orderId);

                RiderClient service = RetrofitClientInstance.getRetrofitInstance().create(RiderClient.class);
                Call<TripResponse> call = service.receiveOrder(tripModel);
                call.enqueue(new Callback<TripResponse>() {
                    @Override
                    public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                        if (response.body() != null) {
                            Log.wtf(TAG, response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<TripResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show();
                    }
                });

                // Updating Firebase db.
                Order order = orderInfo.getOrder();
                order.setStatus(ORDER_DELIVERED);
                DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId);
                orderRef.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.wtf(TAG, "Rider delivered the package, Order status: 3");
                        tripStatus = RIDER_ON_THE_WAY;
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tracking);

        Log.wtf(TAG, "onCreate() has been instantiated");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() != null) {
            orderInfo = (OrderInfoModel) getIntent().getExtras().getSerializable("order_info_model");
        }

        setupWidgets();

        createLocationRequest();
        buildGoogleApiClient();

        ref = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(ref);
    }

    private void setupWidgets() {
        Log.wtf(TAG, "setupWidgets() has been instantiated");

        // setting up widgets
        txtClientName = findViewById(R.id.user_name_textview);
        txtClientName.setText(orderInfo.getOrder().getUser().getName());

        clientProfilePhoto = findViewById(R.id.user_profile_image);
        Picasso.get()
                .load(RetrofitClientInstance.BASE_URL + orderInfo.getOrder().getUser().getImage())
                .placeholder(R.drawable.trump)
                .error(R.drawable.trump)
                .into(clientProfilePhoto);

        btnCallClient = findViewById(R.id.call_user_button);
        btnCallClient.setOnClickListener(btnCallClientListener);

        btnTripStatus = findViewById(R.id.trip_status_btn);
        btnTripStatus.setOnClickListener(btnTripStatusListener);
        btnTripStatus.setText(RIDER_RECEIVED_PACKAGE);

        btnCancelTrip = findViewById(R.id.cancel_trip_btn);
        btnCancelTrip.setOnClickListener(btnCancelTripListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.wtf(TAG, "onMapReady() has been instantiated");

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        displayOriginDestination();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.wtf(TAG, "onConnected() has been instantiated");

        displayLocation();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.wtf(TAG, "onConnectionSuspended() has been instantiated");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.wtf(TAG, "onConnectionFailed() has been instantiated");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.wtf(TAG, "onLocationChanged() has been instantiated");

        mLastLocation = location;
        displayLocation();

    }

    private void displayOriginDestination() {
        Log.wtf(TAG, "displayOriginDestination() has been instantiated");

        double originLat = Double.parseDouble(orderInfo.getOrder().getSender_Lat());
        double originLng = Double.parseDouble(orderInfo.getOrder().getSender_Lng());
        double destinationLat = Double.parseDouble(orderInfo.getOrder().getReceiver_lat());
        double destinationLng = Double.parseDouble(orderInfo.getOrder().getReceiver_lng());


        origin = new LatLng(originLat, originLng);

        MarkerOptions optionsOrg = new MarkerOptions()
                .position(origin)
                .title("Start")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_origin));

        mMap.addMarker(optionsOrg);

        destination = new LatLng(destinationLat, destinationLng);

        MarkerOptions optionsDes = new MarkerOptions()
                .position(destination)
                .title("End")
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination));

        mMap.addMarker(optionsDes);

    }

    private void displayLocation() {
        Log.wtf(TAG, "displayLocation() has been instantiated");

        if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            geoFire.setLocation(String.valueOf(orderInfo.getOrder().getId()), new GeoLocation(lat, lng), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    handleNewLocation(mLastLocation);

                    LatLng riderLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                    drawRoute(riderLatLng, origin, destination);
                }
            });

            Log.d(TAG, String.format("Your location was changed: %f / %f", lat, lng));
        } else {
            Log.d(TAG, "cannot get your location");
        }
    }

    private void startLocationUpdates() {
        Log.wtf(TAG, "startLocationUpdates() has been instantiated");

        if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                try {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                } catch (SecurityException e) {
                    Log.e(TAG, "onResult: SecurityException == " + e.getMessage());
                }
            }
        });


        Log.d(TAG, "startLocationUpdates: TEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEST");
        LatLng riderLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        moveCamToProperZoom(riderLatLng, origin, destination);
    }

    private void handleNewLocation(Location location) {
        Log.wtf(TAG, "handleNewLocation() has been instantiated");

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (mRiderMarker != null)
            mRiderMarker.remove();


        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You")
                .snippet("The is where you are.")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle));
        Log.d(TAG, String.format("Your location was changed: %f / %f", location.getLatitude(), location.getLongitude()));
        mRiderMarker = mMap.addMarker(options);

    }

    private void createLocationRequest() {
        Log.wtf(TAG, "createLocationRequest() has been instantiated");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        Log.wtf(TAG, "buildGoogleApiClient() has been instantiated");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /******************** Routing ********************/
    /**
     * draws shortest route between two points,
     * showing all the alternative routes,
     * but I disabled that here
     * <p>
     * library : https://github.com/jd-alexander/Google-Directions-Android
     *
     * @param riderLatLng the location of the rider (device)
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void drawRoute(LatLng riderLatLng, LatLng origin, LatLng destination) {
        Log.wtf(TAG, "drawRoute() has been instantiated");

        // getting the route
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(riderLatLng, origin, destination)
                .build();

        routing.execute();

    }

    /**
     * Moves the camera and changes its zoom in a way
     * that show all the three Markers.
     *
     * @param riderLatLng the location of the rider (device)
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void moveCamToProperZoom(LatLng riderLatLng, LatLng origin, LatLng destination) {
        Log.wtf(TAG, "moveCamToProperZoom() has been instantiated");

        // controlling the camera position in a way that show both markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(riderLatLng);
        builder.include(origin);
        builder.include(destination);
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.12);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
    }

    /**
     * Removes all the Polyline (routes) from the map.
     */
    private void erasePolylines() {
        Log.wtf(TAG, "erasePolylines() has been instantiated");

        if (polylines != null)
            if (polylines.size() > 0)
                for (Polyline poly : polylines)
                    poly.remove();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Log.wtf(TAG, "onRoutingFailure() has been instantiated");

        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRoutingFailure: Error: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        Log.wtf(TAG, "onRoutingStart() has been instantiated");
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        Log.wtf(TAG, "onRoutingSuccess() has been instantiated");

        // removing old polylines
        erasePolylines();

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.wtf(TAG, "onRoutingCancelled() has been instantiated");

    }

}
