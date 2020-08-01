package com.apptec.camello.mainactivity.fpermission;

import com.apptec.camello.models.PermissionModel;
import com.apptec.camello.repository.localdatabase.converter.DateConverter;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Permission Data Transfer Object
 * <p>
 * This class will help us to transfer Permissions to the server and receive from them
 */
public class PermissionDto {

    private static final String DATE_INCOMING_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DATE_OUT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SerializedName("id")
    private int id;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    @SerializedName("type")
    private int type;

    @SerializedName("comment")
    private String comment;

    @SerializedName("status")
    private int status;


    /**
     * This construct is used when the data come from the server
     */
    public PermissionDto(int id, String startDate, String endDate, int type, String comment, int fkPermissionStatus) {

        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.comment = comment;
        this.status = fkPermissionStatus;
    }

    /**
     * This construct is user locally
     */
    public PermissionDto(String startDate, String endDate, int type) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    /**
     * This construct automatic will transform a Permission model to a PermissionDto
     */
    public PermissionDto(PermissionModel permissionModel) {
        this.id = permissionModel.getId();
        this.startDate = DateConverter.toStringDateFormat(permissionModel.getStartDate(), DATE_OUT_FORMAT);
        this.endDate = DateConverter.toStringDateFormat(permissionModel.getEndDate(), DATE_OUT_FORMAT);
        this.type = permissionModel.getFkPermissionType();
        this.comment = permissionModel.getComment();

    }

    /**
     * Instance an object from permissionModel using this same object
     */
    public PermissionModel getAsPermissionModel() {
        PermissionModel permissionModel = new PermissionModel(id, comment, getTypeId(), getStatusId(), getStartDateLong(), getEndDateLong());
        return permissionModel;
    }

    /**
     * @return the id of the permission Type
     */
    public int getTypeId() {
        return this.type;
    }

    /**
     * @return the id of the permission status
     */
    public int getStatusId() {
        return this.status;
    }

    /**
     * Return this start data string as a long
     */
    public Long getStartDateLong() {
        return toTimestampUsingCustomFormat(this.startDate, PermissionDto.DATE_INCOMING_FORMAT);

    }

    /**
     * Return this end data string as a long
     */
    public Long getEndDateLong() {
        return toTimestampUsingCustomFormat(this.endDate, PermissionDto.DATE_INCOMING_FORMAT);
    }


    // Converter the incoming format to long ()is can be avoid if the backend return always a Long
    private Long toTimestampUsingCustomFormat(String stringDate, String custom_format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(custom_format);
            Date date = dateFormat.parse(stringDate);
            return DateConverter.toTimestamp(date);
        } catch (ParseException p) {
            Timber.e(p);
            return null;
        }
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "PermissionDto{" +
                "id=" + id +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                ", fkPermissionStatus=" + status +
                '}';
    }
}

