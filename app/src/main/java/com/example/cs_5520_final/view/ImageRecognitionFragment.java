package com.example.cs_5520_final.view;

import static android.content.ContentValues.TAG;

import com.example.cs_5520_final.R;
import com.example.cs_5520_final.controller.ImageController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Properties;


public class ImageRecognitionFragment extends Fragment {

    private ImageController imageController;
    private ImageView imageView;
    private TextView resultTextView;
    private boolean isApiLoading = false;

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

        // Initialize UI components
        imageView = view.findViewById(R.id.image_view);
        resultTextView = view.findViewById(R.id.result_text_view);
        resultTextView.setMovementMethod(new ScrollingMovementMethod());

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

        // Debugging for ScrollView and TextView heights
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        scrollView.post(() -> {
            Log.d("ScrollViewHeight", "Height: " + scrollView.getHeight());
            Log.d("TextViewHeight", "Height: " + resultTextView.getHeight());
        });


        FloatingActionButton uploadButton = view.findViewById(R.id.upload_button);

        // Upload button to open the image selector and initialize the API key
        uploadButton.setOnClickListener(v -> {
            String apiKey = loadApiKey();
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("REPLACE_WITH_API_KEY")) {
                Toast.makeText(requireContext(), "API key is not set or invalid", Toast.LENGTH_LONG).show();
                return;
            }

            // Initialize the ImageController with a valid API key
            imageController = new ImageController(apiKey);

            openImageSelector();
        });

        return view;
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

    private void openImageSelector() {
        // Refresh the media store for all image files in the folder
        refreshMediaStoreForFolder("/sdcard/Download");

        // Open the image selector
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
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


    private void refreshMediaStoreForFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg")
                            || name.toLowerCase().endsWith(".jpeg")
                            || name.toLowerCase().endsWith(".png"));

            if (files != null) {
                for (File file : files) {
                    refreshMediaStore(requireContext(), file.getAbsolutePath());
                }
            } else {
                Log.e(TAG, "No files found in folder: " + folderPath);
            }
        } else {
            Log.e(TAG, "Invalid folder path: " + folderPath);
        }
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
            Log.e(TAG, "File not found for refreshing media store: " + filePath);
        }
    }


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
        isApiLoading = true;
        updateTextView();

        imageController.identifyAnimal(imageFile, new ImageController.ImageScanCallback() {
            @Override
            public void onSuccess(String result) {
                isApiLoading = false;
                Log.d(TAG, "Image recognition successful: " + result);
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
                    String cleanContent = content.replaceAll("\\*\\*","");

                    // Update the UI with the API response
                    requireActivity().runOnUiThread(() -> resultTextView.setText(cleanContent));
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing API response", e);
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error parsing API response", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Exception e) {
                isApiLoading = false;
                Log.e(TAG, "Image recognition failed", e);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void updateTextView() {
        if (isApiLoading) {
            resultTextView.setText("Loading... Please wait!");
        } else {
            resultTextView.setText("Upload a photo here to identify the animal and learn more about it!");
        }
    }


}