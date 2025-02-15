package com.mountreachsolution.petwell;

import static android.app.PendingIntent.getActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddMedicin extends AppCompatActivity {
        EditText etMedicineName, etDescription, etDate, etTime;
         Spinner spinnerWith;
         ImageView ivMedicineImage;
         Button btnUploadImage;
         AppCompatButton btnPostSchedule;
         String strDate, strTime, strWith,strname,strdis,strusername;
    Bitmap bitmap;
    Uri filepath;
    private  int pick_image_request=789;

    private static final int PICK_IMAGE_REQUEST_PASS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_medicin);
        getWindow().setStatusBarColor(ContextCompat.getColor(AddMedicin.this,R.color.orange));
        getWindow().setNavigationBarColor(ContextCompat.getColor(AddMedicin.this,R.color.white));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        strusername = getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not found
        etMedicineName = findViewById(R.id.etMedicineName);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        spinnerWith = findViewById(R.id.spinnerWith);
        ivMedicineImage = findViewById(R.id.ivMedicineImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnPostSchedule = findViewById(R.id.btnPostSchedule);

        String[] options = {"Milk", "Water", "Pet Food", "Juice"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWith.setAdapter(adapter);

        spinnerWith.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strWith = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        etDate.setOnClickListener(v -> selectDate());

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectUserProfileimage();
            }
        });


        etTime.setOnClickListener(v -> selectTime());
        btnPostSchedule.setOnClickListener(v -> postMedicineSchedule());

    }

    private void postMedicineSchedule() {
         strname = etMedicineName.getText().toString().trim();
         strdis = etDescription.getText().toString().trim();

        if (strname.isEmpty() || strdis.isEmpty() || strDate == null || strTime == null || strWith == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Postdata();
        }

    }

    private void Postdata() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
         params.put("username",strusername);
         params.put("name",strname);
         params.put("time",strTime);
         params.put("date",strDate);
         params.put("dis",strdis);
         params.put("withwhat",strWith);
         client.post(urls.addMedicin,params,new JsonHttpResponseHandler(){
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 super.onSuccess(statusCode, headers, response);
                 try {
                     String status = response.getString("success");
                     if (status.equals("1")){
                         Toast.makeText(AddMedicin.this, "Medicin Posted !", Toast.LENGTH_SHORT).show();
                         UserImageSaveTodatabase(bitmap,strname);
                     }else{
                         Toast.makeText(AddMedicin.this, "Fail to Post", Toast.LENGTH_SHORT).show();
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


    private void selectDate() {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                strDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                etDate.setText(strDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }

        // Time Picker
        private void selectTime() {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                strTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                etTime.setText(strTime);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            timePickerDialog.show();
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
        VolleyMultipartRequest volleyMultipartRequest =  new VolleyMultipartRequest(Request.Method.POST, urls.addmedicinimage, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Toast.makeText(AddMedicin.this, "Image Save as Profil "+strTitle, Toast.LENGTH_SHORT).show();
                clearData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMedicin.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                String errorMsg = error.getMessage();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMsg = new String(error.networkResponse.data);
                }
                Log.e("UploadError", errorMsg);
                Toast.makeText(AddMedicin.this, "Upload Error: " + errorMsg, Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddMedicin.this).add(volleyMultipartRequest);
    }

    private void clearData() {
        etDate.setText(""); etMedicineName.setText(""); etDescription.setText(""); etTime.setText("");
        ivMedicineImage.setImageDrawable(null); // Clear ImageView

    }

    private byte[] getfiledatafromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }








}