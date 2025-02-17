package com.mountreachsolution.petwell.Admijn;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.petwell.R;
import com.mountreachsolution.petwell.urls;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterGetallUser extends RecyclerView.Adapter<AdpterGetallUser.ViewHolder> {
    List<POJOGETUSER> pojogetuserList;
    Activity activity;

    public AdpterGetallUser(List<POJOGETUSER> pojogetuserList, Activity activity) {
        this.pojogetuserList = pojogetuserList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdpterGetallUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.alluser,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterGetallUser.ViewHolder holder, int position) {
        POJOGETUSER user=pojogetuserList.get(position);
        holder.tvName.setText(user.getName());
        holder.tvMobileno.setText(user.getMobile());
        holder.tvEmail.setText(user.getEmail());
        holder.tvAddress.setText(user.getAddress());
        holder.tvUsername.setText(user.getUsername());
        Glide.with(activity)
                .load(urls.address + "images/"+user.getImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                .into(holder.cvImage);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,UserAllDetails.class);
                i.putExtra("id",user.getUsername());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pojogetuserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cvImage;
        TextView tvName, tvMobileno, tvEmail, tvAddress, tvUsername;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.cvImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobileno = itemView.findViewById(R.id.tvMobileno);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            card = itemView.findViewById(R.id.card);
        }
    }
}
