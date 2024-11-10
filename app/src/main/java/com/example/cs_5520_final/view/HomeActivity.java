package com.example.cs_5520_final.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cs_5520_final.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNav);

        // Default fragment when the activity starts
        if (savedInstanceState == null) {
            selectedFragment = new HomeFragment();
            loadFragment(selectedFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (bottomNavigationView.getSelectedItemId() == item.getItemId()){
                return false;
            }

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_image_rec:
                    selectedFragment = new ImageRecognitionFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                default:
                    selectedFragment = null;
            }
            return loadFragment(selectedFragment);
        });
    }

    // Helper method to load the fragment dynamically
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
