package com.apptec.registrateapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PermissionStatus {

    @PrimaryKey
    private int id;

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
