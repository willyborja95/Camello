package com.appTec.RegistrateApp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private int id;
    private String name;
    private String lastName;
    private String email;
    private Company company;
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
}
