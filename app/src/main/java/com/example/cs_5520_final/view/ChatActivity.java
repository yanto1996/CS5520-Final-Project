package com.example.cs_5520_final.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

    private void sendChatRequest(String prompt) {
        executorService.execute(() -> {

            String response = chatAssistant.chatGPT("sk-xxxxxxxxxx", "gpt-3.5-turbo", prompt);


            mainHandler.post(() -> chatResponse.setText(response));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executorService.shutdown();
    }
}