package com.example.mapstracking.Model;

import com.google.gson.annotations.SerializedName;

public class Mapping {

    public String getLatidue_mapping() {
        return latitude_mapping;
    }

    public String getLongitude_mapping() {
        return longitude_mapping;
    }

    public String getId_mapping() {
        return id_mapping;
    }

    public String getStatus_mapping() {
        return status_mapping;
    }

    @SerializedName("id_mapping") private String id_mapping;

    @SerializedName("latitude_mapping") private String latitude_mapping;

    @SerializedName("longitude_mapping") private String longitude_mapping;

    @SerializedName("status_mapping") private String status_mapping;

}
