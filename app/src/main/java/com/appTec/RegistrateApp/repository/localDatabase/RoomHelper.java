package com.appTec.RegistrateApp.repository.localDatabase;

import androidx.room.Room;

import com.appTec.RegistrateApp.App;
import com.appTec.RegistrateApp.util.Constants;

public class RoomHelper {
    /**
     * This class provide a singleton of RoomDatabase
     */

    private static final AppDatabase sAppDatabase = Room.databaseBuilder(App.getContext(), AppDatabase.class, Constants.DATABASE_NAME)
            .allowMainThreadQueries()
            .build();

    public static AppDatabase getAppDatabaseInstance(){
        return sAppDatabase;
    }

}
