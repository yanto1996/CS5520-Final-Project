package com.example.cs_5520_final.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_5520_final.R;

/**
 * Pet Viewholder class that extends the recycler view. The viewholder contains all the information we want the recycler view to display
 */
public class PetViewHolder extends RecyclerView.ViewHolder {
    public TextView petName;
    public TextView petBreed;
    public TextView petAge;
    public TextView Location;

    public ImageView petImage;
    public PetViewHolder(@NonNull View itemView) {
        super(itemView);
        petName = itemView.findViewById(R.id.petName);
        petBreed = itemView.findViewById(R.id.petBreed);
        petAge = itemView.findViewById(R.id.petAge);
        Location = itemView.findViewById(R.id.petLocation);
        petImage = itemView.findViewById(R.id.petImage);

    }
}
