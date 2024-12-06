package com.example.cs_5520_final.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

/**
 * Pet viewmodel class that helps manage live pet data
 * Can be identified as the service method for the DAO
 * Calls the DAO to manage pet data from SQLite
 */
public class PetViewModel extends AndroidViewModel {
    private MutableLiveData<List<PetModel>> petListLiveData;
    private PetDao petDao;

    /**
     * constructs a pet viewmodel object
     * @param application application context, environment to access specific data
     */
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

    /**
     * Creates a new pet model with specific search parameters
     * @param search user search inputs
     * sets the live data of the view to be new pet model with search query
     */
    public void searchByTypeOrBreed(String search){
        List<PetModel> pets = petDao.getPetsByTypeOrBreed(search);
        petListLiveData.setValue(pets);
    }

    public void searchByState(String search){
        List<PetModel> pets = petDao.getPetsByState(search);
        petListLiveData.setValue(pets);
    }

    public void searchByTypeStateAndAge(String typeOrBreed, String state, int age) {
        List<PetModel> pets = petDao.getPetsByTypeBreedStateAndAge(typeOrBreed, state, age);
        petListLiveData.setValue(pets);
    }

    public void searchByTypeAndState(String type, String state){
        List<PetModel> pets = petDao.getPetsByTypeOrBreedAndState(type, state);
        petListLiveData.setValue(pets);
    }

    public void searchByStateAndAge(String state, int age) {
        List<PetModel> pets = petDao.getPetsByStateAndAge(state, age);
        petListLiveData.setValue(pets);
    }

    public void searchByTypeOrBreedAndAge(String type, int age){
        List <PetModel> pets = petDao.getPetsByTypeOrBreedAndAge(type, age);
        petListLiveData.setValue(pets);

    }

    public void searchByAge(int age) {
        List<PetModel> pets = petDao.getPetsByAge(age);
        petListLiveData.setValue(pets);
    }

    public LiveData<List<PetModel>> getPets() {
        return petListLiveData;
    }
}
