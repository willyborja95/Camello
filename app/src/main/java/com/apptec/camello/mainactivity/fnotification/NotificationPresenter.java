package com.apptec.camello.mainactivity.fnotification;

import androidx.lifecycle.LiveData;

import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.repository.localdatabase.RoomHelper;

import java.util.List;

public class NotificationPresenter {
    /**
     * This class is in the middle of the MVVM pattern.
     * It interacts as an intermediary between the Model and the View.
     */


    public NotificationPresenter() {
        /**
         * Empty constructor
         * */

    }


    public LiveData<List<NotificationModel>> loadNotificationsLiveData() {
        /**
         * Return the notifications into the live data
         */
        return RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
    }
}
