package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class Rider implements Serializable {

    private String id;
    private String uuid;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String image;
    private String Vehicle_ID;
    private String Vehicle_Data;
    private String api_token;
    private String created_at;
    private String updated_at;

    public void setPassword(String password) {
        this.password = password;
    }

    public Rider(String id, String uuid, String name, String email, String password, String phone, String image, String vehicle_ID, String vehicle_Data, String api_token, String created_at, String updated_at) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.image = image;
        Vehicle_ID = vehicle_ID;
        Vehicle_Data = vehicle_Data;
        this.api_token = api_token;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Rider(String api_token) {
        this.api_token = api_token;
    }

    public Rider() {
    }

    public Rider(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Rider(String id, String uuid, String name, String email, String phone, String image, String api_token, String created_at, String updated_at) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.api_token = api_token;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getPassword() {
        return password;
    }

    public String getVehicle_ID() {
        return Vehicle_ID;
    }

    public void setVehicle_ID(String vehicle_ID) {
        this.Vehicle_ID = vehicle_ID;
    }

    public String getVehicle_Data() {
        return Vehicle_Data;
    }

    public void setVehicle_Data(String vehicle_Data) {
        this.Vehicle_Data = vehicle_Data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
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
        return "Rider{" +
                "id='" + id + '\'' +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + image + '\'' +
                ", api_token='" + api_token + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
