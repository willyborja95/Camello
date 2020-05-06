package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class WorkingPeriod {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "work_zone")
    private int workzoneId;

    private Date start_date;

    private Date end_date;

    private int status;                 // 1 = Started   2 = Finished  <0 = Canceled

    // Constructor
    public WorkingPeriod(int id, int workzoneId, Date start_date, Date end_date, int status) {
        this.id = id;
        this.workzoneId = workzoneId;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    // Setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkzoneId() {
        return workzoneId;
    }

    public void setWorkzoneId(int workzoneId) {
        this.workzoneId = workzoneId;
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
