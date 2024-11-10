package com.example.cs_5520_final.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cs_5520_final.R;

import com.example.cs_5520_final.model.PetDao;
import com.example.cs_5520_final.model.PetModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Test the PetDao here
        testPetDao();
        return view;
    }

    private void testPetDao() {
        // Initialize PetDao
        PetDao petDao = new PetDao(requireContext());

        // Fetch all pets from the database
        List<PetModel> pets = petDao.getTenPets(10);

        // Log the fetched data
        if (pets.isEmpty()) {
            Log.d(TAG, "No pets found in the database.");
        } else {
            for (PetModel pet : pets) {
                Log.d(TAG, "Pet: " + pet.getName() + ", Type: " + pet.getType() +
                        ", Age: " + pet.getAge() + ", Breed: " + pet.getBreed());
            }
        }

        // Close the database connection
        petDao.close();
    }
}