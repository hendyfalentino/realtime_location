package com.example.mapstracking.userHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.mapstracking.MainActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0 ;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static String user_id = "user_id";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String user_id){
        editor.putBoolean("LOGIN", true);
        editor.putString("user_id", user_id);
        editor.apply();
    }

    public boolean isLogIn(){
        return sharedPreferences.getBoolean("LOGIN", false);
    }

    public void checkLogIn(){
        if(!this.isLogIn()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity)context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(user_id, sharedPreferences.getString(user_id, null));
        return user;
    }

    public void LogOut(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((MainActivity)context).finish();
    }
}
