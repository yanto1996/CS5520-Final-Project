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

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";  // Endpoint for ChatGPT API
    private final String apiKey;
    private final OkHttpClient client;

    // Constructor: initializes API key and sets up client
    public ImageApiHandler(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key is missing. Set the API key.");
        }
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
        RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageFile.getName(), fileBody)
                .addFormDataPart("model", "gpt-4-turbo")  // Specify the model
                .addFormDataPart("prompt", "Analyze this image and identify the animal; " +
                        "provide information such as natural habitat, food sources, whether it is suitable as pet " +
                        "and other things you think are important.")
                .build();

        // Build the request with the Authorization header
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)  // Use the API key here for authentication
                .build();

        // Execute the request and capture the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String errorBody = response.body().string();
                throw new IOException("API error: " + errorBody);
            }
            return response.body() != null ? response.body().string() : "No response";
        }
    }
}
