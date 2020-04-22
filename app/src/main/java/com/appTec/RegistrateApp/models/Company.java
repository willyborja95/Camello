package com.appTec.RegistrateApp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company implements Serializable {

    @PrimaryKey()
    @NonNull
    private String companyName;


    @Ignore
    private ArrayList<WorkzonesItem> workZones;



    // Getter and setter
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<WorkzonesItem> getWorkZones() {
        return workZones;
    }

    public void setWorkZones(ArrayList<WorkzonesItem> workZones) {
        this.workZones = workZones;
    }
}
