package com.mountreachsolution.petwell;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;


public class userHome extends Fragment {
    CircleImageView cvImage;
     TextView tvName, tvBreed, tvAge, tvColor, tvWeight;
     Button btnAddImage;
     RecyclerView rvListActivity, rvListDiet, rvListMed;
     TextView tvNoActivity, tvNoDiet, tvNoMedicine;
     String username;
    Bitmap bitmap;
    Uri filepath;
    private  int pick_image_request=789;

    private static final int PICK_IMAGE_REQUEST_PASS = 1;
    List<POJOGETACTIVITY> pojogetactivities;
    AdpterActivity adpterActivity;

    List<POJODiet>pojoDiets;
    List<POJOMEDICIN> pojomedicins;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_user_home, container, false);
        username = getActivity()
                .getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not
        cvImage = view.findViewById(R.id.cvImage);
        tvName = view.findViewById(R.id.tvName);
        tvBreed = view.findViewById(R.id.tvBreed);
        tvAge = view.findViewById(R.id.tvAge);
        tvColor = view.findViewById(R.id.tvColor);
        tvWeight = view.findViewById(R.id.tvweight);
        btnAddImage = view.findViewById(R.id.btnAddimage);

        rvListActivity = view.findViewById(R.id.rvListActvity);
        pojogetactivities=new ArrayList<>();
        rvListDiet = view.findViewById(R.id.rvListDite);
        pojoDiets=new ArrayList<>();
        rvListMed = view.findViewById(R.id.rvListMed);
        pojomedicins=new ArrayList<>();

        rvListActivity.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvListDiet.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvListMed.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        

        tvNoActivity = view.findViewById(R.id.tvnoActivity);
        tvNoDiet = view.findViewById(R.id.tvnoDiet);
        tvNoMedicine = view.findViewById(R.id.tvnoMedicin);
        getPetdata(username);
        getAcitvitydata(username);
        getDiet(username);
        getMedicin(username);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectUserProfileimage();
            }
        });


        return view;
    }

    private void getMedicin(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(urls.getMedicin,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d("API_SUCCESS", "Response: " + response.toString());
                    JSONArray jsonArray = response.getJSONArray("getActivity");



                    if (jsonArray.length()==0){
                        rvListMed.setVisibility(View.GONE);
                        tvNoMedicine.setVisibility(View.VISIBLE);
                    }
                    for (int i =0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String strusername=jsonObject.getString("username");
                        String strtime=jsonObject.getString("time");
                        String strdate=jsonObject.getString("date");
                        String strname=jsonObject.getString("name");
                        String strwith=jsonObject.getString("withwhat");
                        String strdis=jsonObject.getString("dis");
                        String strimage=jsonObject.getString("image");
                        pojomedicins.add(new POJOMEDICIN(id,strusername,strtime,strdate,strname,strdis,strwith,strimage));
                    }
                    AdpterMedicin adpterMedicin = new AdpterMedicin(pojomedicins,getActivity());
                    rvListMed.setAdapter(adpterMedicin);
                    rvListMed.setAdapter(adpterMedicin);
                    rvListMed.setVisibility(View.VISIBLE);
                    tvNoMedicine.setVisibility(View.GONE);
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

    private void getDiet(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(urls.getDite,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("getDiet");
                    if (jsonArray.length()==0){
                        rvListDiet.setVisibility(View.GONE);
                        tvNoDiet.setVisibility(View.VISIBLE);
                    }
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String strusername=jsonObject.getString("username");
                        String strtime=jsonObject.getString("time");
                        String strdate=jsonObject.getString("date");
                        String strfood=jsonObject.getString("food");
                        String strquantity=jsonObject.getString("quantity");
                        String strdis=jsonObject.getString("dis");
                        String strDrink=jsonObject.getString("drink");
                        String strimage=jsonObject.getString("image");
                        pojoDiets.add(new POJODiet(id,strusername,strtime,strdate,strfood,strquantity,strdis,strDrink,strimage));

                    }
                    AdpterDiet adpterDiet = new AdpterDiet(pojoDiets,getActivity());
                    rvListDiet.setAdapter(adpterDiet);



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

    private void getAcitvitydata(String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(urls.getActivitydata,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray=response.getJSONArray("getActivity");
                    if (jsonArray.length()==0){
                        rvListActivity.setVisibility(View.GONE);
                        tvNoActivity.setVisibility(View.VISIBLE);
                    }
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String id=jsonObject.getString("id");
                        String strusername=jsonObject.getString("username");
                        String strtime=jsonObject.getString("time");
                        String strdate=jsonObject.getString("date");
                        String strexercise=jsonObject.getString("exercis");
                        String strduration=jsonObject.getString("duratoion");
                        String strdis=jsonObject.getString("dis");
                        pojogetactivities.add(new POJOGETACTIVITY(id,strusername,strtime,strdate,strexercise,strduration,strdis));
                    }
                    adpterActivity=new AdpterActivity(pojogetactivities,getActivity());
                    rvListActivity.setAdapter(adpterActivity);
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

                        tvAge.setText(age);
                        tvName.setText(name);
                        tvBreed.setText(bread);
                        tvColor.setText(color);
                        tvWeight.setText(weight);
                        Glide.with(getActivity())
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
                cvImage.setImageBitmap(bitmap);
                UserImageSaveTodatabase(bitmap,username);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void UserImageSaveTodatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest =  new VolleyMultipartRequest(Request.Method.POST, urls.petImage, new Response.Listener<NetworkResponse>() {
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






}