package com.example.cs_5520_final.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cs_5520_final.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The main activity of the application, responsible for managing and displaying different fragments
 * through a BottomNavigationView. It allows the user to navigate between the home, image recognition,
 * and profile sections
 */
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Floating button
        FloatingActionButton fabChat = findViewById(R.id.fab_chat);
        fabChat.setOnClickListener(v -> {
            new ChatFragment().show(getSupportFragmentManager(), "ChatFragment");
        });


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
