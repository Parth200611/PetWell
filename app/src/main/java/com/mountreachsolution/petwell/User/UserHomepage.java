package com.mountreachsolution.petwell.User;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mountreachsolution.petwell.Help;
import com.mountreachsolution.petwell.R;

public class UserHomepage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_homepage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.orange));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            window.getInsetsController().setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(UserHomepage.this,R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.Home);

    }
    userHome userHome1=new userHome();
    UserProfil userProfil = new UserProfil();
    AddActivity addActivity = new AddActivity();
    DiteAdd diteAdd = new DiteAdd();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Home){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,userHome1).commit();
        }else if(item.getItemId()==R.id.addactivigty){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,addActivity).commit();
        } else if(item.getItemId()==R.id.profil){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,userProfil).commit();
        }else if(item.getItemId()==R.id.addDite){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,diteAdd).commit();
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.usermenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.addmedicin){
            Intent i = new Intent(UserHomepage.this, AddMedicin.class);
            startActivity(i);

        } else if (item.getItemId() == R.id.Addappointment) {
            Intent i = new Intent(UserHomepage.this, AddAppopiment.class);
            startActivity(i);

        } else if (item.getItemId() == R.id.help) {
            Intent i = new Intent(UserHomepage.this, Help.class);
            startActivity(i);

        } else if (item.getItemId() == R.id.AboutUs) {
            Intent i = new Intent(UserHomepage.this, AboutUs.class);
            startActivity(i);

        }

        return true;
    }
}