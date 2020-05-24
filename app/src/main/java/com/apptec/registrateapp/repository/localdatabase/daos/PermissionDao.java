package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.registrateapp.models.PermissionModel;

import java.util.List;

@Dao
public interface PermissionDao {

    @Query("SELECT * FROM permissionmodel")
    LiveData<List<PermissionModel>> getLiveDataListPermission();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(PermissionModel permissionModel);

    @Delete
    void delete(PermissionModel permissionModel);
}
