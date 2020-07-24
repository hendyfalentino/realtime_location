package com.example.mapstracking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.Model.Nasabah;
import com.example.mapstracking.userHandler.SessionManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

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
    String id_nasabah, nama_nasabah, jenis_setoran, jumlah_setoran, deskripsi_setoran, id_petugas, menu, id_mapping;
    Button btn_input_setoran, btn_home_setoran;
    ApiInterface apiInterface;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setoran);
        progressBar = findViewById(R.id.progress_bar_setoran);
        tv_nama_nasabah = findViewById(R.id.tv_nama_nasabah_setoran);
        et_jumlah_setoran = findViewById(R.id.et_jumlah);
        et_deskripsi_setoran = findViewById(R.id.et_deskripsi);
        spinner = findViewById(R.id.jenis_setoran);

        intent = getIntent();
        menu = intent.getStringExtra("map");
        if(menu != null){
            id_mapping = intent.getStringExtra("id_marker");
            getNasabah(id_mapping);
            intent = new Intent(SetoranActivity.this, MapsActivity.class);
        }else{
            id_nasabah = intent.getStringExtra("id_nasabah");
            nama_nasabah = intent.getStringExtra("nama_nasabah");
            tv_nama_nasabah.setText(nama_nasabah);
            intent = new Intent(SetoranActivity.this, MainActivity.class);
        }

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_petugas = user.get(SessionManager.id_petugas);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jenis_setoran = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_jumlah_setoran.addTextChangedListener(onTextChangedListener());

        btn_input_setoran = findViewById(R.id.btn_input_setoran);
        btn_input_setoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumlah_setoran = et_jumlah_setoran.getText().toString().replaceAll(",", "");
                deskripsi_setoran = et_deskripsi_setoran.getText().toString().trim();
                insertSetoran(id_nasabah, jenis_setoran, jumlah_setoran, deskripsi_setoran, id_petugas);
            }
        });

        btn_home_setoran = findViewById(R.id.btn_home_setoran);
        btn_home_setoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SetoranActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getNasabah(String id_mapping) {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Nasabah> call = apiInterface.getNasabah(id_mapping);
        call.enqueue(new Callback<Nasabah>() {
            @Override
            public void onResponse(Call<Nasabah> call, Response<Nasabah> response) {
                if(response.body() != null) {
                    id_nasabah = response.body().getId_nasabah();
                    nama_nasabah = response.body().getNama_nasabah();
                    tv_nama_nasabah.setText(nama_nasabah);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Nasabah> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
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
                changeDestLocStatus(id_mapping);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SetoranActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show();
                Log.d("getData", t.toString());
            }
        });
    }

    public void changeDestLocStatus(String id){
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.setStatusMapping(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SetoranActivity.this, "Data sudah disimpan", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }

        });
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_jumlah_setoran.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    et_jumlah_setoran.setText(formattedString);
                    et_jumlah_setoran.setSelection(et_jumlah_setoran.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                et_jumlah_setoran.addTextChangedListener(this);
            }
        };
    }
}