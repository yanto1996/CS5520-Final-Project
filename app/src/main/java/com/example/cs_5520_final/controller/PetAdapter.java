package com.example.cs_5520_final.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cs_5520_final.R;
import com.example.cs_5520_final.model.PetModel;
import com.example.cs_5520_final.view.PetViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetViewHolder> {

    private List<PetModel> petList = new ArrayList<>();
    private Context context;

    public PetAdapter(Context context) {
        this.context = context;
    }

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

        holder.petAge.setText("Age: " + pet.getAge());
        holder.petBreed.setText("Breed: " + (pet.getBreed() != null ? pet.getBreed() : "Unknown Breed"));
        holder.Location.setText("State: " + (pet.getState() != null ? pet.getState() : "Unknown Location"));

        // Set the image based on the pet type
        if ("cat".equalsIgnoreCase(pet.getType())) {
            holder.petImage.setImageResource(R.drawable.cat_image);
        } else if ("dog".equalsIgnoreCase(pet.getType())) {
            holder.petImage.setImageResource(R.drawable.dog_image);
        } else {
            holder.petImage.setImageResource(R.drawable.profile_icon);
        }

        // Set click listener to show pet details in a dialog
        holder.itemView.setOnClickListener(v -> showPetDetailsDialog(pet));
    }

    private void showPetDetailsDialog(PetModel pet) {
        // Create a detailed description for the pet
        String description = "Name: " + pet.getName() + "\n" +
                "Type: " + pet.getType() + "\n" +
                "Age: " + pet.getAge() + "\n" +
                "Breed: " + pet.getBreed() + "\n" +
                "Gender: " + pet.getGender() + "\n" +
                "Color: " + pet.getColor() + "\n" +
                "Fur Length: " + pet.getFurLength() + "\n" +
                "Vaccinated: " + (pet.getVaccinated() == 1 ? "Yes" : "No") + "\n" +
                "State: " + pet.getState() + "\n" +
                "Description: " + pet.getDescription();

        // Show the dialog
        new AlertDialog.Builder(context)
                .setTitle("Pet Details")
                .setMessage(description)
                .setPositiveButton("Contact To Adopt", (dialog, which) -> {
                    // Code to open email app with prefilled details
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // Only email apps should handle this
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Adoption Inquiry: " + pet.getName());
                    intent.putExtra(Intent.EXTRA_TEXT, "Hello,\n\nI am interested in adopting " + pet.getName() + ". Please provide more information about the adoption process.\n\nThank you.");

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        // Show a message if no email app is available
                        Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }


    @Override
    public int getItemCount() {
        return petList.size();
    }
}
