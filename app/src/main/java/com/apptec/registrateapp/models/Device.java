package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Device implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "imei")
    @SerializedName("identifier")
    private String identifier;   // This is the imei

    @ColumnInfo(name = "active")
    private boolean active;

    @ColumnInfo(name = "pushToken")
    private String pushToken;

    @ColumnInfo(name = "platform")
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
