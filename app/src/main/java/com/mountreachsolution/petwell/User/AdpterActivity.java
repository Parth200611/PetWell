package com.mountreachsolution.petwell.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdpterActivity extends RecyclerView.Adapter<AdpterActivity.ViewHolder> {
    List<POJOGETACTIVITY> pojogetactivities;
    Activity activity;

    public AdpterActivity(List<POJOGETACTIVITY> pojogetactivities, Activity activity) {
        this.pojogetactivities = pojogetactivities;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.adtvitydata,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterActivity.ViewHolder holder, int position) {
        POJOGETACTIVITY exercise=pojogetactivities.get(position);
        holder.tvExercise.setText(exercise.getExercise());
        holder.tvTime.setText(exercise.getTime());
        holder.tvDate.setText(exercise.getDate());
        holder.tvDuration.setText(exercise.getDuration());
        holder.tvDescription.setText(exercise.getDis());

        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemovethePost(exercise.getId(), holder.getAdapterPosition());
            }
        });


    }

    private void RemovethePost(String id, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(urls.removeactivity, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(activity, "Activity Removed!", Toast.LENGTH_SHORT).show();

                        // Remove item from list and update RecyclerView
                        pojogetactivities.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pojogetactivities.size());

                    } else {
                        Toast.makeText(activity, "Failed to Remove", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "Error: Unable to Remove Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return pojogetactivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExercise, tvTime, tvDate, tvDuration, tvDescription;
        Button btnDone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExercise = itemView.findViewById(R.id.tvExercise);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDescription = itemView.findViewById(R.id.tvDiscription);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
