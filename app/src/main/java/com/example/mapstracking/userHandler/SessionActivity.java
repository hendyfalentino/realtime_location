package com.example.mapstracking.userHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mapstracking.MainActivity;
import com.example.mapstracking.R;

public class SessionActivity extends AppCompatActivity {

    private int time = 1000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        sessionManager = new SessionManager(this);

        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLogIn()){
                    finish();
                    startActivity(new Intent(SessionActivity.this, MainActivity.class));
                } else {
                    Intent intent = new Intent(SessionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },time);
    }
}