package com.apptec.registrateapp.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(foreignKeys = {
        @ForeignKey(entity = PermissionType.class,
                parentColumns = "id",
                childColumns = "fkPermissionType",
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = PermissionStatus.class,
                parentColumns = "id",
                childColumns = "fkPermissionStatus",
                onDelete = ForeignKey.CASCADE)
})
public class PermissionModel implements Serializable {

    @PrimaryKey
    private int id;

    private String comment;

    @SerializedName("type")
    private int fkPermissionType;

    private int fkPermissionStatus;


    private Long startDate;

    private Long endDate;


    public PermissionModel(String comment, int fkPermissionType, int fkPermissionStatus, Long startDate, Long endDate) {
        this.comment = comment;
        this.fkPermissionType = fkPermissionType;
        this.fkPermissionStatus = fkPermissionStatus;
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

    public int getFkPermissionType() {
        return fkPermissionType;
    }

    public void setFkPermissionType(int fkPermissionType) {
        this.fkPermissionType = fkPermissionType;
    }

    public int getFkPermissionStatus() {
        return fkPermissionStatus;
    }

    public void setFkPermissionStatus(int fkPermissionStatus) {
        this.fkPermissionStatus = fkPermissionStatus;
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

