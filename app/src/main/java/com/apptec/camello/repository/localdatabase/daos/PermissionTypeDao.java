package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

@Dao
public interface PermissionTypeDao {
    /**
     * Dao fro PermissionType
     * @return
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PermissionType> permissionTypeList);


    @Query("SELECT * FROM " + DBConstants.PERMISSION_TYPE_TABLE + " WHERE " + DBConstants.PERMISSION_TYPE_PK + " = :id")
    PermissionType getPermissionType(int id);

    @Query("SELECT * FROM " + DBConstants.PERMISSION_TYPE_TABLE)
    LiveData<List<PermissionType>> getPermissionTypes();
}
