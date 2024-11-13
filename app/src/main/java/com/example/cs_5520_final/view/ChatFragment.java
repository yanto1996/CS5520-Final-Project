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
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ChatAssistant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatFragment extends DialogFragment {

    private TextView chatResponse;
    private EditText userInput;
    private final ChatAssistant chatAssistant = new ChatAssistant();
    private ExecutorService executorService;
    private Handler mainHandler;
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.83);
//            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = (int) (getResources().getDisplayMetrics().widthPixels * 0.53);


            getDialog().getWindow().setLayout(width, height);
            getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.END); // 右下角对齐
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); // 透明背景
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chat, container, false);

        chatResponse = view.findViewById(R.id.chat_response);
        userInput = view.findViewById(R.id.user_input);
        Button sendButton = view.findViewById(R.id.send_button);
        ImageButton closeButton = view.findViewById(R.id.close_button);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        sendButton.setOnClickListener(v -> {
            String prompt = userInput.getText().toString();
            if (!prompt.isEmpty()) {
                sendChatRequest(prompt);
            }
        });

        closeButton.setOnClickListener(v -> dismiss());

        return view;
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

            mainHandler.post(() -> chatResponse.setText(response));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
