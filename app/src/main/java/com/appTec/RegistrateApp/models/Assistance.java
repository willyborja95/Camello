package com.appTec.RegistrateApp.models;

import java.io.Serializable;
import java.util.Calendar;

public class Assistance implements Serializable {

    private String evento;
    private Calendar fecha;

    public Assistance(String evento, Calendar fecha) {
        this.evento = evento;
        this.fecha = fecha;
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
