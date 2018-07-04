package com.muhammadelsayed.bybike_rider.Model;

public class TripModel {
    private String api_token;
    private String order;
    private String details;

    public TripModel(String api_token, String order) {
        this.api_token = api_token;
        this.order = order;
    }

    public TripModel(String api_token, String order, String details) {
        this.api_token = api_token;
        this.order = order;
        this.details = details;
    }

    public TripModel() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "TripModel{" +
                "api_token='" + api_token + '\'' +
                ", order=" + order +
                '}';
    }
}
