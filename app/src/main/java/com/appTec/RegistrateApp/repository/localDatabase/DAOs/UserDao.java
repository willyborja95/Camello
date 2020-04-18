package com.appTec.RegistrateApp.repository.localDatabase.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.appTec.RegistrateApp.models.User;

import java.util.List;

@Dao
public interface UserDao {
    /**
     * Dao fro User
     * @return
     */

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE 1=1 LIMIT 1")
    User getUser();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}