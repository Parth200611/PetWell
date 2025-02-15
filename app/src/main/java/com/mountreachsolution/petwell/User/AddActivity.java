package com.mountreachsolution.petwell.User;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class AddActivity extends Fragment {
    EditText etTime, etDate, etDescription;
     Spinner spinnerExercise, spinnerDuration;
     Button btnPostExercise;

     String strTime = "", strDate = "", strExercise = "", strDuration = "", strDescription = "";
     String username;
     ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_activity, container, false);
        username = getActivity()
                .getSharedPreferences("login_prefs", MODE_PRIVATE)
                .getString("username", "Guest"); // Default "Guest" if not found

        etTime = view.findViewById(R.id.etTime);
        etDate = view.findViewById(R.id.etDate);
        etDescription = view.findViewById(R.id.etDescription);
        spinnerExercise = view.findViewById(R.id.spinnerExercise);
        spinnerDuration = view.findViewById(R.id.spinnerDuration);
        btnPostExercise = view.findViewById(R.id.btnPostExercise);

        // Set up Time Picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Set up Date Picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Set up Exercise Type Spinner
        String[] exerciseTypes = {"Running", "Walking", "Jumping", "Swimming", "Playing Fetch", "Climbing", "Digging", "Training"};
        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, exerciseTypes);
        spinnerExercise.setAdapter(exerciseAdapter);
        spinnerExercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strExercise = exerciseTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strExercise = "";
            }
        });

        // Set up Duration Spinner
        String[] durationOptions = {"10 min", "20 min", "30 min", "40 min", "50 min", "1 hour", "1.5 hours", "2 hours"};
        ArrayAdapter<String> durationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, durationOptions);
        spinnerDuration.setAdapter(durationAdapter);
        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDuration = durationOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                strDuration = "";
            }
        });

        // Post Exercise Button Click
        btnPostExercise.setOnClickListener(v -> saveExerciseData());







        return view;
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (view, selectedHour, selectedMinute) -> {
            strTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            etTime.setText(strTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    // Show Date Picker Dialog
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            strDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            etDate.setText(strDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Save Exercise Data
    private void saveExerciseData() {
        strDescription = etDescription.getText().toString().trim();

        if (strTime.isEmpty() || strDate.isEmpty() || strExercise.isEmpty() || strDuration.isEmpty() || strDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
        } else {
            // Here you can send data to a database or backend API
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.show();
          postActivity();
        }
    }

    private void postActivity() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",username);
        params.put("time",strTime);
        params.put("date",strDate);
        params.put("exercis",strExercise);
        params.put("duratoion",strDuration);
        params.put("dis",strDescription);

        client.post(urls.addActivity,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Activity Posted !", Toast.LENGTH_SHORT).show();
                        clerdata();
                    }
                    else {
                        Toast.makeText(getActivity(), "Fail To Add ", Toast.LENGTH_SHORT).show();
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
    private void clerdata() {
        etDate.setText("");
        etDescription.setText("");
        etTime.setText("");

        strDate = "";
        strTime = "";
        strDuration = "";
        strExercise = "";
    }
}