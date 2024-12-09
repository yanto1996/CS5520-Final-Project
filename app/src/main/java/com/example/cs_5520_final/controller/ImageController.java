package com.example.cs_5520_final.controller;

import com.example.cs_5520_final.model.ImageApiHandler;

import java.io.File;

public class ImageController {

    private final ImageApiHandler imageApiHandler;

    // Constructor
    public ImageController(String apiKey) {
        this.imageApiHandler = new ImageApiHandler(apiKey);
    }

    // Identify the animal in the image
    public void identifyAnimal(File imageFile, ImageScanCallback callback) {
        imageApiHandler.scanImage(imageFile, new ImageApiHandler.ApiCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
            }
        });
    }

    // Interface for handling responses from the API
    public interface ImageScanCallback {
        void onSuccess(String result);
        void onFailure(Exception e);
    }
}
