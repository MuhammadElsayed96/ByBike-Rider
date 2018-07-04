package com.muhammadelsayed.bybike_rider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muhammadelsayed.bybike_rider.Model.OrderInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class DriverTracking extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        RoutingListener {

    private static final String TAG = DriverTracking.class.getSimpleName();
    private GoogleMap mMap;
    private OrderInfoModel orderInfo;
    public static final int GOOGLE_PLAY_SERVICES_RESOLUTION_REQUEST = 101;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private View rootView;

    // for getting the route
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.error};


    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    Marker mRiderMarker;

    private static LatLng origin, destination;

    DatabaseReference ref;
    GeoFire geoFire;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_tracking);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() != null) {
            orderInfo = (OrderInfoModel) getIntent().getExtras().getSerializable("order_info_model");
        }

        createLocationRequest();
        buildGoogleApiClient();


        ref = FirebaseDatabase.getInstance().getReference("Drivers");
        geoFire = new GeoFire(ref);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        displayOriginDestination();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        displayLocation();
        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

    }

    private void displayOriginDestination() {
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

        if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            geoFire.setLocation("You", new GeoLocation(lat, lng), new GeoFire.CompletionListener() {
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
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
     *
     * library : https://github.com/jd-alexander/Google-Directions-Android
     *
     * @param riderLatLng the location of the rider (device)
     * @param origin      the location from which the trip will start
     * @param destination the location where the trip ends
     */
    private void drawRoute(LatLng riderLatLng, LatLng origin, LatLng destination) {

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
        if (polylines != null)
            if (polylines.size() > 0)
                for (Polyline poly : polylines)
                    poly.remove();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRoutingFailure: Error: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

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

    }
}
