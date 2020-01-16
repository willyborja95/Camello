package com.appTec.RegistrateApp.models;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Permission {
    private int id;
    private PermissionType permissionType;
    private PermissionStatus permissionStatus;
    private Date startDate;
    private Date endDate;

    public Permission(PermissionType permissionType, PermissionStatus permissionStatus, Date startDate, Date endDate) {
        this.permissionType = permissionType;
        this.permissionStatus = permissionStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public PermissionStatus getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(PermissionStatus permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
