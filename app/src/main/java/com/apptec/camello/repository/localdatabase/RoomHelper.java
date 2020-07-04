package com.apptec.camello.repository.localdatabase;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.apptec.camello.App;

public class RoomHelper {
    /**
     * This class provide a singleton of RoomDatabase
     */

    private static final RoomDatabase.Builder<AppDatabase> builder = Room.databaseBuilder(App.getContext(), AppDatabase.class, DBConstants.DATABASE_NAME)
            //      .allowMainThreadQueries()   This was removed because is dangerous to the UX using Room in the main thread. It is very time expensive
            .fallbackToDestructiveMigration();

    private static final AppDatabase sAppDatabase = builder.build();



    public static AppDatabase getAppDatabaseInstance(){
        return sAppDatabase;
    }

}
