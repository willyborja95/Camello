package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.util.Date;

@Entity(tableName = DBConstants.WORKING_PERIOD_TABLE)
public class WorkingPeriodModel {

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_PK)
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_START_DATE)
    private Date start_date;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_END_DATE)
    private Date end_date;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_STATUS)
    private int status;                 // 1 = Started   2 = Finished  <0 = Canceled

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_WORK_ZONE_FK)
    private int workZoneId;

    // Constructors
    @Ignore
    public WorkingPeriodModel(int id, int workZoneId, Date start_date, Date end_date, int status) {
        this.id = id;
        this.workZoneId = workZoneId;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public WorkingPeriodModel() {

    }

    // Setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkZoneId() {
        return workZoneId;
    }

    public void setWorkZoneId(int workZoneId) {
        this.workZoneId = workZoneId;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
