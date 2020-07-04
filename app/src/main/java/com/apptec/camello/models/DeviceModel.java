package com.apptec.camello.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = DBConstants.DEVICE_TABLE)
public class DeviceModel implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = DBConstants.DEVICE_PK)
    private int id;


    @ColumnInfo(name = DBConstants.DEVICE_NAME)
    private String name;

    @ColumnInfo(name = DBConstants.DEVICE_MODEL)
    private String model;


    @SerializedName("identifier")
    @ColumnInfo(name = DBConstants.DEVICE_IDENTIFIER)
    private String identifier;   // This is the imei


    @ColumnInfo(name = DBConstants.DEVICE_ACTIVE)
    private boolean active;

    @ColumnInfo(name = DBConstants.DEVICE_FIREBASE_TOKEN)
    private String pushToken;

    @ColumnInfo(name = DBConstants.DEVICE_PLATFORM)
    private int platform = 0;        // 0 if it is Android and 1 if it is Iphone

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", identifier='" + identifier + '\'' +
                ", status=" + active +
                ", pushToken='" + pushToken + '\'' +
                ", platform=" + platform +
                '}';
    }
}
