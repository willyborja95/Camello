package com.apptec.registrateapp.repository.localDatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apptec.registrateapp.models.Company;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.models.User;
import com.apptec.registrateapp.repository.localDatabase.DAOs.NotificationDao;
import com.apptec.registrateapp.repository.localDatabase.DAOs.UserDao;
import com.apptec.registrateapp.repository.localDatabase.converter.DateConverter;

// @Database(entities = {ProductEntity.class, ProductFtsEntity.class, CommentEntity.class}, version = 2)
@Database(entities = {User.class, Notification.class, Device.class, Company.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     *
     * @return
     */

    public abstract UserDao userDao();
    public abstract NotificationDao notificationDao();
}
