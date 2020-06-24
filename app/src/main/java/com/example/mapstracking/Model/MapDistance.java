package com.example.mapstracking.Model;

import com.google.gson.annotations.SerializedName;

public class MapDistance {
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @SerializedName("distance") private String distance;

}
