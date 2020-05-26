package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.registrateapp.models.PermissionType;

import java.util.List;

@Dao
public interface PermissionTypeDao {
    /**
     * Dao fro PermissionType
     * @return
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PermissionType> permissionTypeList);


    @Query("SELECT * FROM permissiontype WHERE id = :id")
    PermissionType getPermissionType(int id);

    @Query("SELECT * FROM permissiontype")
    LiveData<List<PermissionType>> getPermissionTypes();
}
