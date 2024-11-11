package com.example.cs_5520_final.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cs_5520_final.view.RegisterActivity;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class UserDb extends RoomDatabase {

    private static volatile UserDb INSTANCE;

    public abstract UserDao userDao();

    public static UserDb getInstance(RegisterActivity context) {
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
