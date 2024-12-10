package com.example.cs_5520_final.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ChatAssistant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<String> messages;
    private EditText userInput;
    private final ChatAssistant chatAssistant = new ChatAssistant();
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userInput = findViewById(R.id.user_input);
        Button sendButton = findViewById(R.id.send_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        initializeBotMessage();

        sendButton.setOnClickListener(view -> {
            String prompt = userInput.getText().toString().trim();
            if (!prompt.isEmpty()) {
                addMessage("You: " + prompt);
                sendChatRequest(prompt);
                userInput.setText("");
            }
        });
    }

    private void initializeBotMessage() {
        addMessage("Assistant:  Hi there! I'm your AI assistant. Feel free to ask me anything, or you can start with one of these questions! \uD83D\uDE0A");
        addMessage("Assistant: 1️⃣ What is the average age of a pickleball enthusiast?");
        addMessage("Assistant: 2️⃣ How are points scored in pickleball?");
        addMessage("Assistant: 3️⃣ Where do you play pickleball?");
    }

    private void addMessage(String message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    private String getApiKeyFromAssets(Context context) {
        Properties properties = new Properties();
        String apiKey = null;

        try (InputStream inputStream = context.getAssets().open("apikey.properties")) {
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

    private void sendChatRequest(String prompt) {
        executorService.execute(() -> {
            String apiKey = getApiKeyFromAssets(this);
            String response = chatAssistant.chatGPT(apiKey, "gpt-3.5-turbo", prompt);

            mainHandler.post(() -> addMessage("Assistant: " + response)); // 添加 Bot 回复到聊天记录
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
