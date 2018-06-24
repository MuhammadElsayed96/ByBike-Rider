package com.muhammadelsayed.bybike_rider.Firebase;

public class Orders {
    private String Receiver_lat;
    private String Receiver_lng;
    private String Sender_Lat;
    private String Sender_Lng;
    private String created_at;
    private long id;
    private long status;
    private String updated_at;
    private String user;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public Orders(){}

    public Orders(String Receiver_lat, String Receiver_lng, String Sender_Lat, String Sender_Lng, String created_at,
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

    public String getReceiver_lat() {
        return Receiver_lat;
    }

    public String getReceiver_lng() {
        return Receiver_lng;
    }

    public String getSender_Lat() {
        return Sender_Lat;
    }

    public String getSender_Lng() {
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
