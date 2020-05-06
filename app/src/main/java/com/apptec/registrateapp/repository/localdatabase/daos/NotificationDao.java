package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    /**
     * Dao for notification
     * @return
     */


    @Query("SELECT * FROM notification")
    LiveData<List<Notification>> loadAllLiveData();

    @Query("SELECT * FROM notification where id = :id")
    List<Notification> loadNotificationSync(int id);

    @Insert
    void insert(Notification notification);

    @Delete
    void delete(Notification notification);


}
