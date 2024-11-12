package com.example.cs_5520_final.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class ChatAssistant {

    public static String chatGPT(String prompt) {
        String urlString = "https://api.openai.com/v1/chat/completions";
        String apiKey = "APIKEY";
        String model = "gpt-3.5-turbo";

        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            String body = "{"
                    + "\"model\": \"" + model + "\", "
                    + "\"messages\": ["
                    + "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}"
                    + "]}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

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
        System.out.println(chatGPT("hello. Can you tell me something about a Husky dog?"));
    }
}
