package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = DBConstants.PERMISSION_TABLE, foreignKeys = {
        @ForeignKey(entity = PermissionType.class,
                parentColumns = DBConstants.PERMISSION_TYPE_PK,
                childColumns = DBConstants.PERMISSION_PERMISSION_TYPE_FK,
                onDelete = ForeignKey.CASCADE),

        @ForeignKey(entity = PermissionStatus.class,
                parentColumns = DBConstants.PERMISSION_STATUS_PK,
                childColumns = DBConstants.PERMISSION_PERMISSION_STATUS_FK,
                onDelete = ForeignKey.CASCADE)
})
public class PermissionModel implements Serializable {

    @ColumnInfo(name = DBConstants.PERMISSION_PK)
    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = DBConstants.PERMISSION_COMMENT)
    private String comment;

    @ColumnInfo(name = DBConstants.PERMISSION_PERMISSION_TYPE_FK)
    @SerializedName("type")
    private int fkPermissionType;

    @ColumnInfo(name = DBConstants.PERMISSION_PERMISSION_STATUS_FK)
    private int fkPermissionStatus;

    @ColumnInfo(name = DBConstants.PERMISSION_START_DATE)
    private Long startDate;

    @ColumnInfo(name = DBConstants.PERMISSION_END_DATE)
    private Long endDate;


    public PermissionModel(int id, String comment, int fkPermissionType, int fkPermissionStatus, Long startDate, Long endDate) {
        this.id = id;
        this.comment = comment;
        this.fkPermissionType = fkPermissionType;
        this.fkPermissionStatus = fkPermissionStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public PermissionModel(String comment, int fkPermissionType, int fkPermissionStatus, Long startDate, Long endDate) {
        this.id = id;
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

