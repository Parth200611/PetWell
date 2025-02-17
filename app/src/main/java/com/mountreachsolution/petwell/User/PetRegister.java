package com.mountreachsolution.petwell.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.LoginActivity;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PetRegister extends AppCompatActivity {
     String petName, petBreed, petAge, petColor, petWeight,petGender;

    EditText etPetName;
    EditText etPetBreed;
    EditText etPetAge;
    EditText etPetColor;
    EditText etPetWeight;
    EditText etPetGender;
    String petCategory;

    AppCompatButton btnSubmitPetDetails;
    String username;
    ProgressDialog progressDialog;
    Spinner spinnerCategory;


    // Pet Categories List
    List<String> categories = Arrays.asList("Select Category", "Dog", "Bird", "Mouse", "Horse", "Cow", "Snake");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_register);
        username=getIntent().getStringExtra("Username");
        etPetName = findViewById(R.id.etPetName);
        etPetBreed = findViewById(R.id.etPetBreed);
        etPetAge = findViewById(R.id.etPetAge);
        etPetColor = findViewById(R.id.etPetColor);
        etPetWeight = findViewById(R.id.etPetWeight);
        etPetGender = findViewById(R.id.etPetGender);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Set Listener for Spinner
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                petCategory = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                petCategory = "Select Category";
            }
        });

        // Initialize Button
        btnSubmitPetDetails = findViewById(R.id.btnSubmitPetDetails);

        // Button Click Listener
        btnSubmitPetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePetDetails();
            }
        });

    }
    private void savePetDetails() {
        petName = etPetName.getText().toString().trim();
        petBreed = etPetBreed.getText().toString().trim();
        petAge = etPetAge.getText().toString().trim();
        petColor = etPetColor.getText().toString().trim();
        petWeight = etPetWeight.getText().toString().trim();
        petGender = etPetGender.getText().toString().trim();

        // Validation - Check if any field is empty
        if (petName.isEmpty() || petBreed.isEmpty() || petAge.isEmpty() || petColor.isEmpty() || petWeight.isEmpty() || petGender.isEmpty()) {
            Toast.makeText(this, "Please enter all the details!", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(PetRegister.this);
            progressDialog.show();
            postWork();
        }
    }

    private void postWork() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        params.put("name",petName);
        params.put("age",petAge);
        params.put("bread",petBreed);
        params.put("color",petColor);
        params.put("weight",petWeight);
        params.put("category",petCategory);
        params.put("gender",petGender);

        client.post(urls.petuserregister,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(PetRegister.this, "Your Dear One Is Register!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PetRegister.this, LoginActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(PetRegister.this, "Fail to Register", Toast.LENGTH_SHORT).show();
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