package com.apptec.registrateapp.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.registrateapp.models.NotificationModel;

import java.util.List;

@Dao
public interface NotificationDao {
    /**
     * Dao for notification
     * @return
     */


    @Query("SELECT * FROM NotificationModel")
    LiveData<List<NotificationModel>> loadAllLiveData();

    @Query("SELECT * FROM NotificationModel where id = :id")
    List<NotificationModel> loadNotificationSync(int id);

    @Insert
    void insert(NotificationModel notification);

    @Delete
    void delete(NotificationModel notification);


}
