package com.apptec.camello.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.apptec.camello.repository.localdatabase.DBConstants;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = DBConstants.WORK_ZONE_TABLE)
public class WorkZoneModel {

    @ColumnInfo(name = DBConstants.WORK_ZONE_PK)
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = DBConstants.WORK_ZONE_NAME)
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = DBConstants.WORK_ZONE_LATITUDE)
    @SerializedName("lat")
    private String latitude;

    @ColumnInfo(name = DBConstants.WORK_ZONE_LONGITUDE)
    @SerializedName("lng")
    private String longitude;

    @ColumnInfo(name = DBConstants.WORK_ZONE_RADIUS)
    @SerializedName("radius")
    private String radius;


    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getRadius() {
        return radius;
    }

    public float getRadiusAsFloat() {
        return Float.parseFloat(this.radius);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Double getLatitudeAsDouble() {
        return Double.parseDouble(this.latitude);
    }

    public Double getLongitudeAsDouble() {
        return Double.parseDouble(this.longitude);
    }


    @Override
    public String toString() {
        return
                "WorkzonesItem{" +
                        "lng = '" + longitude + '\'' +
                        ",name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",radius = '" + radius + '\'' +
                        ",lat = '" + latitude + '\'' +
                        "}";
    }
}