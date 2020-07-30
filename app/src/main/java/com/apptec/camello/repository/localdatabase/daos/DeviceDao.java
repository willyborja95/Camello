package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

/**
 * Dao for device
 */
@Dao
public interface DeviceDao {

    @Query("SELECT * FROM " + DBConstants.DEVICE_TABLE)
    LiveData<List<DeviceModel>> loadAllDevicesLiveData();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(DeviceModel device);

    @Delete
    void delete(DeviceModel device);

    @Query("DELETE FROM " + DBConstants.DEVICE_TABLE)
    void deleteAll();
}
