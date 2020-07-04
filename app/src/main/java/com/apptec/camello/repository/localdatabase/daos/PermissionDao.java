package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

@Dao
public interface PermissionDao {

    @Query("SELECT * FROM " + DBConstants.PERMISSION_TABLE)
    LiveData<List<PermissionModel>> getLiveDataListPermission();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(PermissionModel permissionModel);

    @Delete
    void delete(PermissionModel permissionModel);
}
