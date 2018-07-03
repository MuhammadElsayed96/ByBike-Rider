package com.muhammadelsayed.bybike_rider.Model;

public class SignUpResponse {
    private String message;
    private String token;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public SignUpResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public SignUpResponse() {
    }
}
