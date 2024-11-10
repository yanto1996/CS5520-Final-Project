package com.example.cs_5520_final.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PetViewModel extends AndroidViewModel {
    private MutableLiveData<List<PetModel>> petListLiveData;
    private PetDao petDao;

    public PetViewModel(Application application) {
        super(application);
        petListLiveData = new MutableLiveData<>();
        petDao = new PetDao(application);
    }

    // Method to fetch the pets from the database and set it to LiveData
    public void fetchPets(int limit) {
        new Thread(() -> {
            List<PetModel> pets = petDao.getTenPets(limit);
            petListLiveData.postValue(pets);
        }).start();
    }

    public LiveData<List<PetModel>> getPets() {
        return petListLiveData;
    }
}
