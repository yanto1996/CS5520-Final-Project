package com.example.cs_5520_final.controller;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.cs_5520_final.model.UserDb;
import com.example.cs_5520_final.view.HomeActivity;
import com.example.cs_5520_final.view.RegisterActivity;
import com.example.cs_5520_final.model.UserDao;
import com.example.cs_5520_final.model.UserEntity;

public class RegisterController {

    private final RegisterActivity registerActivity;

    public RegisterController(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    public void registerUser() {
        String firstName = registerActivity.getFirstName();
        String lastName = registerActivity.getLastName();
        String email = registerActivity.getEmail();
        String password = registerActivity.getPassword();
        String phoneNumber = registerActivity.getPhoneNumber();

        // Validate input fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || phoneNumber.isEmpty()) {
            registerActivity.showToast("Please fill out all fields");
            return;
        }

        if (!email.contains("@")) {
            registerActivity.showToast("Please enter a valid email address");
            return;
        }

        UserDao userDao = UserDb.getInstance(registerActivity).userDao();
        new Thread(() -> {
            UserEntity existingUser = userDao.getUserByEmail(email); // Fetch the user by email
            if (existingUser != null) {
                // Email already exists
                new Handler(Looper.getMainLooper()).post(() -> {
                    registerActivity.showToast("Email already exists. Please use a different email.");
                });
            } else {
                // Create UserEntity object to save in Room database
                UserEntity user = new UserEntity(firstName, lastName, email, password, phoneNumber);

                // Save user data in Room database
                new Thread(() -> {
                    userDao.insertUser(user);
                    new Handler(Looper.getMainLooper()).post(() -> {
                        registerActivity.showToast("User registered successfully!");
                        Intent home = new Intent(registerActivity, HomeActivity.class);
                        registerActivity.startActivity(home);
                        registerActivity.finishActivity();
                    });
                }).start();
            }
        }).start();
    }
}
