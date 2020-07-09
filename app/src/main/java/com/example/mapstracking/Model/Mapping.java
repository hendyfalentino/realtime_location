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

    public String getNama_nasabah() {
        return nama_nasabah;
    }

    public String getTempat_lahir_nasabah() {
        return tempat_lahir_nasabah;
    }

    public String getTanggal_lahir_nasabah() {
        return tanggal_lahir_nasabah;
    }

    public String getKtp_nasabah() {
        return ktp_nasabah;
    }

    @SerializedName("id_mapping") private String id_mapping;

    @SerializedName("latitude_mapping") private String latitude_mapping;

    @SerializedName("longitude_mapping") private String longitude_mapping;

    @SerializedName("status_mapping") private String status_mapping;

    @SerializedName("nama_nasabah") private String nama_nasabah;

    @SerializedName("tempat_lahir_nasabah") private String tempat_lahir_nasabah;

    @SerializedName("tanggal_lahir_nasabah") private String tanggal_lahir_nasabah;

    @SerializedName("ktp_nasabah") private String ktp_nasabah;

}
