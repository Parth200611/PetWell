package com.mountreachsolution.petwell.Admijn;

import static androidx.core.app.ActivityCompat.recreate;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import kotlinx.coroutines.channels.ChannelResult;

public class AdpterGetAllAppo extends RecyclerView.Adapter<AdpterGetAllAppo.ViewHolder> {
    List<POJOGetAllapoinment> pojoGetAllapoinmentList;
    Activity activity;

    public AdpterGetAllAppo(List<POJOGetAllapoinment> pojoGetAllapoinmentList, Activity activity) {
        this.pojoGetAllapoinmentList = pojoGetAllapoinmentList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterGetAllAppo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.getallaapo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterGetAllAppo.ViewHolder holder, int position) {
        POJOGetAllapoinment item = pojoGetAllapoinmentList.get(position);


        holder.tvName.setText(item.getName());
        holder.tvPetName.setText(item.getPetname());
        holder.tvContact.setText(item.getMobile());
        holder.tvTime.setText(item.getTime());
        holder.tvDate.setText(item.getDate());
        holder.tvDis.setText(item.getDis());
        Glide.with(activity)
                .load(urls.address + "images/"+item.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                .into(holder.tvImage);

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status="Accepet";
                String username=item.getUsername(); String time=item.getTime(); String date=item.getDate();
                String name=item.getName(); String mobile=item.getMobile(); String petname=item.getPetname();
                String image=item.getImage();
                String dis=item.getDis();

                PostData(status,username,time,date,name,mobile,petname,image,dis);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reject="Reject";
                String username=item.getUsername(); String time=item.getTime(); String date=item.getDate();
                String name=item.getName(); String mobile=item.getMobile(); String petname=item.getPetname();
                String image=item.getImage();
                String dis=item.getDis();
                RejectDta(reject,username,time,date,name,mobile,petname,image,dis);

            }
        });


    }

    private void PostData(String status, String username, String time, String date, String name, String mobile, String petname, String image, String dis) {
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",name);
        params.put("time",time);
        params.put("date",date);
        params.put("username",username);
        params.put("dis",dis);
        params.put("mobile",mobile);
        params.put("petname",petname);
        params.put("status",status);
        params.put("image",image);
        client.post(urls.addconfirmappo,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        Toast.makeText(activity, "Appoinment Confirm", Toast.LENGTH_SHORT).show();
                        deleteRequest(username);
                    }else{
                        Toast.makeText(activity, "Fail to accept", Toast.LENGTH_SHORT).show();
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
 private void RejectDta(String status, String username, String time, String date, String name, String mobile, String petname, String image, String dis) {
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("name",name);
        params.put("time",time);
        params.put("date",date);
        params.put("username",username);
        params.put("dis",dis);
        params.put("mobile",mobile);
        params.put("petname",petname);
        params.put("status",status);
        params.put("image",image);
        client.post(urls.addconfirmappo,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status=response.getString("success");
                    if (status.equals("1")){
                        Toast.makeText(activity, "Appoinment Reject", Toast.LENGTH_SHORT).show();
                        deleteRequest(username);
                    }else{
                        Toast.makeText(activity, "Fail to accept", Toast.LENGTH_SHORT).show();
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

    private void deleteRequest(String username) {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        client.post(urls.removeAppo,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String string=response.getString("status");
                    if (string.equals("success")){
                        Toast.makeText(activity, "Request Removed", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < pojoGetAllapoinmentList.size(); i++) {
                            if (pojoGetAllapoinmentList.get(i).getUsername().equals(username)) {
                                pojoGetAllapoinmentList.remove(i);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, pojoGetAllapoinmentList.size());
                                break;
                            }
                        }
                    }else{
                        Toast.makeText(activity, "Fail to do this", Toast.LENGTH_SHORT).show();
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



    @Override
    public int getItemCount() {
        return pojoGetAllapoinmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tvImage;
        TextView tvName, tvPetName, tvContact, tvTime, tvDate, tvDis;
        Button btnAccept, btnReject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImage = itemView.findViewById(R.id.tvImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDis = itemView.findViewById(R.id.tvDis);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
