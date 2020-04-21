package com.appTec.RegistrateApp.models;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class User implements Serializable {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "email")
    private String email;

    @Embedded public Company company;


    @Ignore
    private ArrayList<WorkingPeriod> workingPeriodList;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ArrayList<WorkingPeriod> getWorkingPeriodList() {
        return workingPeriodList;
    }

    public void setWorkingPeriodList(ArrayList<WorkingPeriod> workingPeriodList) {
        this.workingPeriodList = workingPeriodList;
    }

    public Object getFullName() {
        return this.name + " " + this.lastName;
    }
}
