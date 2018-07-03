package com.muhammadelsayed.bybike_rider.Model;

public class VehicleModel {
    private Vehicle[] vehicles;

    public VehicleModel(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }


    public VehicleModel() {
    }

    public Vehicle[] getVehicles() {
        return vehicles;
    }

    public void setVehicles(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }
}
