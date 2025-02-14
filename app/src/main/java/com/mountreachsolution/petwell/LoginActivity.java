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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

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
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        params.put("password",password);
        client.post(urls.login,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String success=response.getString("success");
                    if (success.equals("1")){
                        Toast.makeText(LoginActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this,UserHomepage.class);
                        startActivity(i);
                        saveLoginState(username);
                    }else {
                        Toast.makeText(LoginActivity.this, "Incorrect Username and Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void saveLoginState(String username) {
        getSharedPreferences("login_prefs", MODE_PRIVATE)
                .edit()
                .putString("username", username)
                .apply();
    }
}