package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class PermissionModel implements Serializable {

    @PrimaryKey
    private int id;

    private String comment;

    @ColumnInfo(name = "permissionType")
    private PermissionType permissionType;

    @ColumnInfo(name = "permissionStatus")
    private PermissionStatus permissionStatus;

    @ColumnInfo(name = "startDate")
    private Long startDate;

    @ColumnInfo(name = "endDate")
    private Long endDate;

    public PermissionModel(int id, String comment, PermissionType permissionType, PermissionStatus permissionStatus, Long startDate, Long endDate) {
        /**
         * Full constructor
         */
        this.id = id;
        this.comment = comment;
        this.permissionType = permissionType;
        this.permissionStatus = permissionStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
}

