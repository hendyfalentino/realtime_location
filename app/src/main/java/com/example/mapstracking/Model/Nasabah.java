package com.example.mapstracking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nasabah {

    @SerializedName("id_nasabah")
    @Expose
    private String id_nasabah;

    @SerializedName("nama_nasabah")
    private String nama_nasabah;

    @SerializedName("tempat_lahir_nasabah")
    private String tempat_lahir_nasabah;

    @SerializedName("tanggal_lahir_nasabah")
    private String tanggal_lahir_nasabah;

    @SerializedName("ktp_nasabah")
    private String ktp_nasabah;

    @SerializedName("ibu_kandung_nasabah")
    private String ibu_kandung_nasabah;

    @SerializedName("alamat_nasabah")
    private String alamat_nasabah;

    @SerializedName("nomor_nasabah")
    private String nomor_nasabah;

    @SerializedName("jumlah_penagihan")
    private String jumlah_penagihan;

    public String getId_nasabah() {
        return id_nasabah;
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

    public String getIbu_kandung_nasabah() {
        return ibu_kandung_nasabah;
    }

    public String getAlamat_nasabah() {
        return alamat_nasabah;
    }

    public String getNomor_nasabah() {
        return nomor_nasabah;
    }

    public String getJumlah_penagihan() {
        return jumlah_penagihan;
    }
}
