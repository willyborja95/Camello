package com.appTec.RegistrateApp.viewmodel;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.RoomDatabase;

import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.repository.localDatabase.RoomHelper;

import java.util.List;


public class SharedViewModel extends AndroidViewModel {

    // To show the notifications
    private final LiveData<List<Notification>> mNotifications; // List of notifications


    // This info will be on the drawer
    private final LiveData<User> mUser;


    public SharedViewModel(@NonNull Application application) {
        super(application);

        // Load here the live data needed
        mNotifications = RoomHelper.getAppDatabaseInstance().notificationDao().loadAllLiveData();
        mUser = RoomHelper.getAppDatabaseInstance().userDao().getLiveDataUser();

    }


    /**
     * Expose the LiveData so the UI can observe it
     */
    public LiveData<List<Notification>> getNotifications(){
        /** Exposing the notifications */
        return mNotifications;
    };

    public LiveData<User> getCurrentUser(){
        /** Exposing the user */
        return mUser;
    }



}
