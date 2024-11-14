package com.example.cs_5520_final.controller;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatAssistant {

    public static String chatGPT(String apiKey, String model, String prompt) {
        try {
            System.out.println("Starting chatGPT method...");

            // 构造 URL 和连接
            String urlString = "https://api.openai.com/v1/chat/completions";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            System.out.println("Setting up system message...");

            // 手动构建 JSON 字符串
            String requestBody = "{"
                    + "\"model\": \"" + model + "\", "
                    + "\"messages\": ["
                    + "  {\"role\": \"system\", \"content\": \"You are an enthusiastic and friendly virtual pet adoption assistant. "
                    + "Provide cheerful, informative responses to questions about different pet types or pet adoption tips. "
                    + "Always encourage users to select a pet that matches their lifestyle and commitment level.\"},"
                    + "  {\"role\": \"user\", \"content\": \"" + prompt + "\"}"
                    + "],"
                    + "\"temperature\": 1,"
                    + "\"max_tokens\": 2048,"
                    + "\"top_p\": 1,"
                    + "\"frequency_penalty\": 0.13,"
                    + "\"presence_penalty\": 0.11"
                    + "}";

            System.out.println("Request body: " + requestBody);

            // 发送请求
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes("utf-8"));
            }
            System.out.println("Request sent successfully.");

            // 读取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line.trim());
                    }
                    String parsedResponse = parseResponse(response.toString());

//                    System.out.println("Response received: " + response.toString());
//                    return extractMessageFromJSONResponse(response.toString());
//                    return response.toString();
                    return parsedResponse.toString();
                }
            } else {
                System.err.println("Error: Server returned response code " + responseCode);
                return "Error: Server returned response code " + responseCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
    private static String parseResponse(String rawResponse) {
        try {
            // 将原始响应解析为 JSON 对象
            JSONObject jsonResponse = new JSONObject(rawResponse);

            // 检查是否包含 "choices" 数组
            if (!jsonResponse.has("choices")) {
                return "Unable to parse response: 'choices' not found.";
            }

            // 获取 "choices" 数组的第一个元素
            JSONArray choicesArray = jsonResponse.getJSONArray("choices");
            if (choicesArray.length() == 0) {
                return "Unable to parse response: 'choices' is empty.";
            }

            // 获取 "message" 对象中的 "content" 字段
            JSONObject firstChoice = choicesArray.getJSONObject(0);
            JSONObject messageObject = firstChoice.getJSONObject("message");

            // 检查 "content" 字段是否存在
            if (!messageObject.has("content")) {
                return "Unable to parse response: 'content' not found.";
            }

            // 提取并返回 "content" 的内容
            String content = messageObject.getString("content");
            return content.trim(); // 去掉首尾多余的空格
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while parsing response.";
        }
    }
    public static String extractMessageFromJSONResponse(String response) {
        try {
            // 验证 "content" 字段是否存在
            if (response == null || !response.contains("\"content\":\"")) {
                System.err.println("Response does not contain 'content' field.");
                return "Response parsing error.";
            }

            // 提取内容的开始和结束位置
            int start = response.indexOf("\"content\":\"") + 10;
            int end = response.indexOf("\"", start);

            if (start > 9 && end > start) { // 确保 start 和 end 都合法
                return response.substring(start, end);
            } else {
                System.err.println("Failed to extract message from response: invalid indexes.");
                return "Response parsing error.";
            }
        } catch (Exception e) {
            System.err.println("Exception while parsing response: " + e.getMessage());
            return "Response parsing error.";
        }
    }


    public static String getApiKeyFromAssets() {
        Properties properties = new Properties();
        String apiKey = null;

        try (InputStream inputStream = new FileInputStream("/Users/melaniey/Github/CS5520-Final-Project/app/src/main/assets/apikey.properties")) {
            properties.load(inputStream);
            apiKey = properties.getProperty("API_KEY");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (apiKey == null) {
            System.err.println("API Key not found in apikey.properties.");
        }

        return apiKey;
    }



    public static void main(String[] args) {
        String model = "gpt-3.5-turbo";
        String prompt = "I'm thinking of adopting a dog, but I live in an apartment. Any advice?";
        String apiKey = getApiKeyFromAssets(); // 从文件读取 API Key
        System.out.println("Loaded API Key: " + apiKey);

        if (apiKey != null) {
            System.out.println(chatGPT(apiKey, model, prompt));
        } else {
            System.err.println("Cannot proceed without a valid API Key.");
        }
    }
}