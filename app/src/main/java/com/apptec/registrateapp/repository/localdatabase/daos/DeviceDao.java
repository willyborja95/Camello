package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.Device;

import java.util.List;


@Dao
public interface DeviceDao {
    /**
     * Dao for device
     * @return
     */


    @Query("SELECT * FROM device")
    LiveData<List<Device>> loadAllDevicesLiveData();

    @Query("SELECT * FROM device where id = :id")
    List<Device> loadNotificationSync(int id);

    @Insert
    void insert(Device device);

    @Delete
    void delete(Device device);

}
