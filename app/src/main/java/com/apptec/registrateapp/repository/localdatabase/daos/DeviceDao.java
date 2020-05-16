package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.DeviceModel;

import java.util.List;


@Dao
public interface DeviceDao {
    /**
     * Dao for device
     * @return
     */


    @Query("SELECT * FROM DeviceModel")
    LiveData<List<DeviceModel>> loadAllDevicesLiveData();

    @Query("SELECT * FROM DeviceModel where id = :id")
    List<DeviceModel> loadNotificationSync(int id);

    @Insert
    void insert(DeviceModel device);

    @Delete
    void delete(DeviceModel device);

    @Query("DELETE FROM devicemodel")
    void deleteAll();
}
