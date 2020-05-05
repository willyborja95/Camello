package com.apptec.registrateapp.repository.localDatabase;

import androidx.room.Room;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.util.Constants;

public class RoomHelper {
    /**
     * This class provide a singleton of RoomDatabase
     */

    private static final AppDatabase sAppDatabase = Room.databaseBuilder(App.getContext(), AppDatabase.class, Constants.DATABASE_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build();

    public static AppDatabase getAppDatabaseInstance(){
        return sAppDatabase;
    }

}
