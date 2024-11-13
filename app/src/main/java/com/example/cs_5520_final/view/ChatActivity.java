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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ChatAssistant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    private TextView chatResponse;
    private EditText userInput;
    private final ChatAssistant chatAssistant = new ChatAssistant();
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatResponse = findViewById(R.id.chat_response);
        userInput = findViewById(R.id.user_input);
        Button sendButton = findViewById(R.id.send_button);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());




        sendButton.setOnClickListener(view -> {
            String prompt = userInput.getText().toString();
            sendChatRequest(prompt);
        });
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
            System.out.println("Loaded API Key: " + apiKey);
            String response = chatAssistant.chatGPT(apiKey, "gpt-3.5-turbo", prompt);


            mainHandler.post(() -> chatResponse.setText(response));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executorService.shutdown();
    }
}