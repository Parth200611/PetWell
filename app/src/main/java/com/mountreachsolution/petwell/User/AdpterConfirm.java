package com.mountreachsolution.petwell.User;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.petwell.Admijn.POJOGetAllapoinment;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import java.util.List;

public class AdpterConfirm extends RecyclerView.Adapter<AdpterConfirm.ViewHolder> {
    List<POJOAPPO> pojoappoList;
    Activity activity;

    public AdpterConfirm(List<POJOAPPO> pojoappoList, Activity activity) {
        this.pojoappoList = pojoappoList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterConfirm.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.getallpoinment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterConfirm.ViewHolder holder, int position) {
        POJOAPPO item = pojoappoList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPetName.setText(item.getPetname());
        holder.tvContact.setText(item.getMobile());
        holder.tvTime.setText(item.getTime());
        holder.tvDate.setText(item.getDate());
        holder.tvDis.setText(item.getDis());
        holder.tvStaus.setText(item.getStatus());
        Glide.with(activity)
                .load(urls.address + "images/"+item.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                .into(holder.tvImage);

    }

    @Override
    public int getItemCount() {
        return pojoappoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tvImage;
        TextView tvName, tvPetName, tvContact, tvTime, tvDate, tvDis,tvStaus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImage = itemView.findViewById(R.id.tvImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDis = itemView.findViewById(R.id.tvDis);
            tvStaus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
