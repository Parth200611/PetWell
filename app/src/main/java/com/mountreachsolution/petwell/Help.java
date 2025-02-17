package com.mountreachsolution.petwell;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mountreachsolution.petwell.User.UserHomepage;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.orange));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            window.getInsetsController().setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Button btnCallDoctor = findViewById(R.id.btnCallDoctor);
        btnCallDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doctorPhoneNumber = "+9198222 31923"; // Change this to the vet's actual number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + doctorPhoneNumber));
                startActivity(intent);
            }
        });
    }
}