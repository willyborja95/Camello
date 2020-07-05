package com.apptec.camello.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;

@Entity(
        tableName = DBConstants.WORKING_PERIOD_TABLE,
        foreignKeys = {
                @ForeignKey(entity = WorkZoneModel.class,
                        parentColumns = DBConstants.WORK_ZONE_PK,
                        childColumns = DBConstants.WORKING_PERIOD_WORK_ZONE_FK,
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = DBConstants.WORKING_PERIOD_WORK_ZONE_FK),
        }
)
public class WorkingPeriodModel {

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_PK)
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_START_DATE)
    private Long start_date;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_END_DATE)
    private Long end_date;

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_STATUS)
    private int status;                 // 1 = Started   2 = Finished  <0 = Canceled

    @ColumnInfo(name = DBConstants.WORKING_PERIOD_WORK_ZONE_FK)
    private int workZoneId;

    // Constructors
    @Ignore
    public WorkingPeriodModel(int id, Long start_date, Long end_date, int status, int workZoneId) {
        this.id = id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.workZoneId = workZoneId;
    }

    public WorkingPeriodModel(Long start_date, int status, int workZoneId) {
        this.start_date = start_date;
        this.status = status;
        this.workZoneId = workZoneId;
    }

    @Deprecated
    public WorkingPeriodModel() {

    }


    // Setters and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getStart_date() {
        return start_date;
    }

    public void setStart_date(Long start_date) {
        this.start_date = start_date;
    }

    public Long getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Long end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWorkZoneId() {
        return workZoneId;
    }

    public void setWorkZoneId(int workZoneId) {
        this.workZoneId = workZoneId;
    }

    // To string

    @Override
    public String toString() {
        return "WorkingPeriodModel{" +
                "id=" + id +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", status=" + status +
                ", workZoneId=" + workZoneId +
                '}';
    }
}
