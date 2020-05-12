package com.apptec.registrateapp.mainactivity.fnotification;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;

import java.util.List;

public class NotificationPresenterImpl {
    /**
     * This class is in the middle of the MVVM pattern.
    * It interacts as an intermediary between the Model and the View.
    * */


    public NotificationPresenterImpl() {
        /**
         * Empty constructor
        * */

    }


    public LiveData<List<Notification>> loadNotificationsLiveData() {
        /**
         * Return the notifications into the live data
         */
        return RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
    }
}
