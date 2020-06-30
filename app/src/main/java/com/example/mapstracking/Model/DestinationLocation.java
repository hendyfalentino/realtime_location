package com.example.mapstracking.Model;

import com.google.gson.annotations.SerializedName;

public class DestinationLocation {

    public String getDest_loc_lat() {
        return dest_loc_lat;
    }

    public void setDest_loc_lat(String dest_loc_lat) {
        this.dest_loc_lat = dest_loc_lat;
    }

    public String getDest_loc_lng() {
        return dest_loc_lng;
    }

    public void setDest_loc_lng(String dest_loc_lng) {
        this.dest_loc_lng = dest_loc_lng;
    }

    public String getDest_loc_id() {
        return dest_loc_id;
    }

    public void setDest_loc_id(String dest_loc_id) {
        this.dest_loc_id = dest_loc_id;
    }

    @SerializedName("dest_loc_id") private String dest_loc_id;

    @SerializedName("dest_loc_lat") private String dest_loc_lat;

    @SerializedName("dest_loc_lng") private String dest_loc_lng;

    public String getDest_loc_status() {
        return dest_loc_status;
    }

    public void setDest_loc_status(String dest_loc_status) {
        this.dest_loc_status = dest_loc_status;
    }

    @SerializedName("dest_loc_status") private String dest_loc_status;

}
