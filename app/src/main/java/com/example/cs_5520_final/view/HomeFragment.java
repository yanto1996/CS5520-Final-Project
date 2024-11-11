package com.example.cs_5520_final.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        recyclerViewSetUp(view);
        petViewModel.fetchPets(10);
        setUpSearch(view);
    }

    private void recyclerViewSetUp(View view){
        RecyclerView petRecyclerView = view.findViewById(R.id.petRecyclerView);
        petAdapter = new PetAdapter(getContext());
        petRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        petRecyclerView.setAdapter(petAdapter);
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        petViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            // Update the adapter's data when the data changes
            petAdapter.setPets(pets);
        });
    }

    private void setUpSearch(View view) {
        EditText searchTypeBar = view.findViewById(R.id.searchBar1);
        EditText searchStateBar = view.findViewById(R.id.searchBar2);
        EditText searchAgeBar = view.findViewById(R.id.searchAgeBar);

        Button searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String typeSearch = searchTypeBar.getText().toString().trim();
            String stateSearch = searchStateBar.getText().toString().trim();
            String ageSearch = searchAgeBar.getText().toString().trim();

            // If no search criteria is provided, do nothing
            if (TextUtils.isEmpty(typeSearch) && TextUtils.isEmpty(stateSearch) && TextUtils.isEmpty(ageSearch)) {
                return;
            }

            // All three filters: type, state, and age
            if (!TextUtils.isEmpty(typeSearch) && !TextUtils.isEmpty(stateSearch) && !TextUtils.isEmpty(ageSearch)) {
                try {
                    int age = Integer.parseInt(ageSearch);
                    petViewModel.searchByTypeStateAndAge(typeSearch, stateSearch, age);
                } catch (NumberFormatException e) {
                    searchAgeBar.setError("Invalid age");
                }
            }
            // Type and age only
            else if (!TextUtils.isEmpty(typeSearch) && !TextUtils.isEmpty(ageSearch)) {
                try {
                    int age = Integer.parseInt(ageSearch);
                    petViewModel.searchByTypeAndAge(typeSearch, age);
                } catch (NumberFormatException e) {
                    searchAgeBar.setError("Invalid age");
                }
            }
            // State and age only
            else if (!TextUtils.isEmpty(stateSearch) && !TextUtils.isEmpty(ageSearch)) {
                try {
                    int age = Integer.parseInt(ageSearch);
                    petViewModel.searchByStateAndAge(stateSearch, age);
                } catch (NumberFormatException e) {
                    searchAgeBar.setError("Invalid age");
                }
            }
            // Only age
            else if (!TextUtils.isEmpty(ageSearch)) {
                try {
                    int age = Integer.parseInt(ageSearch);
                    petViewModel.searchByAge(age);
                } catch (NumberFormatException e) {
                    searchAgeBar.setError("Invalid age");
                }
            }
            // Type only
            else if (!TextUtils.isEmpty(typeSearch)) {
                petViewModel.searchByTypeOrBreed(typeSearch);
            }
            // State only
            else if (!TextUtils.isEmpty(stateSearch)) {
                petViewModel.searchByState(stateSearch);
            }
        });

    }}

