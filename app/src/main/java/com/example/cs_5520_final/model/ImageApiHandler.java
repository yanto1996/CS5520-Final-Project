package com.example.cs_5520_final.model;

import android.util.Log;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ImageApiHandler {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";  // ChatGPT endpoint
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

        // Read the image file and convert to Base64 manually for API 24 compatibility
        String base64Image = encodeFileToBase64(imageFile);
        Log.e("Base64Image", base64Image);

        // Create the JSON body for the API request
        JSONObject requestBodyJson = new JSONObject();
        try {
            JSONArray messagesArray = new JSONArray();

            // Add the text part of the prompt
            JSONObject textMessage = new JSONObject();
            textMessage.put("type", "text");
            textMessage.put("text", "Analyze this image and identify the animal. Provide details such as habitat, food sources, suitability as a pet, and any other relevant information.");

            // Add the image part as a Base64-encoded string
            JSONObject imageMessage = new JSONObject();
            imageMessage.put("type", "image_url");
            imageMessage.put("image_url", new JSONObject().put("url", "data:image/jpeg;base64," + base64Image));

            // Create a "user" message containing both the text and the image
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", new JSONArray().put(textMessage).put(imageMessage));

            // Add the user message to the messages array
            messagesArray.put(userMessage);

            // Final request body
            requestBodyJson.put("model", "gpt-4-turbo");
            requestBodyJson.put("messages", messagesArray);

        } catch (JSONException e) {
            throw new IOException("Error building JSON request body: " + e.getMessage(), e);
        }


        // Build the request
        RequestBody requestBody = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and capture the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();  // Return the successful response
            } else {
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                throw new IOException("API error: " + response.code() + " - " + response.message() + " - " + errorBody);
            }
        }
    }

    // Method to encode a file to Base64 string (compatible with API 24)
    private String encodeFileToBase64(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = fileInputStream.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Failed to read file for Base64 encoding.");
            }
            return android.util.Base64.encodeToString(buffer, android.util.Base64.NO_WRAP);
        }
    }
}
