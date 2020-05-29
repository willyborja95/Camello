package com.apptec.registrateapp.mainactivity.fpermission;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.util.List;

@Dao
public interface PermissionFullDao {

    @Query("SELECT * FROM " + DBConstants.DV_PERMISSION_FULL)
    LiveData<List<PermissionFull>> getAllPermissionsFull();
}
