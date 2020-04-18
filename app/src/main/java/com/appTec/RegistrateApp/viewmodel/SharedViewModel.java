package com.appTec.RegistrateApp.viewmodel;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.repository.localDatabase.RoomHelper;

import java.util.List;


public class SharedViewModel extends AndroidViewModel {

    private final LiveData<List<Notification>> mNotifications; // List of notifications



    public SharedViewModel(@NonNull Application application) {
        super(application);


        mNotifications = RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
    }


    /**
     * Expose the LiveData notifications so the UI can observe it
     */
    public LiveData<List<Notification>> getNotifications(){
        return mNotifications;

    };



}
