package com.apptec.registrateapp.repository.localDatabase.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.User;

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

    @Query("SELECT * FROM user WHERE 1=1 LIMIT 1")
    LiveData<User> getLiveDataUser();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
