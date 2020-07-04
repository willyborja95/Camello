package com.apptec.camello.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = DBConstants.PERMISSION_STATUS_TABLE)
public class PermissionStatus {

    @ColumnInfo(name = DBConstants.PERMISSION_STATUS_PK)
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = DBConstants.PERMISSION_STATUS_NAME)
    @SerializedName("name")
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

    @Override
    public String toString() {
        return "PermissionStatus{" +
                "id=" + id +
                ", statusName='" + statusName + '\'' +
                '}';
    }
}
