package com.example.cs_5520_final.view;

import static com.example.cs_5520_final.R.*;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.RegisterController;

/**
 * Register activity class that creates all the items in the register activity
 * On clicking the register button, users will be registered and taken to the home activity
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, phoneNumberEditText;
    private RegisterController registerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(id.lastName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(id.password);
        phoneNumberEditText = findViewById(id.phoneNumber);
        Button registerButton = findViewById(R.id.registerButton);

        registerController = new RegisterController(this);

        registerButton.setOnClickListener(v -> registerController.registerUser());
    }


    public String getFirstName() {
        return firstNameEditText.getText().toString().trim();
    }

    public String getLastName() {
        return lastNameEditText.getText().toString().trim();
    }

    public String getEmail() {
        return emailEditText.getText().toString().trim();
    }

    public String getPassword() {
        return passwordEditText.getText().toString().trim();
    }

    public String getPhoneNumber() {
        return phoneNumberEditText.getText().toString().trim();
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void finishActivity() {
        finish();
    }
}
