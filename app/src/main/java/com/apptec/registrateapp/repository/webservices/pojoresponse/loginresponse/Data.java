package com.apptec.registrateapp.repository.webservices.pojoresponse.loginresponse;

import androidx.annotation.Nullable;

import com.apptec.registrateapp.models.DeviceModel;
import com.apptec.registrateapp.models.WorkZoneModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data{

    @SerializedName("id")
    private int id;

	@SerializedName("name")
	private String name;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("workzones")
    private List<WorkZoneModel> workzones;

    @SerializedName("device")
    @Nullable
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
 	public String toString(){
		return 
			"Data{" + 
			"name = '" + name + '\'' + 
			",tokens = '" + tokens + '\'' + 
			",id = '" + id + '\'' + 
			",workzones = '" + workzones + '\'' + 
			",lastname = '" + lastname + '\'' + 
			"}";
		}
}