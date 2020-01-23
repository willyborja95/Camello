package com.appTec.RegistrateApp.models;

import java.io.Serializable;

public class Device implements Serializable {
    private int id;
    private String nombre;
    private String modelo;
    private String imei;
    private boolean status;

    public Device() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Device(String nombre, String modelo) {
        this.nombre = nombre;
        this.modelo = modelo;
    }

    public Device(String nombre, String modelo, String imei) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.imei = imei;
    }

    public Device(int id, String nombre, String modelo, String imei, boolean status) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.imei = imei;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
