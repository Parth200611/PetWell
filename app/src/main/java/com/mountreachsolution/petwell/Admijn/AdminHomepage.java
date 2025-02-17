package com.mountreachsolution.petwell.Admijn;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.User.UserHomepage;

public class AdminHomepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_homepage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.orange));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            window.getInsetsController().setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(AdminHomepage.this,R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.adminHome);


    }
    AdminHome adminHome = new AdminHome();
    Adminappo adminappo=new Adminappo();
    adminprofil adminprofil1=new adminprofil();
    Viewappoinment viewappoinment= new Viewappoinment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.adminHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,adminHome).commit();
        }else if(item.getItemId()==R.id.adminappopiment){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,adminappo).commit();
        } else if(item.getItemId()==R.id.adminprofil) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome, adminprofil1).commit();
        }else if(item.getItemId()==R.id.viewappoinment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome, viewappoinment).commit();
        }
        return true;
    }
}