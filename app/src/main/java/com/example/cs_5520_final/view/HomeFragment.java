package com.example.cs_5520_final.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.PetAdapter;
import com.example.cs_5520_final.model.PetViewModel;

public class HomeFragment extends Fragment {

    private PetViewModel petViewModel;
    private PetAdapter petAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Step 1: Find the RecyclerView
        RecyclerView petRecyclerView = view.findViewById(R.id.petRecyclerView);

        // Step 2: Initialize the adapter
        petAdapter = new PetAdapter();

        // Step 3: Set up the RecyclerView
        petRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        petRecyclerView.setAdapter(petAdapter);

        // Step 4: Initialize the ViewModel
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);

        // Step 5: Observe the LiveData from ViewModel
        petViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            // Update the adapter's data when the data changes
            petAdapter.setPets(pets);
        });

        // Step 6: Fetch the pets (e.g., limit to 10 pets)
        petViewModel.fetchPets(10);
    }
}
