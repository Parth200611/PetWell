package com.mountreachsolution.petwell.User;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.VolleyMultipartRequest;
import com.mountreachsolution.petwell.urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddAppopiment extends AppCompatActivity {
    EditText tvFullName, tvPetName, etDescription, etMobileNo, etDate, etTime;
    ImageView ivMedicineImage;
    Button btnUploadImage;
    AppCompatButton btnPostSchedule;

    String name,petname,dis,mobilo,date,time,username;
    Bitmap bitmap;
    Uri filepath;
    private  int pick_image_request=789;

    private static final int PICK_IMAGE_REQUEST_PASS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_appopiment);
        username = getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not found
        tvFullName = findViewById(R.id.tvfulname);
        tvPetName = findViewById(R.id.tvPetname);
        etDescription = findViewById(R.id.etDescription);
        etMobileNo = findViewById(R.id.etMobileno);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);

        // Initializing ImageView
        ivMedicineImage = findViewById(R.id.ivMedicineImage);

        // Initializing Buttons
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnPostSchedule = findViewById(R.id.btnPostSchedule);

        // Set onClickListeners if needed
        etDate.setOnClickListener(view -> showDatePicker());
        etTime.setOnClickListener(view -> showTimePicker());
        btnPostSchedule.setOnClickListener(view -> postAppointment());
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectUserProfileimage();
            }
        });

    }



    private void postAppointment() {
        name=tvFullName.getText().toString().trim();
        mobilo=etMobileNo.getText().toString().trim();
        dis=etDescription.getText().toString().trim();
        petname=tvPetName.getText().toString().trim();
        if (name.isEmpty() || mobilo.isEmpty() || date == null || time == null || dis == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }else {
            PostData();

        }

    }

    private void PostData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        params.put("name",name);
        params.put("time",time);
        params.put("date",date);
        params.put("dis",dis);
        params.put("mobile",mobilo);
        params.put("petname",petname);

        client.post(urls.addappoinment,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String succsecc=response.getString("success");
                    if (succsecc.equals("1")){

                        Toast.makeText(AddAppopiment.this, "Appointment Request Send", Toast.LENGTH_SHORT).show();
                        UserImageSaveTodatabase(bitmap,mobilo);
                        clearData();
                    }else{
                        Toast.makeText(AddAppopiment.this, "fail to send request", Toast.LENGTH_SHORT).show();
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

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                     date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    etDate.setText(date);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Example TimePicker
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                     time = hourOfDay + ":" + String.format("%02d", minute);
                    etTime.setText(time);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void clearData() {
        etMobileNo.setText(""); tvPetName.setText(""); tvFullName.setText(""); etDescription.setText("");
        ivMedicineImage.setImageDrawable(null); // Clear ImageView

    }

    private void SelectUserProfileimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image For Profil"),pick_image_request);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==pick_image_request && resultCode==RESULT_OK && data!=null){
            filepath=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ivMedicineImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void UserImageSaveTodatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest =  new VolleyMultipartRequest(Request.Method.POST, urls.addappoimage, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Toast.makeText(AddAppopiment.this, "Image Save as Profil "+strTitle, Toast.LENGTH_SHORT).show();
                clearData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAppopiment.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                String errorMsg = error.getMessage();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMsg = new String(error.networkResponse.data);
                }
                Log.e("UploadError", errorMsg);
                Toast.makeText(AddAppopiment.this, "Upload Error: " + errorMsg, Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("tags", strTitle); // Adjusted to match PHP parameter name
                return parms;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String,VolleyMultipartRequest.DataPart> parms = new HashMap<>();
                long imagename = System.currentTimeMillis();
                parms.put("pic",new VolleyMultipartRequest.DataPart(imagename+".jpeg",getfiledatafromBitmap(bitmap)));

                return parms;

            }

        };
        Volley.newRequestQueue(AddAppopiment.this).add(volleyMultipartRequest);
    }



    private byte[] getfiledatafromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }











}