package com.appTec.RegistrateApp.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Permission implements Serializable {
    private int id;
    private PermissionType permissionType;
    private PermissionStatus permissionStatus;
    private Calendar startDate;
    private Calendar endDate;

    public Permission(PermissionType permissionType, PermissionStatus permissionStatus, Calendar startDate, Calendar endDate) {
        this.permissionType = permissionType;
        this.permissionStatus = permissionStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Permission(PermissionType permissionType, Calendar startDate, Calendar endDate) {
        this.permissionType = permissionType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Permission(int id, PermissionType permissionType, PermissionStatus permissionStatus, Calendar startDate, Calendar endDate) {
        this.id = id;
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

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}
