package com.proyecto.geobus.models;

import com.google.gson.annotations.SerializedName;
import com.proyecto.geobus.api.PedidosAPI;

import java.util.Date;
import java.util.List;

/**
 * Created by alexis on 19/03/17.
 */


public class RequestSavePedidosDTO {

    //@SerializedName("device_id")
    //private String idDevice;
    //@SerializedName("action")
    //private String action;
    @SerializedName("fecha")
    private Date fecha;
    @SerializedName("pedidos")
    private List<PedidoDTO> pedidos;
    //private String pedidos;

    //public String getAction() {
    //    return action;
    //}
//
    //public void setAction(String action) {
    //    this.action = action;
    //}
//
    //public String getIdDevice() {
    //    return idDevice;
    //}
//
    //public void setIdDevice(String idDevice) {
    //    this.idDevice = idDevice;
    //}

    //public List<PedidoDTO> getPedidos() {
    //    return pedidos;
    //}

    public List<PedidoDTO> getPedidos() {
        return pedidos;
    }

    //public void setPedidos(List<PedidoDTO> pedidos) {
    //    this.pedidos = pedidos;
    //}

    public void setPedidos(List<PedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
