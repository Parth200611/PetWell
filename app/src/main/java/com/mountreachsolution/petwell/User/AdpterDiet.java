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

public class AdpterDiet extends RecyclerView.Adapter<AdpterDiet.ViewHolder> {
    List<POJODiet>pojoDiets;
    Activity activity;
    POJODiet item;

    public AdpterDiet(List<POJODiet> pojoDiets, Activity activity) {
        this.pojoDiets = pojoDiets;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterDiet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dite,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterDiet.ViewHolder holder, int position) {
         item = pojoDiets.get(position);

        // Set data
        holder.tvDiteName.setText(item.getFood());
        holder.tvTime.setText(item.getTime());
        holder.tvDate.setText(item.getDate());
        holder.tvQuantity.setText(item.getQuantity());
        holder.tvDrink.setText(item.getDrink());
        holder.tvDis.setText(item.getDis());
        Glide.with(activity)
                .load(urls.address + "images/"+item.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                .into(holder.cvImage);

        holder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= item.getId();
                RemovethePost(item.getId(), holder.getAdapterPosition());
            }
        });

    }

    private void RemovethePost(String id, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(urls.removedite, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(activity, "Diet Removed!", Toast.LENGTH_SHORT).show();

                        // Remove item from list and update RecyclerView
                        pojoDiets.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, pojoDiets.size());

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
        return pojoDiets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiteName, tvTime, tvDate, tvQuantity, tvDrink, tvDis;
        CircleImageView cvImage;
        AppCompatButton btnDone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiteName = itemView.findViewById(R.id.tvDiteName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvDrink = itemView.findViewById(R.id.tvDrink);
            tvDis = itemView.findViewById(R.id.tvDis);
            cvImage = itemView.findViewById(R.id.cvImage);
            btnDone = itemView.findViewById(R.id.btnDone);

        }
    }
}
