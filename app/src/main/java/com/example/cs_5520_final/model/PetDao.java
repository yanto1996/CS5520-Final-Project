package com.example.cs_5520_final.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class PetDao {

    private SQLiteDatabase database;

    public PetDao(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        database = dbHelper.openDatabase();
    }

    public List<PetModel> getAllPets() {
        List<PetModel> pets = new ArrayList<>();
        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String type = cursor.getString(cursor.getColumnIndexOrThrow("Type"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    int age = cursor.getInt(cursor.getColumnIndexOrThrow("Age"));
                    String breed = cursor.getString(cursor.getColumnIndexOrThrow("Breed"));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"));
                    String color = cursor.getString(cursor.getColumnIndexOrThrow("Color"));
                    String furLength = cursor.getString(cursor.getColumnIndexOrThrow("Fur Length"));
                    int vaccinated = cursor.getInt(cursor.getColumnIndexOrThrow("Vaccinated"));
                    String state = cursor.getString(cursor.getColumnIndexOrThrow("State"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));

                    pets.add(new PetModel(type, name, age, breed, gender, color, furLength, vaccinated, state, description));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return pets;
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
