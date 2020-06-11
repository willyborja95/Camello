package com.apptec.registrateapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = DBConstants.PERMISSION_TYPE_TABLE)
public class PermissionType implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = DBConstants.PERMISSION_TYPE_PK)
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = DBConstants.PERMISSION_TYPE_NAME)
    @SerializedName("name")
    private String typeName;

    public PermissionType(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @NonNull
    @Override
    public String toString() {
        return "" + getTypeName();
    }
}
