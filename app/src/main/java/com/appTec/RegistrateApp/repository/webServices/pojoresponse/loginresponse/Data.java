package com.appTec.RegistrateApp.repository.webServices.pojoresponse.loginresponse;

import java.util.List;

import com.appTec.RegistrateApp.models.WorkzonesItem;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("name")
	private String name;

	@SerializedName("tokens")
	private Tokens tokens;

	@SerializedName("enterprise")
	private String enterprise;

	@SerializedName("id")
	private int id;

	@SerializedName("workzones")
	private List<WorkzonesItem> workzones;

	@SerializedName("lastname")
	private String lastname;


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

	public List<WorkzonesItem> getWorkzones() {
		return workzones;
	}

	public void setWorkzones(List<WorkzonesItem> workzones) {
		this.workzones = workzones;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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