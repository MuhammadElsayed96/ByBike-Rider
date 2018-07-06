package com.muhammadelsayed.bybike_rider.Model;

import java.io.Serializable;

public class EarningsModel implements Serializable {

    private Earnings[] earnings;

    public EarningsModel() {
    }

    public EarningsModel(Earnings[] earnings) {
        this.earnings = earnings;
    }

    public Earnings[] getEarnings() {
        return earnings;
    }

    public void setEarnings(Earnings[] earnings) {
        this.earnings = earnings;
    }
}
