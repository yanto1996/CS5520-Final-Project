package com.example.cs_5520_final.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ChatAssistant;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Chat fragment that handles the implementation of the chatbot interface where users can send messages and receive AI generated responses
 */
public class ChatFragment extends DialogFragment {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<String> messages;
    private EditText userInput;
    private final ChatAssistant chatAssistant = new ChatAssistant();
    private ExecutorService executorService;
    private Handler mainHandler;
    private boolean isInitialMessageAdded = false;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.83);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);

            getDialog().getWindow().setLayout(width, height);
            getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.END);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);

        ImageButton closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());

        userInput = view.findViewById(R.id.user_input);
        Button sendButton = view.findViewById(R.id.send_button);

        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        if (savedInstanceState != null) {
            messages = savedInstanceState.getStringArrayList("messages");
            isInitialMessageAdded = true;
        } else {
            messages = new ArrayList<>();
        }

        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        if (savedInstanceState == null) {
            initializeBotMessage();
            isInitialMessageAdded = true;
        }

        sendButton.setOnClickListener(v -> {
            String prompt = userInput.getText().toString();
            if (!prompt.isEmpty()) {
                addMessage("You: " + prompt);
                sendChatRequest(prompt);
                userInput.setText("");
            }
        });

        return view;
    }

    private void initializeBotMessage() {

        addMessage("Assistant: Hi there! I'm your AI assistant. Feel free to ask me anything, or you can start with one of these questions! \uD83D\uDE0A");
        addMessage("Assistant: 1️⃣ I live in a small apartment. What kind of dog would suit my lifestyle?");
        addMessage("Assistant: 2️⃣ I have young children at home. Are there any dog breeds that are great with kids?");
        addMessage("Assistant: 3️⃣ I work long hours. What kind of pet would be low-maintenance and happy alone?");

    }

    private void sendPresetQuestion(String question) {
        addMessage("You: " + question);
        sendChatRequest(question);
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
            String apiKey = getApiKeyFromAssets(requireContext());
            String response = chatAssistant.chatGPT(apiKey, "gpt-3.5-turbo", prompt);

            mainHandler.post(() -> {
                addMessage("Assistant: " + response);
            });
        });
    }

    private void addMessage(String message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("messages", new ArrayList<>(messages));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
