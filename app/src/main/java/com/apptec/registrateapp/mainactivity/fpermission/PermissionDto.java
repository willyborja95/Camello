package com.apptec.registrateapp.mainactivity.fpermission;

import com.apptec.registrateapp.models.PermissionModel;
import com.apptec.registrateapp.repository.localdatabase.converter.DateConverter;

public class PermissionDto {
    /**
     * Permission Data Transfer Object
     * <p>
     * This class will help us to transfer Permissions to the server and receive from them
     */


    private int id;


    private String startDate;


    private String endDate;


    private int type;


    private String comment;


    private int fkPermissionStatus;


    public PermissionDto(int id, String startDate, String endDate, int type, String comment, int fkPermissionStatus) {
        /**
         * THis construct is used when the data come from the server
         */
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.comment = comment;
        this.fkPermissionStatus = fkPermissionStatus;
    }

    public PermissionDto(String startDate, String endDate, int type) {
        /**
         * THis construct is user locally
         */
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public PermissionDto(PermissionModel permissionModel) {
        /**
         * This construct automatic will transform a Permission model to a PermissionDto
         */
        this.id = permissionModel.getId();
        this.startDate = DateConverter.toStringDateFormat(permissionModel.getStartDate());
        this.endDate = DateConverter.toStringDateFormat(permissionModel.getEndDate());
        this.type = permissionModel.getFkPermissionType();
        this.comment = permissionModel.getComment();

    }


    // Getter and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                ", fkPermissionStatus=" + fkPermissionStatus +
                '}';
    }
}

