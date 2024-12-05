package com.example.cs_5520_final.controller;

import com.example.cs_5520_final.model.ImageApiHandler;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class ImageController {

    private final ImageApiHandler imageApiHandler;

    // Constructor initializes the controller with the API handler
    public ImageController(String apiKey) {
        this.imageApiHandler = new ImageApiHandler(apiKey);
    }

    // Method to initiate image scanning and notify View through callback
    public void identifyAnimal(File imageFile, ImageScanCallback callback) {
        // Start a background thread to avoid blocking the UI
        new Thread(() -> {
            try {
                // Use ImageApiHandler to scan the image
                String result = imageApiHandler.scanImage(imageFile);

                // Pass the result to the callback on success
                callback.onSuccess(result);
            } catch (IOException e) {
                // Pass the error to the callback on failure
                callback.onFailure(e);
            }
        }).start();
    }

    // Callback interface to handle asynchronous responses
    public interface ImageScanCallback {
        void onSuccess(String result);  // Called when the image scan is successful
        void onFailure(Exception e);    // Called when there's an error during scanning
    }
}
