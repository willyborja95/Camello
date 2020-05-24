package com.apptec.registrateapp.repository.localdatabase;

import android.util.Log;

import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.sharedpreferences.SharedPreferencesHelper;
import com.apptec.registrateapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class DataGenerator {
    /**
     * This class will pre populate the room database in the first run
     * <p>
     * We could have used a file.db but if we change the schema in the future,
     * we need to manually go to the file.db in rewrite it. So in this way have mor control
     * on the insert queries when the schema change.
     */
    private static final String TAG = "DataGenerator";

    public static void prepopulateDatabase() {
        /**
         * Main method of this class
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean populated_database = SharedPreferencesHelper.getSharedPreferencesInstance().getBoolean(Constants.NEED_TO_POPULATE_DATABASE, true);
                if (populated_database) {
                    Log.w(TAG, "Populating database");
                    // Prepopulate the permissions types
                    RoomHelper.getAppDatabaseInstance().permissionTypeDao().insertAll(getPermissionTypesToPrepopulate());

                    // Prepopulate the permissions status
                    RoomHelper.getAppDatabaseInstance().permissionStatusDao().insertAll(getPermissionStatusToPrepopulate());

                    SharedPreferencesHelper.putBooleanValue(Constants.NEED_TO_POPULATE_DATABASE, false);
                }


            }


        }).start();

    }


    private static List<PermissionType> getPermissionTypesToPrepopulate() {
        /**
         * We can obtain this data from any source. For now we are 'hardcoding' it
         */
        List<PermissionType> permissionTypesList = new ArrayList<>();

        PermissionType permissionType1 = new PermissionType(1, "Permiso");
        PermissionType permissionType2 = new PermissionType(2, "Vacaciones");

        permissionTypesList.add(permissionType1);
        permissionTypesList.add(permissionType2);

        return permissionTypesList;
    }

    private static List<PermissionStatus> getPermissionStatusToPrepopulate() {
        /**
         * We can obtain this data from any source. For now we are 'hardcoding' it
         */
        List<PermissionStatus> permissionStatusList = new ArrayList<>();

        PermissionStatus permissionStatus1 = new PermissionStatus(1, "En reevisión");
        PermissionStatus permissionStatus2 = new PermissionStatus(2, "Aceptado");
        PermissionStatus permissionStatus3 = new PermissionStatus(3, "Rechazado");

        permissionStatusList.add(permissionStatus1);
        permissionStatusList.add(permissionStatus2);
        permissionStatusList.add(permissionStatus3);


        return permissionStatusList;
    }
}
