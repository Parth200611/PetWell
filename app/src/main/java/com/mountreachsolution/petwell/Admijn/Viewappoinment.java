package com.mountreachsolution.petwell.Admijn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.User.ViewAppoinment;
import com.mountreachsolution.petwell.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Viewappoinment extends Fragment {
    RecyclerView rvList;
    TextView tvNoUser, tvTitle;
    String id,username,time,date,name,mobile,petname,dis,image;
    List<POJOALLAPP>pojoallapps;
    Adpterconfirm adpterGetAllAppo;
    

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_viewappoinment, container, false);
        rvList = view.findViewById(R.id.rvList);
        tvNoUser = view.findViewById(R.id.tvNoUser);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        pojoallapps=new ArrayList<>();
        getData();
        return view;
    }

    private void getData() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, urls.getcoonfirappo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("getAllAppo");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String username = jsonObject1.getString("username");
                        String time = jsonObject1.getString("time");
                        String date = jsonObject1.getString("date");
                        String name = jsonObject1.getString("name");
                        String mobile = jsonObject1.getString("mobile");
                        String petname = jsonObject1.getString("petname");
                        String dis = jsonObject1.getString("dis");
                        String image = jsonObject1.getString("image");  String status = jsonObject1.getString("status");
                        pojoallapps.add(new POJOALLAPP(id,username,time,date,name,mobile,petname,dis,image,status));

                    }
                    adpterGetAllAppo = new Adpterconfirm(pojoallapps,getActivity());
                    rvList.setAdapter(adpterGetAllAppo);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}