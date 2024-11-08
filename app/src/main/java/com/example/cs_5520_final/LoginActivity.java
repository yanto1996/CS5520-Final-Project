package com.example.cs_5520_final;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                performLogin();
            }
        });

        // Set up forgot password text click listener
        forgotPasswordText.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LoginActivity.this, "Please enter a valid email first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate sending a password reset link
            Toast.makeText(LoginActivity.this, "Password reset link sent to " + email, Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate email
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        try {
            // Simulate login success
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            handleLoginError(e);
        }
    }

    private void handleLoginError(Exception e) {
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        Toast.makeText(LoginActivity.this,
                "Login failed: " + e.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}
