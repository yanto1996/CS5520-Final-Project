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

        if (pet.getName() != null) {
            holder.petName.setText(pet.getName());
        } else {
            holder.petName.setText("No Name");
        }

        holder.petAge.setText("Age: " + String.valueOf(pet.getAge()));

        if (pet.getBreed() != null) {
            holder.petBreed.setText("Breed: " + pet.getBreed());
        } else {
            holder.petBreed.setText("Unknown Breed");
        }

        if (pet.getState() != null) {
            holder.Location.setText("State: " + pet.getState());
        } else {
            holder.Location.setText("Unknown Location");
        }
    }


    @Override
    public int getItemCount() {
        return petList.size();
    }
}
