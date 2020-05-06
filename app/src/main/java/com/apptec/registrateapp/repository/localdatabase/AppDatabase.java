package com.apptec.registrateapp.repository.localdatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apptec.registrateapp.models.Company;
import com.apptec.registrateapp.models.Device;
import com.apptec.registrateapp.models.Notification;
import com.apptec.registrateapp.models.User;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.apptec.registrateapp.repository.localdatabase.daos.DeviceDao;
import com.apptec.registrateapp.repository.localdatabase.daos.NotificationDao;
import com.apptec.registrateapp.repository.localdatabase.daos.UserDao;

// @Database(entities = {ProductEntity.class, ProductFtsEntity.class, CommentEntity.class}, version = 2)
@Database(entities = {
        User.class,
        Notification.class,
        Device.class,
        Company.class}, version = 2)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     *
     * @return
     */

    public abstract UserDao userDao();
    public abstract NotificationDao notificationDao();

    public abstract DeviceDao deviceDao();

}
