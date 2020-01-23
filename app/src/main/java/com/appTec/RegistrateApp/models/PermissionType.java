package com.appTec.RegistrateApp.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class PermissionType implements Serializable {
    private int id;
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
