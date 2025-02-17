package com.mountreachsolution.petwell.Admijn;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.User.UserHomepage;
import com.mountreachsolution.petwell.User.UserProfil;
import com.mountreachsolution.petwell.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAllDetails extends AppCompatActivity {

    TextView tvName1, tvMobileno, tvEmail, tvAddress, tvUsername;
     TextView tvName, tvBreed, tvAge, tvColor, tvWeight,tvGender;
     CircleImageView cvImage1, cvImage;
     AppCompatButton btnremove;
     String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_all_details);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.orange));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false);
            window.getInsetsController().setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(UserAllDetails.this,R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        username=getIntent().getStringExtra("id");

        cvImage1 = findViewById(R.id.cvImage1);
        tvName1 = findViewById(R.id.tvName1);
        tvMobileno = findViewById(R.id.tvMobileno);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvUsername = findViewById(R.id.tvUsername);

        // Initialize Pet Details Views
        cvImage = findViewById(R.id.cvImage);
        tvName = findViewById(R.id.tvName);
        tvBreed = findViewById(R.id.tvBreed);
        tvAge = findViewById(R.id.tvAge);
        tvColor = findViewById(R.id.tvColor);
        tvWeight = findViewById(R.id.tvweight);
        tvGender=findViewById(R.id.tvGender);

        btnremove=findViewById(R.id.btnRemove);
        getData(username);
        getPetdata(username);
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemovethePost(username);
            }
        });



    }
    private void getData(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        client.post(urls.profil,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getProfildata");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name=jsonObject.getString("name");
                        String mobileno=jsonObject.getString("mobileno");
                        String address=jsonObject.getString("address");
                        String email=jsonObject.getString("email");
                        String image=jsonObject.getString("image");
                        tvName1.setText(name);
                        tvMobileno.setText(mobileno);
                        tvAddress.setText(address);
                        tvEmail.setText(email);
                        tvUsername.setText(username);
                        Glide.with(UserAllDetails.this)
                                .load(urls.address + "images/"+image)
                                .skipMemoryCache(true)
                                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                                .into(cvImage1);
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
    private void getPetdata(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        client.post(urls.getpetdata,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getPetdata");
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String name=jsonObject.getString("name");
                        String age=jsonObject.getString("age");
                        String bread=jsonObject.getString("bread");
                        String color=jsonObject.getString("color");
                        String weight=jsonObject.getString("weight");
                        String image=jsonObject.getString("image");
                        String gender=jsonObject.getString("gender");

                        tvAge.setText(age);
                        tvName.setText(name);
                        tvBreed.setText(bread);
                        tvColor.setText(color);
                        tvWeight.setText(weight);
                        tvGender.setText(gender);
                        Glide.with(UserAllDetails.this)
                                .load(urls.address + "images/"+image)
                                .skipMemoryCache(true)
                                .error(R.drawable.baseline_pets_24)// Resize the image to 800x800 pixels
                                .into(cvImage);
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
    private void RemovethePost(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);

        client.post(urls.removeUser, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(UserAllDetails.this, "Activity Removed!", Toast.LENGTH_SHORT).show();

                      Intent i = new Intent(UserAllDetails.this,AdminHomepage.class);
                      startActivity(i);
                      finish();


                    } else {
                        Toast.makeText(UserAllDetails.this, "Failed to Remove", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }




}