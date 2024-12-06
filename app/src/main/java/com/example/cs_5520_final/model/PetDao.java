package com.example.cs_5520_final.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pet DAO class that does all the work for interacting with pet SQLite DB
 * Queries all pet data and stores it into a new petmodel list object
 */
public class PetDao {

    private SQLiteDatabase database;

    /**
     * Dbhelper object to help move SQLite to internal storage
     * @param context information to access
     */
    public PetDao(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        database = dbHelper.openDatabase();
    }

    /**
     * Default display for the view. Gets 10 random pets
     * @param limit pet limit (10)
     * @return list of 10 random pets
     */
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

    /**
     *
     * @param searchQuery user search input
     * @return list of pets based on type or breed
     */
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

    /**
     *
     * @param searchQuery user input
     * @return list of pets by state
     */
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

    /**
     *
     * @param typeOrBreedQuery user input for type or breed
     * @param stateQuery user input for state
     * @return list of pets by type/breed and state
     */
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


    public List<PetModel> getPetsByTypeBreedStateAndAge(String typeOrBreedQuery, String stateQuery, int ageQuery) {
        List<PetModel> pets = new ArrayList<>();
        String selection = "(Type LIKE ? AND Age LIKE ? AND State LIKE ?) OR (Breed LIKE ? AND Age LIKE ? and State LIKE ?)";
        String[] selectionArgs = new String[]{
                "%" + typeOrBreedQuery + "%",
                String.valueOf(ageQuery),
                "%" + stateQuery + "%",
                "%" + typeOrBreedQuery + "%",
                String.valueOf(ageQuery),
                "%" + stateQuery + "%"
        };
        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs, null, null, null);

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

    /**
     *
     * @param state user input for state
     * @param age user input for age
     * @return list of pets based on state and age
     */
    public List<PetModel> getPetsByStateAndAge(String state, int age) {
        List<PetModel> pets = new ArrayList<>();
        String selection = "State LIKE ? AND Age = ?";
        String[] selectionArgs = new String[]{"%" + state + "%", String.valueOf(age)};

        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data and add to pets list
                pets.add(extractPetFromCursor(cursor));
            }
            cursor.close();
        }
        return pets;
    }

    /**
     *
     * @param age user input for age
     * @return list of pets based on age
     */
    public List<PetModel> getPetsByAge(int age) {
        List<PetModel> pets = new ArrayList<>();
        String selection = "Age = ?";
        String[] selectionArgs = new String[]{String.valueOf(age)};

        Cursor cursor = database.query("dataset",
                new String[]{"Type", "Name", "Age", "Breed", "Gender", "Color", "[Fur Length]", "Vaccinated", "State", "Description"},
                selection, selectionArgs, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                // Extract data and add to pets list
                pets.add(extractPetFromCursor(cursor));
            }
            cursor.close();
        }
        return pets;
    }

    /**
     *
     * @param typeOrBreedQuery user input for type or breed
     * @param ageQuery user input for age
     * @return list of pets for type/breed and age
     */
    public List<PetModel> getPetsByTypeOrBreedAndAge(String typeOrBreedQuery, int ageQuery) {
        List<PetModel> pets = new ArrayList<>();

        String selection = "(Type LIKE ? AND Age LIKE ?) OR (Breed LIKE ? AND Age LIKE ?)";
        String[] selectionArgs = new String[]{
                "%" + typeOrBreedQuery + "%",
                String.valueOf(ageQuery),
                "%" + typeOrBreedQuery + "%",
                String.valueOf(ageQuery),
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

    /**
     *
     * @param cursor sqlite cursor
     * @return new pet model with information gathered from SQLite cursor
     */
    private PetModel extractPetFromCursor(Cursor cursor) {
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
        return new PetModel(type, name, age, breed, gender, color, furLength, vaccinated, state, description);
    }
}
