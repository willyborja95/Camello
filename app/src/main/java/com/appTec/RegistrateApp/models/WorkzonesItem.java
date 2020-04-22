package com.appTec.RegistrateApp.models;

import com.google.gson.annotations.SerializedName;

public class WorkzonesItem{

	@SerializedName("lng")
	private String lng;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("radius")
	private String radius;

	@SerializedName("lat")
	private String lat;

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRadius(String radius){
		this.radius = radius;
	}

	public String getRadius(){
		return radius;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"WorkzonesItem{" + 
			"lng = '" + lng + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",radius = '" + radius + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}