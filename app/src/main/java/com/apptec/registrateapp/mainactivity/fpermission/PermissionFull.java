package com.apptec.registrateapp.mainactivity.fpermission;

import androidx.room.DatabaseView;
import androidx.room.Embedded;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.models.PermissionStatus;
import com.apptec.registrateapp.models.PermissionType;
import com.apptec.registrateapp.repository.localdatabase.DBConstants;

@DatabaseView("SELECT * " +
        " FROM " + DBConstants.PERMISSION_TABLE + ", " + DBConstants.PERMISSION_STATUS_TABLE + ", " + DBConstants.PERMISSION_TYPE_TABLE +
        " WHERE " + DBConstants.PERMISSION_PERMISSION_STATUS_FK + " = " + DBConstants.PERMISSION_STATUS_PK +
        " AND " + DBConstants.PERMISSION_PERMISSION_TYPE_FK + " = " + DBConstants.PERMISSION_TYPE_PK +
        ";")
public class PermissionFull {
    /**
     * This class allow us to access the permission type and permission status directly
     * instead doing a query every time we need to
     */


    @Embedded
    PermissionModel permissionModel;

    @Embedded
    PermissionStatus permissionStatus;

    @Embedded
    PermissionType permissionType;

    public PermissionModel getPermissionModel() {
        return permissionModel;
    }

    public void setPermissionModel(PermissionModel permissionModel) {
        this.permissionModel = permissionModel;
    }

    public PermissionStatus getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(PermissionStatus permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }
}
