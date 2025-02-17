package com.mountreachsolution.petwell.Admijn;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mountreachsolution.petwell.LoginActivity;
import com.mountreachsolution.petwell.R;


public class adminprofil extends Fragment {

   AppCompatButton btnlogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      
        View view= inflater.inflate(R.layout.fragment_adminprofil, container, false);
        btnlogout=view.findViewById(R.id.btnAdminLOGout);
        
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        return view ;
    }

    private void Logout() {

        SharedPreferences prefs = getActivity().getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("username");
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }
}