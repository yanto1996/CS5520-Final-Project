package com.example.cs_5520_final.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data Access Object to do DB work
 */
@Dao
public interface UserDao {

    @Insert
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity getUserByEmailAndPassword(String email, String password);

    // New method to get the first user
    @Query("SELECT * FROM users LIMIT 1")
    UserEntity getUser();
}
