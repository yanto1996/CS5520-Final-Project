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

    // Send thread to get 10 random pets asynchronously
    public void fetchPets(int limit) {
        new Thread(() -> {
            List<PetModel> pets = petDao.getTenPets(limit);
            petListLiveData.postValue(pets);
        }).start();
    }

    public void searchByTypeOrBreed(String searchType){
        List<PetModel> pets = petDao.getPetsByTypeOrBreed(searchType);
        petListLiveData.setValue(pets);
    }

    public LiveData<List<PetModel>> getPets() {
        return petListLiveData;
    }
}
