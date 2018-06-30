package com.muhammadelsayed.bybike_rider.Model;

public class OrderModel {
    private String order;
    private String api_token;

    @Override
    public String toString() {
        return "OrderModel{" +
                "order='" + order + '\'' +
                ", api_token='" + api_token + '\'' +
                '}';
    }

    public OrderModel(String order, String api_token) {
        this.order = order;
        this.api_token = api_token;
    }

    public OrderModel() {
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }
}


