package com.proyecto.geobus.models;

import java.io.Serializable;

/**
 * Created by alexis on 19/03/17.
 */

public class ClienteDTO implements Serializable {
    private String id;
    private String descripcion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
