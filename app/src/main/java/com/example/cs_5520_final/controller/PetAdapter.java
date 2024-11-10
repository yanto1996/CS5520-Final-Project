package com.example.cs_5520_final.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.model.PetModel;
import com.example.cs_5520_final.view.PetViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder> {

    private List<PetModel> petList = new ArrayList<>();

    // Method to update the list of pets
    public void setPets(List<PetModel> pets) {
        this.petList = pets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        PetModel pet = petList.get(position);

        // Ensure non-null values are passed to setText
        holder.petName.setText(pet.getName() != null ? pet.getName() : "No Name");
        holder.petAge.setText(String.valueOf(pet.getAge()));
        holder.petBreed.setText(pet.getBreed() != null ? pet.getBreed() : "Unknown Breed");
        holder.Location.setText(pet.getState() != null ? pet.getState() : "Unknown Location");
    }


    @Override
    public int getItemCount() {
        return petList.size();
    }
}
