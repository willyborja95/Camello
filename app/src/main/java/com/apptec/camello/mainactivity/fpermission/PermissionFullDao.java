package com.apptec.camello.mainactivity.fpermission;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PermissionFullDao {

    @Query("SELECT * FROM permissionfull")
        // Using the view
    LiveData<List<PermissionFull>> getAllPermissionsFull();
}
