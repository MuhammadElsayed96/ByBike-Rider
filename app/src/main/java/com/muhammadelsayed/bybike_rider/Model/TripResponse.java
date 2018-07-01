package com.muhammadelsayed.bybike_rider.Model;

public class TripResponse {

    private String message;
    private boolean status;

    public TripResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public TripResponse() {
    }

    @Override
    public String toString() {
        return "TripResponse{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
