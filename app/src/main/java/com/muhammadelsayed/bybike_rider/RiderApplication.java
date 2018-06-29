package com.muhammadelsayed.bybike_rider;

import android.app.Application;

import com.muhammadelsayed.bybike_rider.Model.RiderModel;

/**
 * I created this class for a specific purpose which is
 * making a global variable keep its value around the lifecycle of the application
 * regardless which activity is running.
 * <p>
 * I need this class to keep the current user session, by that I mean to keep the user logged in to the app
 * even when the app gets destroyed.
 * <p>
 * The user's session gets lost only when the user sign out from the app.
 */

public class RiderApplication extends Application {


    private RiderModel currentRider;

    public RiderApplication() {
    }

    public RiderApplication(RiderModel currentRider) {
        this.currentRider = currentRider;
    }

    public RiderModel getCurrentRider() {
        return currentRider;
    }

    public void setCurrentRider(RiderModel currentRider) {
        this.currentRider = currentRider;
    }
}
