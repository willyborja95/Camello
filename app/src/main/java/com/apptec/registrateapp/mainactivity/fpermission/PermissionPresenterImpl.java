package com.apptec.registrateapp.mainactivity.fpermission;

import androidx.lifecycle.LiveData;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.repository.localdatabase.RoomHelper;

import java.util.List;

public class PermissionPresenterImpl {

    /**
     * Helps the interaction repository - view
     */

    public LiveData<List<PermissionModel>> getLiveDataListPermission() {
        /**
         * It return the live of permission into the database
         */
        return RoomHelper.getAppDatabaseInstance().permissionDao().getLiveDataListPermission();
    }
}
