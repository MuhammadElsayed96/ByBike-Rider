package com.muhammadelsayed.bybike_rider;

public class test {
    private String api_token;
    private String phone;

    public test() {
    }

    public test(String api_token, String phone) {
        this.api_token = api_token;
        this.phone = phone;
    }



    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
