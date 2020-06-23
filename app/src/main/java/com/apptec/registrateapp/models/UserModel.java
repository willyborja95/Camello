package com.apptec.registrateapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.apptec.registrateapp.repository.localdatabase.DBConstants;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = DBConstants.USER_TABLE)
public class UserModel implements Serializable {

    @ColumnInfo(name = DBConstants.USER_PK)
    @PrimaryKey
    private int id;

    @ColumnInfo(name = DBConstants.USER_NAME)
    private String name;

    @ColumnInfo(name = DBConstants.USER_LAST_NAME)
    private String lastName;

    @ColumnInfo(name = DBConstants.USER_EMAIL)
    private String email;

    @Embedded
    public CompanyModel company;


    @Ignore
    private ArrayList<WorkingPeriodModel> workingPeriodList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CompanyModel getCompany() {
        return company;
    }

    public void setCompany(CompanyModel company) {
        this.company = company;
    }

    public ArrayList<WorkingPeriodModel> getWorkingPeriodList() {
        return workingPeriodList;
    }

    public void setWorkingPeriodList(ArrayList<WorkingPeriodModel> workingPeriodList) {
        this.workingPeriodList = workingPeriodList;
    }

    @Ignore
    public String getFullName() {
        return this.name + " " + this.lastName;
    }
}
