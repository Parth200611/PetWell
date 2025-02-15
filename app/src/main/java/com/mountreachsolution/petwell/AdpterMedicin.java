package com.mountreachsolution.petwell;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

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
