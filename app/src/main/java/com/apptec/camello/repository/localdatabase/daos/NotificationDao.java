package com.apptec.camello.repository.localdatabase.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.repository.localdatabase.DBConstants;

import java.util.List;

/**
 * Dao for notification
 */
@Dao
public interface NotificationDao {

    @Query("SELECT * FROM " + DBConstants.NOTIFICATION_TABLE + " ORDER BY " + DBConstants.NOTIFICATION_SENT_DATE + " DESC")
    LiveData<List<NotificationModel>> loadAllLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceList(List<NotificationModel> notificationModelList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(NotificationModel notificationModel);

    @Insert
    void insert(NotificationModel notification);

    @Delete
    void delete(NotificationModel notification);


}
