package com.appTec.RegistrateApp.models;

import androidx.annotation.NonNull;

public class PermissionType {
    private int id;
    private String permissionType;

    public PermissionType(int id, String permissionType) {
        this.id = id;
        this.permissionType = permissionType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    @NonNull
    @Override
    public String toString() {
        return this.permissionType;
    }
}
