package com.muhammadelsayed.bybike_rider.Model;

public class Vehicle {
    private String id;
    private String type;
    private String Initial_Price;
    private String Min_Price;
    private String KM_Price;
    private String created_at;
    private String updated_at;


    public Vehicle() {
    }

    public Vehicle(String id, String type, String initial_Price, String min_Price, String KM_Price, String created_at, String updated_at) {
        this.id = id;
        this.type = type;
        Initial_Price = initial_Price;
        Min_Price = min_Price;
        this.KM_Price = KM_Price;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInitial_Price() {
        return Initial_Price;
    }

    public void setInitial_Price(String initial_Price) {
        Initial_Price = initial_Price;
    }

    public String getMin_Price() {
        return Min_Price;
    }

    public void setMin_Price(String min_Price) {
        Min_Price = min_Price;
    }

    public String getKM_Price() {
        return KM_Price;
    }

    public void setKM_Price(String KM_Price) {
        this.KM_Price = KM_Price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
