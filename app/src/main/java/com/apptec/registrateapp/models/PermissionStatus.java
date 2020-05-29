package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

@Entity(tableName = DBConstants.PERMISSION_STATUS_TABLE)
public class PermissionStatus {

    @ColumnInfo(name = DBConstants.PERMISSION_STATUS_PK)
    @PrimaryKey
    private int id;

    @ColumnInfo(name = DBConstants.PERMISSION_STATUS_NAME)
    private String statusName;

    public PermissionStatus(int id, String statusName) {
        this.id = id;
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
