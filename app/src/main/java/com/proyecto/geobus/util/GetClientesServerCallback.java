package com.proyecto.geobus.util;

import com.proyecto.geobus.models.ClienteDTO;

import java.util.List;

/**
 * Created by alexis on 19/03/17.
 */

public interface GetClientesServerCallback {
    void onSuccess(List<ClienteDTO> list);
}
