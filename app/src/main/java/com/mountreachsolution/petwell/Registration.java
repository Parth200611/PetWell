package com.mountreachsolution.petwell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
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

public class Registration extends AppCompatActivity {
    EditText etName, etNumber, etEmail, etAddress, etreUsername, etrePassword;
    AppCompatButton abtnRegisterNow;

    String name, number, email, address, username, password;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etreUsername = findViewById(R.id.etreUsername);
        etrePassword = findViewById(R.id.etrePassword);
        abtnRegisterNow = findViewById(R.id.abtnRegisterNow);


        abtnRegisterNow.setOnClickListener(v -> {
            name = etName.getText().toString().trim();
            number = etNumber.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            address = etAddress.getText().toString().trim();
            username = etreUsername.getText().toString().trim();
            password = etrePassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(address) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(Registration.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            } else {
               progressDialog=new ProgressDialog(Registration.this);
               progressDialog.show();
               Register();
            }


        });

    }

    private void Register() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params= new RequestParams();

        params.put("name",name);
        params.put("emailid",email);
        params.put("mobileno",number);
        params.put("username",username);
        params.put("password",password);
        params.put("address",address);

        client.post(urls.userregister,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(Registration.this, "Account Created!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Registration.this,PetRegister.class);
                        i.putExtra("Username",username);
                        startActivity(i);
                    }else{
                        Toast.makeText(Registration.this, "Fail to create account", Toast.LENGTH_SHORT).show();
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
}