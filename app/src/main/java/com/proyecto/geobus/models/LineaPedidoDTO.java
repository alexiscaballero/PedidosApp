package com.proyecto.geobus.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by alexis on 19/03/17.
 */

public class LineaPedidoDTO implements Serializable {
    private int id;
    @SerializedName("codigo")
    private String idProducto;
    @SerializedName("cantidad")
    private float cantidad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object obj) {
        LineaPedidoDTO linea = (LineaPedidoDTO) obj;
        if (linea.getCantidad() == this.getCantidad() && linea.getIdProducto().equals(this.getIdProducto())) {
            return true;
        } else {
            return false;
        }
    }
}
