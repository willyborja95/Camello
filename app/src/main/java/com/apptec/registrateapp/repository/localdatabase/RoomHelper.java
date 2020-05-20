package com.apptec.registrateapp.repository.localdatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apptec.registrateapp.App;
import com.apptec.registrateapp.util.Constants;

public class RoomHelper {
    /**
     * This class provide a singleton of RoomDatabase
     */

    private static final RoomDatabase.Builder<AppDatabase> builder = Room.databaseBuilder(App.getContext(), AppDatabase.class, Constants.DATABASE_NAME)
            //      .allowMainThreadQueries()   This was removed because is dangerous to the UX using Room in the main thread. It is very time expensive
            .fallbackToDestructiveMigration();

    private static final AppDatabase sAppDatabase = builder.build();



    public static AppDatabase getAppDatabaseInstance(){
        return sAppDatabase;
    }

}
