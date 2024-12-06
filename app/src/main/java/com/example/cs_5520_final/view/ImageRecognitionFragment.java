package com.example.cs_5520_final.view;

import static android.content.ContentValues.TAG;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ImageController;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

import java.util.Arrays;
import java.util.Properties;


public class ImageRecognitionFragment extends Fragment {

    private ImageController imageController;
    private ImageView imageView;
    private TextView resultTextView;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(requireContext(), "Permission required to access media files", Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_recognition, container, false);

        // Request permission based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {  // Android 12 and below
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        // Initialize ImageController with actual API key
        String apiKey = loadApiKey();
        if (apiKey==null || apiKey.isEmpty() || apiKey.equals("REPLACE_WITH_API_KEY")){
            throw new IllegalStateException("API key is not set or invalid");
        }
        imageController = new ImageController(apiKey);

        // Initialize UI components
        imageView = view.findViewById(R.id.image_view);
        resultTextView = view.findViewById(R.id.result_text_view);
        Button uploadButton = view.findViewById(R.id.upload_button);

        // Upload button to open the image selector
        uploadButton.setOnClickListener(v -> {
            String[] filePaths = {"/sdcard/Download/images.jpeg", "/sdcard/Download/Mc CAT.jpg"};
            for (String path : filePaths) {
                refreshMediaStore(requireContext(), path);
            }
            openImageSelector();
        });


        return view;
    }

    private void refreshMediaStore(Context context, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Log.d(TAG, "Media store refreshed for: " + filePath);
        } else {
            Log.e(TAG, "File does not exist: " + filePath);
        }
    }


    private String loadApiKey() {
        try {
            String[] files = requireContext().getAssets().list("");
            Log.d(TAG, "Assets files: " + Arrays.toString(files));

            InputStream inputStream = requireContext().getAssets().open("apikey.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            String apiKey = properties.getProperty("API_KEY_IMG", "");
            Log.d(TAG, "Loaded API Key: " + apiKey);
            return apiKey;
        } catch (IOException e) {
            Log.e(TAG, "Failed to load API key from properties file", e);
            return "";
        }
    }


    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    Log.d(TAG, "pickImageLauncher: Image URI received - " + uri);
                    imageView.setImageURI(uri);

                    try {
                        File imageFile = createFileFromUri(uri);
                        Log.d(TAG, "pickImageLauncher: Image file created - " + imageFile.getAbsolutePath());
                        startImageScanning(imageFile);
                    } catch (IOException e) {
                        Log.e(TAG, "pickImageLauncher: Failed to create file from URI", e);
                        Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "pickImageLauncher: No image selected.");
                    Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }


    // Convert Uri to File so it can be processed by ImageController
    private File createFileFromUri(Uri uri) throws IOException {
        File file = new File(requireContext().getCacheDir(), "selected_image.jpg");
        Log.d(TAG, "createFileFromUri: Attempting to create file from URI.");

        InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Failed to open input stream for the selected image.");
        }

        try (inputStream; FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        Log.d(TAG, "createFileFromUri: File created successfully - " + file.getAbsolutePath());
        return file;
    }

    private void startImageScanning(File imageFile) {
        Log.d(TAG, "startImageScanning: Starting image scanning for file - " + imageFile.getAbsolutePath());
        imageController.identifyAnimal(imageFile, new ImageController.ImageScanCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "startImageScanning: Image recognition successful - " + result);
                requireActivity().runOnUiThread(() -> resultTextView.setText(result));
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "startImageScanning: Image recognition failed", e);
                requireActivity().runOnUiThread(() -> {
                    Log.e("Error", e.getMessage()); // Corrected placement of the semicolon and method
                });
            }
        });
    }

}
