package com.example.cs_5520_final.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class DbHelper {

    private static final String TAG = "PetDatabaseHelper";
    private static final String DB_NAME = "petfinder.db";
    private static final String DB_PATH = "/data/data/com.example.cs_5520_final/databases/";
    private Context context;

    public DbHelper(Context context) {
        this.context = context;
        copyDatabase();
    }

    // Method to copy database from assets to internal storage
    private void copyDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            copy(dbFile);
        }
    }

    // Method to copy the database from the assets folder "taken from stack overflow"
    private void copy(File dbFile) {
        try {
            // Create directories if they don't exist
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            InputStream inputStream = context.getAssets().open(DB_NAME);
            OutputStream outputStream = new FileOutputStream(dbFile);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "Database copied successfully to internal storage");

        } catch (IOException e) {
            Log.e(TAG, "Error copying database", e);
            throw new RuntimeException("Error copying database", e);
        }
    }

    // Method to open the database
    public SQLiteDatabase openDatabase() {
        String dbFilePath = DB_PATH + DB_NAME;
        return SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READONLY);
    }
}
