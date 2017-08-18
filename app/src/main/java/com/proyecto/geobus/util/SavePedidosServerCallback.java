package com.proyecto.geobus.util;

import com.proyecto.geobus.models.MensajeDTO;

/**
 * Created by alexis on 19/03/17.
 */

public interface SavePedidosServerCallback {
    void onSuccess(MensajeDTO mensaje);
}
