package com.example.mapstracking.userHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.MainActivity;
import com.example.mapstracking.Model.Petugas;
import com.example.mapstracking.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    SessionManager sessionManager;
    private EditText id_petugas, password_petugas;
    private Button btn_login;
    String uId;
    String uPass;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        id_petugas = findViewById(R.id.id_petugas);
        password_petugas = findViewById(R.id.password_petugas);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (uId.isEmpty()){
                    id_petugas.setError("Please Insert User ID");
                }
                if (uPass.isEmpty()){
                    password_petugas.setError("Please Insert Passsword");
                }
                if(!uId.isEmpty() && !uPass.isEmpty()) {
                    Login(uId, uPass);
                }

                 */
                Login();
            }
        });
    }

    private void Login(){
        progressBar.setVisibility(View.VISIBLE);
        uId = id_petugas.getText().toString();
        uPass = password_petugas.getText().toString().trim();
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<Petugas> call = apiInterface.loginRequest(uId, uPass);
        call.enqueue(new Callback<Petugas>() {
            @Override
            public void onResponse(Call<Petugas> call, Response<Petugas> response) {
                if(response.body() != null) {
                    progressBar.setVisibility(View.GONE);
                    sessionManager.createSession(uId);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Petugas> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}