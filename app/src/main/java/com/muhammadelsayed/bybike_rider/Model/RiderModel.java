package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class RiderModel implements Serializable {

    private String token;
    private Rider rider;

    public RiderModel() {
    }

    public RiderModel(String token, Rider rider) {
        this.token = token;
        this.rider = rider;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    @Override
    public String toString() {
        return "RiderModel{" +
                "token='" + token + '\'' +
                ", rider=" + rider +
                '}';
    }
}
