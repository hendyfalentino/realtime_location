package com.example.mapstracking.userHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.MainActivity;
import com.example.mapstracking.MapsActivity;
import com.example.mapstracking.Model.User;
import com.example.mapstracking.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    SessionManager sessionManager;
    private EditText user_id, user_password;
    private Button btn_login;
    String uId;
    String uPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        user_id = findViewById(R.id.user_id);
        user_password = findViewById(R.id.user_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (uId.isEmpty()){
                    user_id.setError("Please Insert User ID");
                }
                if (uPass.isEmpty()){
                    user_password.setError("Please Insert Passsword");
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
        uId = user_id.getText().toString();
        uPass = user_password.getText().toString().trim();
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<User> call = apiInterface.loginRequest(uId, uPass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null) {
                    sessionManager.createSession(uId);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}