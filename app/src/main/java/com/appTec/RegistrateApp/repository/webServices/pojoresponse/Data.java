package com.appTec.RegistrateApp.repository.webServices.pojoresponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("name")
	private String name;

	@SerializedName("tokens")
	private Tokens tokens;

	@SerializedName("id")
	private int id;

	@SerializedName("workzones")
	private List<WorkzonesItem> workzones;

	@SerializedName("lastname")
	private String lastname;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setTokens(Tokens tokens){
		this.tokens = tokens;
	}

	public Tokens getTokens(){
		return tokens;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setWorkzones(List<WorkzonesItem> workzones){
		this.workzones = workzones;
	}

	public List<WorkzonesItem> getWorkzones(){
		return workzones;
	}

	public void setLastname(String lastname){
		this.lastname = lastname;
	}

	public String getLastname(){
		return lastname;
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