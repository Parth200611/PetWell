package com.mountreachsolution.petwell.User;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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


public class DiteAdd extends Fragment {

     EditText etTime, etDate, etQuantity, etDescription;
     Spinner spinnerFood, spinnerDrink;
     Button btnSelectImage, btnPostDiet;
     ImageView ivDietImage;
     String strTime, strDate,strFood,strDrink,quantity,dis,strUsername;
     ProgressDialog progressDialog;
    Bitmap bitmap;
    Uri filepath;
    private  int pick_image_request=789;

    private static final int PICK_IMAGE_REQUEST_PASS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        strUsername = getActivity()
                .getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not found

        View view= inflater.inflate(R.layout.fragment_dite_add, container, false);
        etTime = view.findViewById(R.id.etTime);
        etDate = view.findViewById(R.id.etDate);
        etQuantity = view.findViewById(R.id.etQuantity);
        etDescription = view.findViewById(R.id.etDescription);
        spinnerFood = view.findViewById(R.id.spinnerFood);
        spinnerDrink = view.findViewById(R.id.spinnerDrink);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnPostDiet = view.findViewById(R.id.btnPostDiet);
        ivDietImage = view.findViewById(R.id.ivDietImage);

        String[] foodItems = {"Dog Food", "Cat Food", "Grass", "Hay", "Fish", "Insects", "Fruits"};
        ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, foodItems);
        spinnerFood.setAdapter(foodAdapter);

        // Drink Spinner Data
        String[] drinkItems = {"Water", "Milk", "Juice", "Other"};
        ArrayAdapter<String> drinkAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, drinkItems);
        spinnerDrink.setAdapter(drinkAdapter);

        // Handle Food Selection
        spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strFood = parent.getItemAtPosition(position).toString(); // Store selected food
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strFood = ""; // Default empty
            }
        });

        // Handle Drink Selection
        spinnerDrink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDrink = parent.getItemAtPosition(position).toString(); // Store selected drink
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strDrink = ""; // Default empty
            }
        });

        etTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view1, hourOfDay, minute) -> {
                strTime = hourOfDay + ":" + String.format("%02d", minute);
                etTime.setText(strTime);
            }, 12, 0, true);
            timePickerDialog.show();
        });


        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view12, year, month, dayOfMonth) -> {
                strDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                etDate.setText(strDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectUserProfileimage();

            }
        });

        btnPostDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 quantity = etQuantity.getText().toString();
                 dis = etDescription.getText().toString();

                if (strTime == null || strDate == null || quantity.isEmpty() || dis.isEmpty() || strFood.isEmpty() || strDrink.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                   progressDialog = new ProgressDialog(getActivity());
                   progressDialog.setCanceledOnTouchOutside(false);
                   progressDialog.show();
                   postDite();

                }
            }
        });
        return view;
    }

    private void postDite() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",strUsername);
        params.put("time",strTime);
        params.put("date",strDate);
        params.put("food",strFood);
        params.put("quantity",quantity);
        params.put("dis",dis);
        params.put("drink",strDrink);
        client.post(urls.addDite,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Dite Posted !", Toast.LENGTH_SHORT).show();
                        UserImageSaveTodatabase(bitmap,dis);
                        clerdata();
                    }
                    else {
                        Toast.makeText(getActivity(), "Fail To Add Dite", Toast.LENGTH_SHORT).show();
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
                bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filepath);
                ivDietImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void UserImageSaveTodatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest =  new VolleyMultipartRequest(Request.Method.POST, urls.addditeimage, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Toast.makeText(getActivity(), "Image Save as Profil "+strTitle, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                String errorMsg = error.getMessage();
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMsg = new String(error.networkResponse.data);
                }
                Log.e("UploadError", errorMsg);
                Toast.makeText(getActivity(), "Upload Error: " + errorMsg, Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

    private byte[] getfiledatafromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void clerdata() {
        etDate.setText("");
        etDescription.setText("");
        etTime.setText("");
        etQuantity.setText("");
        ivDietImage.setImageDrawable(null); // Clear ImageView
        strDate = "";
        strTime = "";
        strDrink = "";
        strFood = "";
    }





}