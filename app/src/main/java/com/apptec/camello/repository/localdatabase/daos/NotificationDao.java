package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

@Dao
public interface NotificationDao {
    /**
     * Dao for notification
     * @return
     */


    @Query("SELECT * FROM " + DBConstants.NOTIFICATION_TABLE)
    LiveData<List<NotificationModel>> loadAllLiveData();

    @Query("SELECT * FROM " + DBConstants.NOTIFICATION_TABLE + " where " + DBConstants.NOTIFICATION_PK + " = :id")
    List<NotificationModel> loadNotificationSync(int id);

    @Insert
    void insert(NotificationModel notification);

    @Delete
    void delete(NotificationModel notification);


}
