package com.muhammadelsayed.bybike_rider.Firebase;

public class Orders {
    private double Receiver_lat;
    private double Receiver_lng;
    private double Sender_Lat;
    private double Sender_Lng;
    private String created_at;
    private long id;
    private long status;
    private String updated_at;
    private String user;
    private String uuid;

    public Orders() {
    }

    public Orders(double Receiver_lat, double Receiver_lng, double Sender_Lat, double Sender_Lng, String created_at,
                  long id, long status, String updated_at, String user, String uuid) {
        this.Receiver_lat = Receiver_lat;
        this.Receiver_lng = Receiver_lng;
        this.Sender_Lat = Sender_Lat;
        this.Sender_Lng = Sender_Lng;
        this.created_at = created_at;
        this.id = id;
        this.status = status;
        this.updated_at = updated_at;
        this.user = user;
        this.uuid = uuid;
    }

    public void setReceiver_lat(double receiver_lat) {
        Receiver_lat = receiver_lat;
    }

    public void setReceiver_lng(double receiver_lng) {
        Receiver_lng = receiver_lng;
    }

    public void setSender_Lat(double sender_Lat) {
        Sender_Lat = sender_Lat;
    }

    public void setSender_Lng(double sender_Lng) {
        Sender_Lng = sender_Lng;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public double getReceiver_lat() {
        return Receiver_lat;
    }

    public double getReceiver_lng() {
        return Receiver_lng;
    }

    public double getSender_Lat() {
        return Sender_Lat;
    }

    public double getSender_Lng() {
        return Sender_Lng;
    }

    public String getCreated_at() {
        return created_at;
    }

    public long getId() {
        return id;
    }

    public long getStatus() {
        return status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", Receiver_lat='" + Receiver_lat + '\'' +
                ", Receiver_lng='" + Receiver_lng + '\'' +
                ", Sender_Lat='" + Sender_Lat + '\'' +
                ", Sender_Lng='" + Sender_Lng + '\'' +
                ", status='" + status + '\'' +
                ", user='" + user + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
