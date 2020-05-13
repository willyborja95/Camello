package com.apptec.registrateapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class WorkZoneModel {

    @PrimaryKey
    private int id;

	@SerializedName("name")
	private String name;

	@SerializedName("lat")
	private String lat;

    @SerializedName("lng")
    private String lng;

    @SerializedName("radius")
    private String radius;



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