package com.example.cs_5520_final.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
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

        // 初始化视图
        userInput = findViewById(R.id.user_input);
        Button sendButton = findViewById(R.id.send_button);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);

        // 初始化 RecyclerView 和适配器
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // 初始化线程池和主线程 Handler
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // 添加 Bot 的初始引导消息
        initializeBotMessage();

        // 发送按钮点击事件
        sendButton.setOnClickListener(view -> {
            String prompt = userInput.getText().toString().trim();
            if (!prompt.isEmpty()) {
                addMessage("You: " + prompt); // 添加用户输入到聊天记录
                sendChatRequest(prompt); // 向 AI 发送请求
                userInput.setText(""); // 清空输入框
            }
        });
    }

    private void initializeBotMessage() {
        addMessage("Assistant: 你好，我是你的AI助手，你可以问我任何问题，或者从以下问题入手：");
        addMessage("Assistant: 1️⃣ What is the average age of a pickleball enthusiast?");
        addMessage("Assistant: 2️⃣ How are points scored in pickleball?");
        addMessage("Assistant: 3️⃣ Where do you play pickleball?");
    }

    private void addMessage(String message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1); // 滚动到最新消息
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
