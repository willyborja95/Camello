package com.apptec.registrateapp.mainactivity.fpermission;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;
import com.google.gson.annotations.SerializedName;

public class PermissionDto {
    /**
     * Permission Data Transfer Object
     * <p>
     * This class will help us to transfer Permissions to the server and receive from them
     */
    private static final String TAG = "PermissionDto";

    @SerializedName("id")
    public int id;

    @SerializedName("employeeId")
    public int employeeId;

    @SerializedName("startDate")
    public String formattedStartDate;

    @SerializedName("endDate")
    public String formattedEndDate;

    @SerializedName("type")
    public int fkPermissionType;

    @SerializedName("comment")
    public String comment;

    @SerializedName("status")
    public int fkPermissionStatus;


    public PermissionDto(int id, int employeeId, String formattedStartDate, String formattedEndDate, int fkPermissionType, String comment, int fkPermissionStatus) {
        /**
         * THis construct is used when the data come from the server
         */
        this.id = id;
        this.employeeId = employeeId;
        this.formattedStartDate = formattedStartDate;
        this.formattedEndDate = formattedEndDate;
        this.fkPermissionType = fkPermissionType;
        this.comment = comment;
        this.fkPermissionStatus = fkPermissionStatus;
    }

    public PermissionDto(String formattedStartDate, String formattedEndDate, int fkPermissionType) {
        /**
         * THis construct is user locally
         */
        this.formattedStartDate = formattedStartDate;
        this.formattedEndDate = formattedEndDate;
        this.fkPermissionType = fkPermissionType;
    }

    public PermissionDto(PermissionModel permissionModel) {
        /**
         * This construct automatic will transform a Permission model to a PermissionDto
         */
        this.id = permissionModel.getId();
        this.formattedStartDate = DateConverter.toStringDateFormat(permissionModel.getStartDate());
        this.formattedEndDate = DateConverter.toStringDateFormat(permissionModel.getEndDate());
        this.fkPermissionType = permissionModel.getFkPermissionType();
        this.comment = permissionModel.getComment();

    }


    // Getter and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFormattedStartDate() {
        return formattedStartDate;
    }

    public void setFormattedStartDate(String formattedStartDate) {
        this.formattedStartDate = formattedStartDate;
    }

    public String getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setFormattedEndDate(String formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    public int getFkPermissionType() {
        return fkPermissionType;
    }

    public void setFkPermissionType(int fkPermissionType) {
        this.fkPermissionType = fkPermissionType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getFkPermissionStatus() {
        return fkPermissionStatus;
    }

    public void setFkPermissionStatus(int fkPermissionStatus) {
        this.fkPermissionStatus = fkPermissionStatus;
    }

    @Override
    public String toString() {
        return "PermissionDto{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", formattedStartDate='" + formattedStartDate + '\'' +
                ", formattedEndDate='" + formattedEndDate + '\'' +
                ", fkPermissionType=" + fkPermissionType +
                ", comment='" + comment + '\'' +
                ", fkPermissionStatus=" + fkPermissionStatus +
                '}';
    }
}

