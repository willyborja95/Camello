package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.UserModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

/**
 * Dao fro User
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM " + DBConstants.USER_TABLE)
    List<UserModel> getAll();

    @Query("SELECT * FROM " + DBConstants.USER_TABLE + " WHERE 1=1 LIMIT 1")
    UserModel getUser();

    @Query("SELECT * FROM " + DBConstants.USER_TABLE + " WHERE 1=1 LIMIT 1")
    LiveData<UserModel> getLiveDataUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(UserModel user);

    @Delete
    void delete(UserModel user);

    @Query("DELETE FROM " + DBConstants.USER_TABLE)
    void deleteAll();


}
