package com.example.cs_5520_final.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.example.cs_5520_final.model.UserDb;
import com.example.cs_5520_final.view.HomeActivity;
import com.example.cs_5520_final.view.RegisterActivity;
import com.example.cs_5520_final.model.UserDao;
import com.example.cs_5520_final.model.UserEntity;

/**
 * Register Controller class that handles all back end logic for registration
 * Handles all validation logic such as checking for email duplication
 * Saves the user information into Room Database for validation during login
 */

public class RegisterController {


    private final RegisterActivity registerActivity;

    /**
     * constructor for register controller
     * @param registerActivity register activity that takes in the user inputs
     */

    public RegisterController(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    /**
     * Register user method that validates user inputs and saves user information into room
     * Method also stores user information into sharedPreference to query in profile page
     * Takes user to the home fragment upon successful registration
     */
    public void registerUser() {
        String firstName = registerActivity.getFirstName();
        String lastName = registerActivity.getLastName();
        String email = registerActivity.getEmail();
        String password = registerActivity.getPassword();
        String phoneNumber = registerActivity.getPhoneNumber();

        // Validate user inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || phoneNumber.isEmpty()) {
            registerActivity.showToast("Please fill out all fields");
            return;
        }

        if (!email.contains("@")) {
            registerActivity.showToast("Please enter a valid email address");
            return;
        }

        if (password.length() < 3){
            registerActivity.showToast("Password needs to be at least 3 characters long");
            return;
        }

        // Instantiate Room DAO to store user information
        UserDao userDao = UserDb.getInstance(registerActivity).userDao();
        // spawn a BG thread to perform DB operation
        new Thread(() -> {
            UserEntity existingUser = userDao.getUserByEmail(email);
            if (existingUser != null) {
                // Email already exists
                new Handler(Looper.getMainLooper()).post(() -> {
                    registerActivity.showToast("Email already exists. Please use a different email.");
                });
            } else {
                UserEntity user = new UserEntity(firstName, lastName, email, password, phoneNumber);

                // Save user data in Room database
                userDao.insertUser(user);

                // Store email and password in SharedPreferences
                SharedPreferences sharedPreferences = registerActivity.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();

                // Navigate to HomeActivity
                new Handler(Looper.getMainLooper()).post(() -> {
                    registerActivity.showToast("User registered successfully!");
                    Intent home = new Intent(registerActivity, HomeActivity.class);
                    registerActivity.startActivity(home);
                    registerActivity.finishActivity();
                });
            }
        }).start();
    }
}
