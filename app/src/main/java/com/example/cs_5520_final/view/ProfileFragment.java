package com.example.cs_5520_final.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.cs_5520_final.R;
import com.example.cs_5520_final.model.UserDao;
import com.example.cs_5520_final.model.UserDb;
import com.example.cs_5520_final.model.UserEntity;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private TextView profileName;
    private TextView profileEmail;
    private TextView profilePhone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilePhone = view.findViewById(R.id.profilePhone);

        // Retrieve email from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        userDetails(email, password);
    }

    private void userDetails(String email, String password){
        // Access UserDb instance with context
        UserDb userDb = UserDb.getInstance(requireContext());
        UserDao userDao = userDb.userDao();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            UserEntity user = userDao.getUserByEmailAndPassword(email, password);
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    profileName.setText(user.getFirstName() + " " + user.getLastName());
                    profileEmail.setText(user.getEmail());
                    profilePhone.setText(user.getPhoneNumber());
                });
            }
        });
    }
}
