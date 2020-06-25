package com.example.mapstracking.userHandler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mapstracking.API.ApiClient;
import com.example.mapstracking.API.ApiInterface;
import com.example.mapstracking.MainActivity;
import com.example.mapstracking.Model.CurrentLocation;
import com.example.mapstracking.Model.ErrorModel;
import com.example.mapstracking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    SessionManager sessionManager;
    private EditText user_id, user_password;
    private Button btn_login;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        user_id = findViewById(R.id.user_id);
        user_password = findViewById(R.id.user_password);
        btn_login = findViewById(R.id.btn_login);

        final String uId = user_id.getText().toString().trim();
        final String uPass = user_password.getText().toString().trim();
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
                Login(uId, uPass);
            }
        });
    }

    private void Login(final String uId, String uPass) {
        apiInterface = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<ErrorModel> call = apiInterface.loginRequest(uId, uPass);
        call.enqueue(new Callback<ErrorModel>() {
            @Override
            public void onResponse(Call<ErrorModel> call, Response<ErrorModel> response) {
                if (response.isSuccessful()){
                    try {
                        String data = response.body().getResponse();
                        JSONObject jsonObject = new JSONObject(data);   //String cannot be converted to JSONObject
                        if (jsonObject.getString("error").equals("false")){
                            sessionManager.createSession(uId);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_id", uId);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Gagal Login",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ErrorModel> call, Throwable t) {

            }

        });
    }
}