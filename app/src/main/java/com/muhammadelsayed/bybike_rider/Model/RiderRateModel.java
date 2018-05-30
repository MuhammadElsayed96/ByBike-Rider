package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class RiderRateModel implements Serializable {

    private String five_stars;
    private String acceptance_rate;
    private String rating;

    public RiderRateModel() {
    }

    public RiderRateModel(String five_stars, String acceptance_rate, String rating) {
        this.five_stars = five_stars;
        this.acceptance_rate = acceptance_rate;
        this.rating = rating;
    }

    public String getFive_stars() {
        return five_stars;
    }

    public void setFive_stars(String five_stars) {
        this.five_stars = five_stars;
    }

    public String getAcceptance_rate() {
        return acceptance_rate;
    }

    public void setAcceptance_rate(String acceptance_rate) {
        this.acceptance_rate = acceptance_rate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RiderRateModel{" +
                "five_stars='" + five_stars + '\'' +
                ", acceptance_rate=" + acceptance_rate + '\'' +
                ", rating=" + rating + '\'' +
                '}';
    }
}
