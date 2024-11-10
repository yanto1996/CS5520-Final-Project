package com.example.cs_5520_final.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PetViewHolder extends RecyclerView.ViewHolder {
    ImageView tempImage;
    TextView petType, petName, petBreed, petAge, Location;
    public PetViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
