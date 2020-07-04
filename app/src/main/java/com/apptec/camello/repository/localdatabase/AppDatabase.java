package com.apptec.camello.repository.localdatabase;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.apptec.camello.mainactivity.fpermission.PermissionFull;
import com.apptec.camello.mainactivity.fpermission.PermissionFullDao;
import com.apptec.camello.models.CompanyModel;
import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.models.NotificationModel;
import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.models.UserModel;
import com.apptec.camello.models.WorkZoneModel;
import com.apptec.camello.models.WorkingPeriodModel;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.apptec.camello.repository.localdatabase.daos.CompanyDao;
import com.apptec.camello.repository.localdatabase.daos.DeviceDao;
import com.apptec.camello.repository.localdatabase.daos.NotificationDao;
import com.apptec.camello.repository.localdatabase.daos.PermissionDao;
import com.apptec.camello.repository.localdatabase.daos.PermissionStatusDao;
import com.apptec.camello.repository.localdatabase.daos.PermissionTypeDao;
import com.apptec.camello.repository.localdatabase.daos.UserDao;
import com.apptec.camello.repository.localdatabase.daos.WorkZoneDao;
import com.apptec.camello.repository.localdatabase.daos.WorkingPeriodDao;


@Database(entities = {
        UserModel.class,
        NotificationModel.class,
        DeviceModel.class,
        CompanyModel.class,
        WorkZoneModel.class,
        WorkingPeriodModel.class,
        PermissionType.class,
        PermissionStatus.class,
        PermissionModel.class},
        views = {
                PermissionFull.class
        },
        version = 16,
        exportSchema = true)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Database
     * <p>
     * Register here the entities and the DAOs
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

    public abstract PermissionStatusDao permissionStatusDao();

    public abstract PermissionTypeDao permissionTypeDao();

    public abstract PermissionFullDao permissionFullDao();

}
