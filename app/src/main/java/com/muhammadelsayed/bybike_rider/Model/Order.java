package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class Order implements Serializable {

    private int id;
    private String uuid;
    private int status;
    private String Sender_Lat;
    private String Sender_Lng;
    private String Receiver_lat;
    private String Receiver_lng;
    private String Created_at;
    private String Updated_at;
    private Client user;
    private Rate rate;

    public Order() {
    }

    public Order(int id, String uuid, int status, String sender_Lat, String sender_Lng, String receiver_lat, String receiver_lng, Client user, String created_at, String updated_at, Rate rate) {
        this.id = id;
        this.uuid = uuid;
        this.status = status;
        Sender_Lat = sender_Lat;
        Sender_Lng = sender_Lng;
        Receiver_lat = receiver_lat;
        Receiver_lng = receiver_lng;
        Created_at = created_at;
        Updated_at = updated_at;
        this.user = user;
        this.rate = rate;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSender_Lat() {
        return Sender_Lat;
    }

    public void setSender_Lat(String sender_Lat) {
        Sender_Lat = sender_Lat;
    }

    public String getSender_Lng() {
        return Sender_Lng;
    }

    public void setSender_Lng(String sender_Lng) {
        Sender_Lng = sender_Lng;
    }

    public String getReceiver_lat() {
        return Receiver_lat;
    }

    public void setReceiver_lat(String receiver_lat) {
        Receiver_lat = receiver_lat;
    }

    public String getReceiver_lng() {
        return Receiver_lng;
    }

    public void setReceiver_lng(String receiver_lng) {
        Receiver_lng = receiver_lng;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String created_at) {
        Created_at = created_at;
    }

    public String getUpdated_at() {
        return Updated_at;
    }

    public void setUpdated_at(String updated_at) {
        Updated_at = updated_at;
    }

    public Client getUser() {
        return user;
    }

    public void setUser(Client user) {
        this.user = user;
    }

}
