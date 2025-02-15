package com.mountreachsolution.petwell.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterMedicin extends RecyclerView.Adapter<AdpterMedicin.ViewhOlder> {
    List<POJOMEDICIN> pojomedicins;
    Activity activity;

    public AdpterMedicin(List<POJOMEDICIN> pojomedicins, Activity activity) {
        this.pojomedicins = pojomedicins;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterMedicin.ViewhOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.med,parent,false);
        return new ViewhOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterMedicin.ViewhOlder holder, int position) {
        POJOMEDICIN schedule = pojomedicins.get(position);

        // Setting data to TextViews
        holder.tvName.setText(schedule.getName());
        holder.tvTime.setText(schedule.getTime());
        holder.tvDate.setText(schedule.getDate());
        holder.tvWith.setText(schedule.getWith());
        holder.tvDis.setText(schedule.getDic());

        Glide.with(activity)
                .load(urls.address + "images/"+schedule.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                .into(holder.cvImage);
        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= schedule.getId();
                RemovethePost(schedule.getId(), holder.getAdapterPosition());
            }
        });


    }

    private void RemovethePost(String id, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(urls.removemedicin, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(activity, "Medicin  Removed!", Toast.LENGTH_SHORT).show();

                        // Remove item from list and update RecyclerView
                        pojomedicins.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pojomedicins.size());

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
                Toast.makeText(activity, "Error: Unable to Remove Diet", Toast.LENGTH_SHORT).show();
            }
        });



    }




    @Override
    public int getItemCount() {
        return pojomedicins.size();
    }

    public class ViewhOlder extends RecyclerView.ViewHolder {
        CircleImageView cvImage;
        TextView tvName, tvTime, tvDate, tvWith, tvDis;
        AppCompatButton btnDone;
        public ViewhOlder(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.cvImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvWith = itemView.findViewById(R.id.tvwith);
            tvDis = itemView.findViewById(R.id.tvDis);
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }
}
