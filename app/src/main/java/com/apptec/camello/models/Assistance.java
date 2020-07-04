package com.apptec.camello.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

public class Assistance implements Serializable {

    @SerializedName("dispositivoid")
    private int deviceId;
    @SerializedName("latitud")
    private double latitude;
    @SerializedName("longitud")
    private double longitude;
    @SerializedName("hora")
    private String time;
    private String evento;
    private Calendar fecha;

    public Assistance(String evento, Calendar fecha) {
        this.evento = evento;
        this.fecha = fecha;
    }

    public Assistance(int deviceId, double latitude, double longitude){
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Assistance(int deviceId, double latitude, double longitude, String time) {
        this.deviceId = deviceId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Calendar getFecha() {
        return fecha;
    }

    public void setFecha(Calendar fecha) {
        this.fecha = fecha;
    }
}
