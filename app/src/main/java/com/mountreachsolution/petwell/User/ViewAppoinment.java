package com.mountreachsolution.petwell.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAppoinment extends AppCompatActivity {
    RecyclerView rvList;
    TextView tvNoUser, tvTitle;
    String id,username,time,date,name,mobile,petname,dis,image;
    List<POJOAPPO>pojoappos;
    String Username;
    SharedPreferences sharedPreferences;
    AdpterConfirm adpterGetAllAppo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_appoinment);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.orange));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.white));
        Username = getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not found
        rvList = findViewById(R.id.rvList);
        tvNoUser = findViewById(R.id.tvNoUser);

        rvList.setLayoutManager(new LinearLayoutManager(ViewAppoinment.this));
        pojoappos=new ArrayList<>();
        getData(Username);
    }

    private void getData(final String username) {  // Accept username as a parameter
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.getconbyusername,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("getAllAppo");

                            pojoappos.clear(); // Clear old data before adding new

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String id = jsonObject1.getString("id");
                                String username = jsonObject1.getString("username");
                                String time = jsonObject1.getString("time");
                                String date = jsonObject1.getString("date");
                                String name = jsonObject1.getString("name");
                                String mobile = jsonObject1.getString("mobile");
                                String petname = jsonObject1.getString("petname");
                                String dis = jsonObject1.getString("dis");
                                String image = jsonObject1.getString("image");  String status = jsonObject1.getString("status");

                                pojoappos.add(new POJOAPPO(id, username, time, date, name, mobile, petname, dis, image,status));
                            }

                            adpterGetAllAppo = new AdpterConfirm(pojoappos, ViewAppoinment.this);
                            rvList.setAdapter(adpterGetAllAppo);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewAppoinment.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username); // Pass username as a parameter
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

}