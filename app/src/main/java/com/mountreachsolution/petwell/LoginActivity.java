package com.mountreachsolution.petwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
     AppCompatButton abtnLogin;
     CheckBox cbShowPassword;
     TextView tvNewUser;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbShowPassword = findViewById(R.id.cbshowpassword);
        abtnLogin = findViewById(R.id.abtnLogin);
        tvNewUser = findViewById(R.id.tvNewuser);


        abtnLogin.setOnClickListener(v -> {
             username = etUsername.getText().toString().trim();
             password = etPassword.getText().toString().trim();
             if (username.isEmpty()){
                 Toast.makeText(this, "Enter UserName", Toast.LENGTH_SHORT).show();
             } else if (password.isEmpty()) {
                 Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
             }else if(username.equals("Admin") && password.equals("Admin")){
                 Intent i = new Intent(LoginActivity.this,AdminHomepage.class);
                 startActivity(i);
             }else {
                 LogIN();
             }


        });


        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etPassword.setSelection(etPassword.getText().length());
        });


        tvNewUser.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this,Registration.class);
            startActivity(i);

        });
    }

    private void LogIN() {
    }
}