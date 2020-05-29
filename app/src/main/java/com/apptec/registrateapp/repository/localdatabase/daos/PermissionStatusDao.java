package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.util.List;

@Dao
public interface PermissionStatusDao {
    /**
     * Dato for permissions status
     *
     * @param permissionStatusList
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PermissionStatus> permissionStatusList);

    @Query("SELECT * FROM " + DBConstants.PERMISSION_STATUS_TABLE + " WHERE " + DBConstants.PERMISSION_STATUS_PK + " = :id")
    PermissionStatus getPermissionStatus(int id);
}
