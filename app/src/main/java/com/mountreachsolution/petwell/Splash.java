package com.mountreachsolution.petwell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.mountreachsolution.petwell.User.AdminHomepage;
import com.mountreachsolution.petwell.User.UserHomepage;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Hide the status bar and navigation bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", null);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (username != null) {
                    // Redirect to Admin or User Homepage
                    if (username.equals("Admin")) {
                        startActivity(new Intent(Splash.this, AdminHomepage.class));
                    } else {
                        startActivity(new Intent(Splash.this, UserHomepage.class));
                    }
                } else {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                }
            }
        },3000);

    }
}