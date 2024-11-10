package com.example.cs_5520_final.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PetDao {

    private SQLiteDatabase database;

    public PetDao(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        database = dbHelper.openDatabase();
    }

    public List<PetModel> getTenPets(int limit) {
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
        Collections.shuffle(pets);
        return pets.subList(0,limit);
    }

    public List<PetModel> getPetsByTypeOrBreed(String searchQuery) {
        List<PetModel> pets = new ArrayList<>();
        String selection = "Type LIKE ? OR Breed LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%", "%" + searchQuery + "%"};

        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs,
                null, null, null);

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

    public List<PetModel> getPetsByState(String searchQuery) {
        List<PetModel> pets = new ArrayList<>();

        String selection = "State LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%"};

        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs,
                null, null, null);

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

    public List<PetModel> getPetsByTypeOrBreedAndState(String typeOrBreedQuery, String stateQuery) {
        List<PetModel> pets = new ArrayList<>();

        if (typeOrBreedQuery.isEmpty() || stateQuery.isEmpty()) {
            return pets;
        }

        String selection = "(Type LIKE ? AND State LIKE ?) OR (Breed LIKE ? AND State LIKE ?)";
        String[] selectionArgs = new String[]{
                "%" + typeOrBreedQuery + "%",
                "%" + stateQuery + "%",
                "%" + typeOrBreedQuery + "%",
                "%" + stateQuery + "%"
        };

        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs,
                null, null, null);

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

}
