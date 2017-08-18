package com.proyecto.geobus.models;

import java.io.Serializable;

/**
 * Created by alexis on 28/03/17.
 */

public class ConfiguracionDTO implements Serializable {
    private String host = "";
    private String clientes = "";
    private String pedidos = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientes() {
        return clientes;
    }

    public void setClientes(String clientes) {
        this.clientes = clientes;
    }

    public String getPedidos() {
        return pedidos;
    }

    public void setPedidos(String pedidos) {
        this.pedidos = pedidos;
    }
}
