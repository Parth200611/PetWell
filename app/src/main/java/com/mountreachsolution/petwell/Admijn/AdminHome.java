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
import com.mountreachsolution.petwell.urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdminHome extends Fragment {
    RecyclerView rvList;
     TextView tvNoUser, tvTitle;
     List<POJOGETUSER>pojogetuserList;
     AdpterGetallUser adpterGetallUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_admin_home, container, false);
        rvList = view.findViewById(R.id.rvList);
        tvNoUser = view.findViewById(R.id.tvNoUser);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        pojogetuserList=new ArrayList<>();
        getAllUser();

        return view;
    }

    private void getAllUser() {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urls.getAllUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray =jsonObject.getJSONArray("getProfildata");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String mobileno = jsonObject1.getString("mobileno");
                        String address = jsonObject1.getString("address");
                        String email = jsonObject1.getString("email");
                        String username = jsonObject1.getString("username");
                        String password = jsonObject1.getString("password");
                        String image = jsonObject1.getString("image");
                        pojogetuserList.add(new POJOGETUSER(id,name,mobileno,address,email,username,password,image));
                    }
                    adpterGetallUser=new AdpterGetallUser(pojogetuserList,getActivity());
                    rvList.setAdapter(adpterGetallUser);
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