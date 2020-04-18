package com.appTec.RegistrateApp.repository.localDatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.appTec.RegistrateApp.models.Company;
import com.appTec.RegistrateApp.models.Device;
import com.appTec.RegistrateApp.models.Notification;
import com.appTec.RegistrateApp.models.User;
import com.appTec.RegistrateApp.repository.localDatabase.DAOs.NotificationDao;
import com.appTec.RegistrateApp.repository.localDatabase.DAOs.UserDao;
import com.appTec.RegistrateApp.repository.localDatabase.converter.DateConverter;

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
