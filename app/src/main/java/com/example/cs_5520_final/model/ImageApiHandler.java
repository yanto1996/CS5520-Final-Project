package com.example.cs_5520_final.model;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ImageApiHandler {

    private static final String API_URL = "https://api.openai.com/v1/images"; // Replace with actual endpoint if needed
    private String apiKey;
    private OkHttpClient client;

    // Constructor that takes the API key as a parameter
    public ImageApiHandler(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // Method to scan an image and return the API response
    public String scanImage(File imageFile) throws IOException {
        if (imageFile == null || !imageFile.exists()) {
            throw new IllegalArgumentException("Image file must not be null or non-existent");
        }

        // Prepare the image file for sending
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(), fileBody)
                .build();

        // Build the request with the Authorization header
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)  // Use the API key here
                .build();

        // Execute the request and capture the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();  // Return the response body as a string
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }
}
