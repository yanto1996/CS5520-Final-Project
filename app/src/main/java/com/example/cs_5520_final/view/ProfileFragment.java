package com.example.cs_5520_final.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private ImageView profileImage;
    private TextView profileName;
    private TextView profileUsername;
    private TextView profileEmail;
    private TextView profilePhone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profileImage);
        profileName = view.findViewById(R.id.profileName);
        profileUsername = view.findViewById(R.id.profileUsername);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilePhone = view.findViewById(R.id.profilePhone);

        // Access UserDb instance with context
        UserDb userDb = UserDb.getInstance(requireContext());
        UserDao userDao = userDb.userDao();

        // Fetch user data in background thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            UserEntity user = userDao.getUser(); // Assuming `getUser()` retrieves a user
            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    profileName.setText(user.getFirstName() + " " + user.getLastName());
//                    profileUsername.setText(user.getUsername());
                    profileEmail.setText(user.getEmail());
                    profilePhone.setText(user.getPhoneNumber());
                });
            }
        });
    }
}
