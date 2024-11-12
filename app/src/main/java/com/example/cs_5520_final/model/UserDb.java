package com.example.cs_5520_final.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDb extends RoomDatabase {

    private static volatile UserDb INSTANCE;

    // Abstract method for accessing the UserDao
    public abstract UserDao userDao();

    // Singleton pattern to get the instance of the database
    public static UserDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UserDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    UserDb.class, "user_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
