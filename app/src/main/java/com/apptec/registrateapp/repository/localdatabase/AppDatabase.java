package com.apptec.registrateapp.repository.localdatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apptec.registrateapp.models.CompanyModel;
import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.NotificationModel;
import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.models.UserModel;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.apptec.registrateapp.models.WorkingPeriodModel;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.apptec.registrateapp.repository.localdatabase.daos.CompanyDao;
import com.apptec.registrateapp.repository.localdatabase.daos.DeviceDao;
import com.apptec.registrateapp.repository.localdatabase.daos.NotificationDao;
import com.apptec.registrateapp.repository.localdatabase.daos.PermissionDao;
import com.apptec.registrateapp.repository.localdatabase.daos.UserDao;
import com.apptec.registrateapp.repository.localdatabase.daos.WorkZoneDao;
import com.apptec.registrateapp.repository.localdatabase.daos.WorkingPeriodDao;


@Database(entities = {
        UserModel.class,
        NotificationModel.class,
        DeviceModel.class,
        CompanyModel.class,
        WorkZoneModel.class,
        WorkingPeriodModel.class,
        PermissionModel.class}, version = 8)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**s
     *
     * @return
     */

    public abstract UserDao userDao();
    public abstract NotificationDao notificationDao();
    public abstract DeviceDao deviceDao();
    public abstract CompanyDao companyDao();
    public abstract WorkZoneDao workZoneDao();
    public abstract WorkingPeriodDao workingPeriodDao();

    public abstract PermissionDao permissionDao();

}
