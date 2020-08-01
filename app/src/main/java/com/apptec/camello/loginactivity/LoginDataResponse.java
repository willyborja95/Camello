package com.apptec.camello.loginactivity;

import androidx.annotation.Nullable;

import com.apptec.camello.models.DeviceModel;
import com.apptec.camello.models.WorkZoneModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Login data that will match the response of database
 */
public class LoginDataResponse {

    @SerializedName("employeeId")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("workzones")
    private List<WorkZoneModel> workzones;

    @SerializedName("device")
    private DeviceModel device;

    @SerializedName("tokens")
    private Tokens tokens;

    @SerializedName("enterprise")
    private String enterprise;


    // Setter and getter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setTokens(Tokens tokens) {
        this.tokens = tokens;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<WorkZoneModel> getWorkzones() {
        return workzones;
    }

    public void setWorkzones(List<WorkZoneModel> workzones) {
        this.workzones = workzones;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Nullable
    public DeviceModel getDevice() {
        return device;
    }

    public void setDevice(@Nullable DeviceModel device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", workzones=" + workzones +
                ", device=" + device +
                ", tokens=" + tokens +
                ", enterprise='" + enterprise + '\'' +
                '}';
    }
}
