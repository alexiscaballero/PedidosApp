package com.proyecto.geobus.api;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.proyecto.geobus.R;
import com.proyecto.geobus.models.ClienteDTO;
import com.proyecto.geobus.models.ConfiguracionDTO;
import com.proyecto.geobus.models.MensajeDTO;
import com.proyecto.geobus.models.PedidoDTO;
import com.proyecto.geobus.models.RequestClientesDTO;
import com.proyecto.geobus.models.RequestSavePedidosDTO;
import com.proyecto.geobus.util.GetClientesServerCallback;
import com.proyecto.geobus.util.InternalStorage;
import com.proyecto.geobus.util.Misc;
import com.proyecto.geobus.util.SavePedidosServerCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexis on 19/03/17.
 */

public class PedidosAPI {

    private String readSavePedidosKey = "PEDIDOS";
    private String readSaveClientesKey = "CLIENTES";
    private String readSaveConfiguracionKey = "CONFIGURACION";
    private static PedidosAPI mInstance;
    private List<PedidoDTO> listaPedidos = new ArrayList<PedidoDTO>();
    private List<ClienteDTO> listaClientes = new ArrayList<ClienteDTO>();
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private ConfiguracionDTO config = new ConfiguracionDTO();

    private PedidosAPI(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        if (!cargarConfiguracion()) {
            setConfigHost(mCtx.getResources().getString(R.string.host));
            setConfigClientes(mCtx.getResources().getString(R.string.getClientesEndPoint));
            setConfigPedidos(mCtx.getResources().getString(R.string.savePedidosEndPoint));
        };
        cargarPedidos();
        cargarClientes();
    }

    public static synchronized PedidosAPI getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PedidosAPI(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void getClientes(final Context context, final GetClientesServerCallback callback) {
        Gson gson = new Gson();
        RequestClientesDTO request = new RequestClientesDTO();
        String devID = getDeviceID();
        request.setId(Misc.md5(devID));
        //request.setId("f93e710e324a3e6b2eb424e76d19bea1");
        request.setAction(getConfig().getClientes());

        String jsonString = gson.toJson(request);
        try {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, String.format("%s%s", getConfig().getHost() + getConfig().getClientes() + Misc.md5(devID), ""), new JSONObject(jsonString), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Boolean success = false;
                            String estado = "1";
                            String mensaje = "";
                            try {
                                estado = response.getString("estado");
                                mensaje = response.getString("mensaje");
                                if (estado.equals("1")) {
                                    success = true;
                                } else {
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                //Mostrar error generico
                                Toast.makeText(context, "Se produjo un error inesperado.", Toast.LENGTH_SHORT).show();
                                estado = "0";
                                success = false;
                            }
                            if (success) {
                                try {
                                    JsonParser parser = new JsonParser();
                                    JsonElement tradeElement = null;
                                    tradeElement = parser.parse(response.getString("clientes"));
                                    listaClientes.clear();
                                    JsonArray trade = tradeElement.getAsJsonArray();
                                    for(int i=0;i<trade.size();i++){
                                        JsonObject jsonObject= trade.get(i).getAsJsonObject();
                                        ClienteDTO cliente = new ClienteDTO();
                                        cliente.setId(jsonObject.get("id").getAsString());
                                        cliente.setDescripcion(jsonObject.get("nombre").getAsString());
                                        listaClientes.add(cliente);
                                    }
                                    if (listaClientes.isEmpty()) {
                                        cargarClientes();
                                    }
                                    Toast.makeText(context, "Se actualizó la lista de clientes.", Toast.LENGTH_SHORT).show();
                                    callback.onSuccess(listaClientes);
                                } catch (JSONException e) {
                                    success = false;
                                    Toast.makeText(context, "Se produjo un error inesperado.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //Toast.makeText(context, "Se produjo un error inesperado.", Toast.LENGTH_LONG).show();
                                List<ClienteDTO> list = new ArrayList<ClienteDTO>();
                                callback.onSuccess(list);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Si el servidor no esta disponible, intentar cargar los clientes del disco, si los hay
                            if (cargarClientes()) {
                                callback.onSuccess(listaClientes);
                            } else {
                                List<ClienteDTO> list = new ArrayList<ClienteDTO>();
                                callback.onSuccess(list);
                            }
                        }
                    });
            addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
            callback.onSuccess(null);
        }
    }

    public ConfiguracionDTO getConfig() {
        return this.config;
    }

    public boolean cargarConfiguracion() {
        try {
            // Retrieve the list from internal storage
            ConfiguracionDTO cfg = (ConfiguracionDTO) InternalStorage.readObject(mCtx, readSaveConfiguracionKey);
            config = cfg;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cargarPedidos() {
        try {
            // Retrieve the list from internal storage
            List<PedidoDTO> cachedPedidos = (List<PedidoDTO>) InternalStorage.readObject(mCtx, readSavePedidosKey);
            // Display the items from the list retrieved.
            listaPedidos.clear();
            for (PedidoDTO pedido : cachedPedidos) {
                listaPedidos.add(pedido);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cargarClientes() {
        try {
            // Retrieve the list from internal storage
            List<ClienteDTO> cachedClientes = (List<ClienteDTO>) InternalStorage.readObject(mCtx, readSaveClientesKey);
            // Display the items from the list retrieved.
            listaClientes.clear();
            for (ClienteDTO cliente : cachedClientes) {
                listaClientes.add(cliente);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public List<PedidoDTO> getPedidos() {
        return listaPedidos;
    }

    public boolean addPedido(PedidoDTO pedido) {
        //Chequear si ya existe, si existe reemplazarlo, sino agregarlo
        boolean existe = false;
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (listaPedidos.get(i).getId().equals(pedido.getId())) {
                listaPedidos.remove(i);
                listaPedidos.add(pedido);
                existe = true;
                break;
            }
        }
        if (!existe) {
            listaPedidos.add(pedido);
        }
        return savePedidos();
    }

    public boolean eliminarPedido(String idPedido) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (listaPedidos.get(i).getId().equals(idPedido)) {
                listaPedidos.remove(i);
                break;
            }
        }
        return savePedidos();
    }

    public boolean saveConfiguracion() {
        try {
            InternalStorage.writeObject(mCtx, readSaveConfiguracionKey, config);
            cargarConfiguracion();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean setConfigHost(String ht) {
        this.config.setHost(ht);
        return saveConfiguracion();
    }

    public boolean setConfigClientes(String cl) {
        this.config.setClientes(cl);
        return saveConfiguracion();
    }

    public boolean setConfigPedidos(String pd) {
        this.config.setPedidos(pd);
        return saveConfiguracion();
    }

    public boolean savePedidos() {
        try {
            InternalStorage.writeObject(mCtx, readSavePedidosKey, listaPedidos);
            cargarPedidos();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public boolean saveClientes() {
        try {
            InternalStorage.writeObject(mCtx, readSaveClientesKey, listaClientes);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void sendPedidos (final SavePedidosServerCallback callback) {
        Gson gson = new Gson();
        RequestSavePedidosDTO request = new RequestSavePedidosDTO();
        String devID = getDeviceID();
        //request.setIdDevice(Misc.md5(devID));
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        request.setFecha(date);
        //String json = new Gson().toJson(listaPedidos);
        request.setPedidos(listaPedidos);
        //request.setAction(getConfig().getPedidos());
        //String jsonString = gson.toJson(request).replace("\\", "");
        String jsonString = gson.toJson(request);
        try {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, String.format("%s%s", getConfig().getHost() + getConfig().getPedidos() + Misc.md5(devID), ""), new JSONObject(jsonString), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //chequear si tuvo exito o no
                            Boolean success = false;
                            String estado = "1";
                            String mensaje = "";
                            try {
                                estado = response.getString("estado");
                                mensaje = response.getString("mensaje");
                                if (estado.equals("1")) {
                                    success = true;
                                }
                            } catch (JSONException e) {
                                //Mostrar error generico
                                MensajeDTO msj = new MensajeDTO();
                                msj.setEstado(0);
                                msj.setMensaje("Se produjo un error al enviar los pedidos, inténtelo nuevamente por favor.");
                                callback.onSuccess(msj);
                                estado = "0";
                                success = false;
                            }
                            if (success) {
                                //Limpiar pedidos realizados
                                limpiarPedidos();
                                MensajeDTO msj = new MensajeDTO();
                                msj.setEstado(1);
                                msj.setMensaje("Éxito al enviar los pedidos.");
                                callback.onSuccess(msj);
                            } else {
                                //Mostrar mensaje de error (mensaje)
                                MensajeDTO msj = new MensajeDTO();
                                msj.setEstado(0);
                                msj.setMensaje(mensaje);
                                callback.onSuccess(msj);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Mostrar error de conexion, o de formato del json
                            MensajeDTO msj = new MensajeDTO();
                            msj.setEstado(0);
                            msj.setMensaje("No hay conexión con el servidor. Verifique su conexión a internet.");
                            callback.onSuccess(msj);
                        }
                    });
            addToRequestQueue(jsObjRequest);
        } catch (JSONException e) {
            //Mostrar error generico
            MensajeDTO msj = new MensajeDTO();
            msj.setEstado(0);
            msj.setMensaje("Se produjo un error al enviar los pedidos, inténtelo nuevamente por favor.");
            callback.onSuccess(msj);
            callback.onSuccess(msj);
        }
    }

    public PedidoDTO getPedido(int idPedido) {
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (listaPedidos.get(i).getId().equals(idPedido)) {
                return listaPedidos.get(i);
            }
        }
        return null;
    }

    public boolean limpiarPedidos() {
        listaPedidos.clear();
        savePedidos();
        return cargarPedidos();
    }

    public String getDeviceID() {
        final TelephonyManager tm = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(mCtx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        //UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        //String deviceId = deviceUuid.toString();
        return tmDevice;
    }

    public String getClienteNombre(String idCliente) {
        String nombre = "Desconocido.";
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getId().equals(idCliente)) {
                nombre = listaClientes.get(i).getDescripcion();
            }
        }
        return nombre;
    }
}
