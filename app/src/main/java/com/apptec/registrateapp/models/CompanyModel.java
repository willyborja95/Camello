package com.apptec.registrateapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = DBConstants.COMPANY_TABLE)
public class CompanyModel implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = DBConstants.COMPANY_PK)
    private String companyName;


    @Ignore
    private ArrayList<WorkZoneModel> workZones;



    // Getter and setter
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ArrayList<WorkZoneModel> getWorkZones() {
        return workZones;
    }

    public void setWorkZones(ArrayList<WorkZoneModel> workZones) {
        this.workZones = workZones;
    }
}
