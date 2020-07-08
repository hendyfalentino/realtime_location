package com.example.mapstracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.Nasabah;
import com.example.mapstracking.userHandler.SessionManager;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetoranActivity extends AppCompatActivity {

    ProgressBar progressBar;
    SessionManager sessionManager;
    TextView tv_nama_nasabah;
    EditText et_jumlah_setoran, et_deskripsi_setoran;
    Spinner spinner;
    String id_nasabah, nama_nasabah, jenis_setoran, jumlah_setoran, deskripsi_setoran, id_petugas;
    Button btn_input_setoran;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setoran);

        Intent intent = getIntent();
        id_nasabah = intent.getStringExtra("id_nasabah");
        nama_nasabah = intent.getStringExtra("nama_nasabah");
        tv_nama_nasabah = findViewById(R.id.tv_nama_nasabah_setoran);
        tv_nama_nasabah.setText(nama_nasabah);
        et_jumlah_setoran = findViewById(R.id.et_jumlah_setoran);
        et_deskripsi_setoran = findViewById(R.id.et_deskripsi_setoran);

        progressBar = findViewById(R.id.progress_bar_setoran);

        jumlah_setoran = et_jumlah_setoran.getText().toString().trim();
        deskripsi_setoran = et_deskripsi_setoran.getText().toString().trim();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_petugas = user.get(SessionManager.id_petugas);
        spinner = findViewById(R.id.jenis_setoran);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jenis_setoran = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_input_setoran = findViewById(R.id.btn_input_setoran);
        btn_input_setoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertSetoran(id_nasabah, jenis_setoran, jumlah_setoran, deskripsi_setoran, id_petugas);
            }
        });
    }

    private void insertSetoran(String id_nasabah, String jenis_setoran, String jumlah_setoran, String deskripsi_setoran, String id_petugas) {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.insertSetoran(id_nasabah, jenis_setoran, jumlah_setoran, deskripsi_setoran, id_petugas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SetoranActivity.this, "Data sudah disimpan", Toast.LENGTH_SHORT).show();
                Log.d("getData", response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SetoranActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show();
                Log.d("getData", t.getMessage());
            }
        });
    }
}