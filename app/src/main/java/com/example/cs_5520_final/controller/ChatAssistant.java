package com.example.cs_5520_final.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class ChatAssistant {

    public static String chatGPT(String apiKey, String model, String prompt) {
        try {
            String urlString = "https://api.openai.com/v1/chat/completions";
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // Set up the system message
            String systemMessage = "{"
                    + "\"role\": \"system\","
                    + "\"content\": \"You are an enthusiastic and friendly virtual pet adoption assistant. "
                    + "Provide cheerful, informative responses to questions about different pet types or pet adoption tips. "
                    + "Always encourage users to select a pet that matches their lifestyle and commitment level, and emphasize the joy and responsibilities involved in caring for their furry (or feathered, or scaly) friend. "
                    + "Maintain a cheerful, friendly, and conversational tone throughout all interactions.\""
                    + "}";

            // User message
            String userMessage = "{"
                    + "\"role\": \"user\","
                    + "\"content\": \"" + prompt + "\""
                    + "}";

            // JSON body including both system and user messages
            String body = "{"
                    + "\"model\": \"" + model + "\", "
                    + "\"messages\": [" + systemMessage + "," + userMessage + "],"
                    + "\"temperature\": 1,"
                    + "\"max_tokens\": 2048,"
                    + "\"top_p\": 1,"
                    + "\"frequency_penalty\": 0,"
                    + "\"presence_penalty\": 0"
                    + "}";

            // Send the request
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return extractMessageFromJSONResponse(response.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        return response.substring(start, end);
    }

    public static void main(String[] args) {
        String apiKey = "API_KEY";
//        String apiKey = "sk-prxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String model = "gpt-3.5-turbo";
        String prompt = "I'm thinking of adopting a dog, but I live in an apartment. Any advice?";

        System.out.println(chatGPT(apiKey, model, prompt));
    }
}