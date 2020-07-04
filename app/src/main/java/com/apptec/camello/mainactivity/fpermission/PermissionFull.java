package com.apptec.camello.mainactivity.fpermission;

import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Ignore;

import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.models.PermissionStatus;
import com.apptec.camello.models.PermissionType;
import com.apptec.camello.repository.localdatabase.DBConstants;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.apptec.camello.util.Constants;

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


    // Other attributes useful for the view
    @Ignore
    String onlyStartDate, onlyEndDate, onlyStartTime, onlyEndTime;


    public String getOnlyStartDate() {

        this.onlyStartDate = DateConverter.toStringDateFormat(this.getPermissionModel().getStartDate(), Constants.PATTERN_DATE_FORMAT_PERMISSION_DATE);
        return this.onlyStartDate;
    }

    public String getOnlyEndDate() {
        this.onlyEndDate = DateConverter.toStringDateFormat(this.getPermissionModel().getEndDate(), Constants.PATTERN_DATE_FORMAT_PERMISSION_DATE);
        return this.onlyEndDate;
    }

    public String getOnlyStartTime() {
        this.onlyStartTime = DateConverter.toStringDateFormat(this.getPermissionModel().getStartDate(), Constants.PATTERN_DATE_FORMAT_PERMISSION_TIME);
        return this.onlyStartTime;
    }

    public String getOnlyEndTime() {
        this.onlyEndTime = DateConverter.toStringDateFormat(this.getPermissionModel().getEndDate(), Constants.PATTERN_DATE_FORMAT_PERMISSION_TIME);
        return this.onlyEndTime;
    }


}
