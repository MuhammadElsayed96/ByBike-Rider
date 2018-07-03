package com.muhammadelsayed.bybike_rider.Model;

public class Transportation {

    private String transType;
    private int transImg;

    public Transportation() {
    }

    public Transportation(String transType, int transImg) {
        this.transType = transType;
        this.transImg = transImg;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public int getTransImg() {
        return transImg;
    }

    public void setTransImg(int transImg) {
        this.transImg = transImg;
    }
}
