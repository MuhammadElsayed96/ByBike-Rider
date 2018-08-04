package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class Rate implements Serializable {

    private String id;
    private String rate;
    private String user;
    private String order;
    private String transporter;
    private String created_at;
    private String updated_at;


    public Rate() {
    }

    public Rate(String id, String rate, String user, String order, String transporter, String created_at, String updated_at) {
        this.id = id;
        this.rate = rate;
        this.user = user;
        this.order = order;
        this.transporter = transporter;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
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

    @Override
    public String toString() {
        return "Rate{" +
                "id='" + id + '\'' +
                ", rate='" + rate + '\'' +
                ", user='" + user + '\'' +
                ", order='" + order + '\'' +
                ", transporter='" + transporter + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
