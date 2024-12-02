package com.example.cs_5520_final.controller;

import android.content.Context;
import android.util.Patterns;

import com.example.cs_5520_final.model.UserDao;
import com.example.cs_5520_final.model.UserDb;
import com.example.cs_5520_final.model.UserEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController {

    private final Context context;
    private final LoginCallback callback;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginError(String errorMessage);
    }

    public LoginController(Context context, LoginCallback callback) {
        this.context = context.getApplicationContext(); // Ensure application context
        this.callback = callback;
    }

    public boolean validateInput(String email, String password) {
        if (email.isEmpty()) {
            callback.onLoginError("Email is required");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            callback.onLoginError("Please enter a valid email");
            return false;
        }

        if (password.isEmpty()) {
            callback.onLoginError("Password is required");
            return false;
        }

        return true;
    }

    public void performLogin(String email, String password) {
        if (!validateInput(email, password)) {
            return;
        }

        UserDb userDb = UserDb.getInstance(context); // Use proper context
        UserDao userDao = userDb.userDao();

        executor.execute(() -> {
            UserEntity user = userDao.getUserByEmailAndPassword(email, password);
            if (user != null) {
                callback.onLoginSuccess();
            } else {
                callback.onLoginError("Invalid email or password");
            }
        });
    }
}
