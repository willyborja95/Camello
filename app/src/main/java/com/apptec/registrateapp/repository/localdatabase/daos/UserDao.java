package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.UserModel;

import java.util.List;

@Dao
public interface UserDao {
    /**
     * Dao fro User
     * @return
     */

    @Query("SELECT * FROM UserModel")
    List<UserModel> getAll();

    @Query("SELECT * FROM UserModel WHERE 1=1 LIMIT 1")
    UserModel getUser();

    @Query("SELECT * FROM UserModel WHERE 1=1 LIMIT 1")
    LiveData<UserModel> getLiveDataUser();

    @Insert
    void insert(UserModel user);

    @Delete
    void delete(UserModel user);
}
