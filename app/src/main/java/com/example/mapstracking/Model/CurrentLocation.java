package com.example.mapstracking.Model;

import com.google.gson.annotations.SerializedName;

public class CurrentLocation {

    public String getLocation_latitude() {
        return location_latitude;
    }

    public void setLocation_latitude(String location_latitude) {
        this.location_latitude = location_latitude;
    }

    @SerializedName("location_latitude") private String location_latitude;

    public String getLocation_longitude() {
        return location_longitude;
    }

    public void setLocation_longitude(String location_longitude) {
        this.location_longitude = location_longitude;
    }

    @SerializedName("location_longitude") private String location_longitude;

}
