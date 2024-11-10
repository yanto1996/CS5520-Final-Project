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
        searchType(view);
    }

    private void recyclerViewSetUp(View view){
        RecyclerView petRecyclerView = view.findViewById(R.id.petRecyclerView);
        petAdapter = new PetAdapter();
        petRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        petRecyclerView.setAdapter(petAdapter);
        petViewModel = new ViewModelProvider(this).get(PetViewModel.class);
        petViewModel.getPets().observe(getViewLifecycleOwner(), pets -> {
            // Update the adapter's data when the data changes
            petAdapter.setPets(pets);
        });
    }

    private void searchType(View view){
        EditText searchbar = view.findViewById(R.id.searchBar1);
        Button searchButton = view.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String search = searchbar.getText().toString().trim();

            // if search bar is empty do nothing
            if (search.isEmpty()){
                return;
            }

            petViewModel.searchByTypeOrBreed(search);
        });
    }
}
