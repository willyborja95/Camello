package com.apptec.registrateapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class PermissionType implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "nombre")
    private String nombe;

    public PermissionType(int id, String permissionType) {
        this.id = id;
        this.nombe = permissionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombe() {
        return nombe;
    }

    public void setNombe(String nombe) {
        this.nombe = nombe;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nombe;
    }
}
