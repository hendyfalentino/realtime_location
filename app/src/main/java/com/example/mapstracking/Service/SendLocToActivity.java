package com.example.mapstracking.Service;

import android.location.Location;

public class SendLocToActivity {

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location;

    public SendLocToActivity(Location location){
        this.location = location;
    }
}
