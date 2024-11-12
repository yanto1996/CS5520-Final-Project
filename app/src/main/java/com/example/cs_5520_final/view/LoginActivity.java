package com.example.cs_5520_final.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.LoginController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity implements LoginController.LoginCallback {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);
        TextView registration = findViewById(R.id.register);
        FloatingActionButton fabChat = findViewById(R.id.fab_chat);  // Add this line

        // Initialize Controller
        loginController = new LoginController(this);

        // Set up FloatingActionButton click listener to open ChatActivity
        fabChat.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
            startActivity(intent);
        });

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (loginController.validateInput(email, password)) {
                progressBar.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                loginController.performLogin(email, password);
            }
        });

        // Set up forgot password text click listener
        forgotPasswordText.setOnClickListener(v -> {
            // Start PasswordResetActivity when "Forgot Password?" is clicked
            Intent intent = new Intent(LoginActivity.this, PasswordResetActivity.class);
            startActivity(intent);
        });

        registration.setOnClickListener(v ->{
            Intent intent = new Intent (LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLoginSuccess() {
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

        // Navigate to HomeActivity on successful login
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity
    }

    @Override
    public void onLoginError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }
}
