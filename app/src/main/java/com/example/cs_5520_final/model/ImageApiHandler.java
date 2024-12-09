package com.example.cs_5520_final.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ImageApiHandler {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";  // ChatGPT endpoint
    private final String apiKey;
    private final OkHttpClient client;

    public interface ApiCallback {
        void onSuccess(String result);
        void onFailure(Exception E);
    }


    // Constructor: initializes API key and sets up client
    public ImageApiHandler(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("API Key is missing.");
        }
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // Method to scan an image and return the API response
    public void scanImage(File imageFile, ApiCallback callback) {
        try {
            if (imageFile == null || !imageFile.exists()) {
                throw new IllegalArgumentException("Image file must not be null or non-existent");
            }

            // Read the image file and convert to Base64
            String base64Image = encodeFileToBase64(imageFile);
            Log.e("Created Base64Image: ", base64Image);

            // Create the JSON body for the API request
            JSONObject requestBodyJson = new JSONObject();
            JSONArray messagesArray = new JSONArray();

            // Add customized the prompt to the JSON body
            JSONObject textMessage = new JSONObject();
            textMessage.put("type", "text");
            textMessage.put("text", "Analyze this image and identify the animal. Provide details such as habitat, food sources, suitability as a pet, and any other relevant information.");

            // Add scanned image as a Base64-encoded string
            JSONObject imageMessage = new JSONObject();
            imageMessage.put("type", "image_url");
            imageMessage.put("image_url", new JSONObject().put("url", "data:image/jpeg;base64," + base64Image));

            // A "user" message containing both the text and the image
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", new JSONArray().put(textMessage).put(imageMessage));
            messagesArray.put(userMessage);

            // Final request body
            requestBodyJson.put("model", "gpt-4-turbo");
            requestBodyJson.put("messages", messagesArray);

            // Build the request
            RequestBody requestBody = RequestBody.create(requestBodyJson.toString(), MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "API request failed", e);
                    callback.onFailure(e);
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String responseBody = response.body().string();
                        Log.d(TAG, "API response received: " + responseBody);
                        callback.onSuccess(responseBody);
                    } else {
                        String errorBody = response.body() != null ? response.body().string() : "No response body";
                        callback.onFailure(new IOException("API error: " + response.code() + " - " + errorBody));
                    }
                }
            });
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }

/* Method to encode an image file to Base64 String
 */
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
