package com.apptec.camello.repository.localdatabase.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

/**
 * Dat for permissions status
 */
@Dao
public interface PermissionStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PermissionStatus> permissionStatusList);

    @Query("SELECT * FROM " + DBConstants.PERMISSION_STATUS_TABLE + " WHERE " + DBConstants.PERMISSION_STATUS_PK + " = :id")
    PermissionStatus getPermissionStatus(int id);

    @Query("SELECT * FROM " + DBConstants.PERMISSION_STATUS_TABLE)
    List<PermissionStatus> getListPermission();
}
