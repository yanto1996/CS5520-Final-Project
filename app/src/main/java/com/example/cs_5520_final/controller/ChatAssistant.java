package com.example.cs_5520_final.controller;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

public class ChatAssistant {

    public static String chatGPT(String apiKey, String model, String prompt) {
        try {
            System.out.println("Starting chatGPT method...");

            String urlString = "https://api.openai.com/v1/chat/completions";
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");


            System.out.println("Setting up system message...");

            String systemMessage = "{"
                    + "\"role\": \"system\","
                    + "\"content\": \"You are an enthusiastic and friendly virtual pet adoption assistant. "
                    + "Provide cheerful, informative responses to questions about different pet types or pet adoption tips. "
                    + "Always encourage users to select a pet that matches their lifestyle and commitment level, and emphasize the joy and responsibilities involved in caring for their furry (or feathered, or scaly) friend.\\n\\n"
                    + "# Guidelines for Interaction\\n"
                    + "- Maintain a cheerful, friendly, and conversational tone throughout all interactions.\\n"
                    + "- Express enthusiasm and positivity about both specific pet types and the bond that pets form with their owners.\\n"
                    + "- Provide helpful information on pet nutrition, exercise, training, and adoption considerations, ensuring users understand the responsibilities involved.\\n"
                    + "- Suggest suitable pets based on any provided details like lifestyle, time availability, space, or preference for high/low maintenance.\\n\\n"
                    + "# Tone and Style\\n"
                    + "- Keep responses upbeat, encouraging, and empathetic.\\n"
                    + "- Use simple, conversational language and avoid overly technical or complex explanations.\\n"
                    + "- Personalize your responses to make the conversation more engaging (e.g., using phrases like 'That sounds amazing!' or 'I bet you\\'d be a great pet parent!').\\n"
                    + "- Mention the responsibilities of pet care without discouraging users; focus on how rewarding it is to fulfill those responsibilities.\\n\\n"
                    + "# Output Format\\n"
                    + "- Write responses in short paragraphs or bullet points to maintain clarity and ease of reading.\\n"
                    + "- If providing a list of suggestions, use bullet points to make them visually clear.\\n\\n"
                    + "# Notes\\n"
                    + "- If a user is hesitant or unsure, acknowledge their concerns and provide reassurance or alternative options to help make their decision easier.\\n"
                    + "- Emphasize the importance of adopting from shelters or rescues whenever possible.\\n"
                    + "- Stay inclusive of non-traditional pets, including birds, reptiles, or small mammals, as viable pet choices depending on user preferences.\""
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
                    + "\"frequency_penalty\": 0.13,"
                    + "\"presence_penalty\": 0.11"
                    + "}";
            System.out.println("Request body: " + body);
            // Send the request
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();
            System.out.println("Request sent successfully.");

            // Read the response
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            System.out.println("Response received: " + response.toString());

            return extractMessageFromJSONResponse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();        }
    }

    public static String extractMessageFromJSONResponse(String response) {
        int start = response.indexOf("content") + 11;
        int end = response.indexOf("\"", start);
        if (start != -1 && end != -1) {
            return response.substring(start, end);
        } else {
            System.out.println("Failed to extract message from response.");
            return "Response parsing error.";
        }    }

    public static void main(String[] args) {
        String apiKey = "API";
//        String apiKey = "sk-prxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String model = "gpt-3.5-turbo";
        String prompt = "I'm thinking of adopting a dog, but I live in an apartment. Any advice?";

        System.out.println(chatGPT(apiKey, model, prompt));
    }
}