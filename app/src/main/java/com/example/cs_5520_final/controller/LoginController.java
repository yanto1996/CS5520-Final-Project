package com.example.cs_5520_final.controller;

import android.util.Patterns;

public class LoginController {

    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginError(String errorMessage);
    }

    private final LoginCallback callback;

    public LoginController(LoginCallback callback) {
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

        if (password.length() < 3) {
            callback.onLoginError("Password must be at least 3 characters");
            return false;
        }

        return true;
    }

    public void performLogin(String email, String password) {
        // no validation yet
        callback.onLoginSuccess();
    }
}
