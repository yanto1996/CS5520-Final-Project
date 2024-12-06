package com.example.cs_5520_final.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.LoginController;

public class LoginActivity extends AppCompatActivity implements LoginController.LoginCallback {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        TextView registration = findViewById(R.id.register);

        loginController = new LoginController(this, this);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (loginController.validateInput(email, password)) {
                loginController.performLogin(email, password);
            }
        });

        registration.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLoginSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onLoginError(String errorMessage) {
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        });
    }
}
