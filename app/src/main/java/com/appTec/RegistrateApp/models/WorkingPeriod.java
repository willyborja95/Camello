package com.appTec.RegistrateApp.models;

import androidx.room.Entity;

import java.util.Calendar;

public class WorkingPeriod {
    private Calendar start;
    private Calendar end;
    private int day;

    public WorkingPeriod(Calendar start, Calendar end, int day) {
        this.start = start;
        this.end = end;
        this.day = day;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
