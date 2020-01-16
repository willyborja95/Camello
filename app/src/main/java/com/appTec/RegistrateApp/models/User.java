package com.appTec.RegistrateApp.models;

import java.util.ArrayList;

public class User {
    private String names;
    private String lastnames;
    private String email;
    private Company company;
    private ArrayList<WorkingPeriod> workingPeriodList;

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastnames() {
        return lastnames;
    }

    public void setLastnames(String lastnames) {
        this.lastnames = lastnames;
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
