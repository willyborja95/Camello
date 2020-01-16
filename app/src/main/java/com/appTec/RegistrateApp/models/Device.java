package com.appTec.RegistrateApp.models;

public class Device {
    private int id;
    private String nombre;
    private String modelo;
    private String imei;

    public Device(String nombre, String modelo, String imei) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.imei = imei;
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
