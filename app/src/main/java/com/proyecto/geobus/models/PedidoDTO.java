package com.proyecto.geobus.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexis on 19/03/17.
 */

public class PedidoDTO implements Serializable {
    private String id;
    @SerializedName("cliente")
    private String idCliente;
    private Date fecha;
    @SerializedName("productos")
    private List<LineaPedidoDTO> pedidos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<LineaPedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<LineaPedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public void agregarPedido(LineaPedidoDTO lPedido) {
        if (pedidos == null) {
            pedidos = new ArrayList<LineaPedidoDTO>();
        }
        pedidos.add(lPedido);
    }

    public void eliminarPedidos() {
        if (pedidos == null) {
            pedidos = new ArrayList<LineaPedidoDTO>();
        }
        pedidos.clear();
    }
}
