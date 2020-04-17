package com.appTec.RegistrateApp.repository.localDatabase.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.appTec.RegistrateApp.models.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    /**
     * Dao for notification
     * @return
     */


    @Query("SELECT * FROM notification")
    List<Notification> getAll();

    @Insert
    void insert(Notification notification);

    @Delete
    void delete(Notification notification);


}
