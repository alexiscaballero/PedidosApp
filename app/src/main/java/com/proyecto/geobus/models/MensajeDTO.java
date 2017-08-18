package com.proyecto.geobus.models;

/**
 * Created by alexis on 23/03/17.
 */

public class MensajeDTO {
    private int estado;
    private String mensaje;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
