package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class RiderInfoModel implements Serializable {

    private Rider rider;
    private String rating;
    private String total_trips;

    public RiderInfoModel() {
    }

    public RiderInfoModel(Rider rider, String rating, String total_trips) {
        this.rider = rider;
        this.rating = rating;
        this.total_trips = total_trips;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_trips() {
        return total_trips;
    }

    public void setTotal_trips(String total_trips) {
        this.total_trips = total_trips;
    }

    @Override
    public String toString() {
        return "RiderInfoModel{" +
                "rider='" + rider + '\'' +
                ", rating=" + rating + '\'' +
                ", total_trips=" + total_trips + '\'' +
                '}';
    }

}
