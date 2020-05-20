package com.apptec.registrateapp.repository.localdatabase;

import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.models.PermissionType;

import java.util.List;

public class DataGenerator {
    /**
     * This class will pre populate the room database in the first run
     * <p>
     * We could have used a file.db but if we change the schema in the future,
     * we need to manually go to the file.db in rewrite it. So in this way have mor control
     * on the insert queries when the schema change.
     */

    public void prepopulateDatabase() {
        /**
         * Main method of this class
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Prepopulate the permissions types
                RoomHelper.getAppDatabaseInstance().permissionTypeDao().insertAll(getPermissionTypesToPrepopulate());

                // Prepopulate the permissions status
                RoomHelper.getAppDatabaseInstance().permissionStatusDao().insertAll(getPermissionStatusToPrepopulate());
            }


        }).start();

    }


    private List<PermissionType> getPermissionTypesToPrepopulate() {
        /**
         * We can obtain this data from any source. For now we are 'hardcoding' it
         */
        // TODO:
        return null;
    }

    private List<PermissionStatus> getPermissionStatusToPrepopulate() {
        /**
         * We can obtain this data from any source. For now we are 'hardcoding' it
         */
        // TODO
        return null;
    }
}
