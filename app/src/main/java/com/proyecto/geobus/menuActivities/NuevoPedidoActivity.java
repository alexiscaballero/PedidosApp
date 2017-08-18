package com.proyecto.geobus.menuActivities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.geobus.NuevaLineaDialogFragment;
import com.proyecto.geobus.R;
import com.proyecto.geobus.api.PedidosAPI;
import com.proyecto.geobus.models.ClienteDTO;
import com.proyecto.geobus.models.LineaPedidoDTO;
import com.proyecto.geobus.models.PedidoDTO;
import com.proyecto.geobus.util.GetClientesServerCallback;
import com.proyecto.geobus.util.LineaPedidoAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by alexis on 19/03/17.
 */
public class NuevoPedidoActivity extends AppCompatActivity {
    private Spinner spinnerClientes;
    private ArrayAdapter<String> adapter;
    private ArrayList<ClienteDTO> items;
    private ArrayList<String> descripcionesItems;
    private boolean edicion = false;
    private PedidoDTO pedidoEditar;
    private ProgressDialog progressDialog = null;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout;
    private NuevoPedidoActivity mIntancia = null;
    private LineaPedidoAdapter adbLineasPedido;
    private ArrayList<LineaPedidoDTO> listaLineaPedidos = new ArrayList<LineaPedidoDTO>();
    private ListView listview;
    private LineaPedidoDTO lineaPedidoSeleccionada;
    private boolean nuevaLineaPedido;
    private int posicionCliente = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_nuevo_pedido);
        items = new ArrayList<ClienteDTO>();
        descripcionesItems = new ArrayList<String>();
        spinnerClientes = (Spinner) findViewById(R.id.fClientes);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, descripcionesItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerClientes.setAdapter(adapter);
        mIntancia = this;

        listview = (ListView) findViewById(R.id.listView1);

        adbLineasPedido= new LineaPedidoAdapter(this, 0, listaLineaPedidos);
        listview.setAdapter(adbLineasPedido);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarLineaPedido(listaLineaPedidos.get(position));
            }
        });

        Button registrar = (Button) findViewById(R.id.bt_registrar_pedido);
        if (getIntent().getExtras()!=null) {
            PedidoDTO pedido = (PedidoDTO) getIntent().getSerializableExtra("Pedido");
            registrar.setText("Guardar Cambios");
            pedidoEditar = pedido;
            edicion = true;
            for (int i = 0; i < pedido.getPedidos().size(); ++i) {
                listaLineaPedidos.add(pedido.getPedidos().get(i));
            }
            adbLineasPedido.notifyDataSetChanged();
        }
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (esPedidoValido()) {
                    PedidoDTO pedido= new PedidoDTO();
                    String idCliente = items.get(spinnerClientes.getSelectedItemPosition()).getId();
                    if (!edicion) {
                        String uniqueID = UUID.randomUUID().toString();
                        pedido.setId(uniqueID);
                    } else {
                        pedido.setId(pedidoEditar.getId());
                    }
                    pedido.setIdCliente(idCliente);
                    pedido.setPedidos(listaLineaPedidos);
                    //Guardar pedido
                    PedidosAPI.getInstance(getApplicationContext()).addPedido(pedido);
                    mIntancia.finish();
                }
                else {
                    String mensajeError;
                    boolean clienteVacio = false;
                    if (spinnerClientes.getSelectedItem() == null) {
                        clienteVacio = true;
                    }
                    if (listaLineaPedidos.isEmpty() && (clienteVacio)) {
                        mensajeError = "Seleccione un cliente e ingrese al menos una línea de pedido";
                    } else if (listaLineaPedidos.isEmpty()) {
                        mensajeError = "Ingrese al menos una línea de pedido";
                    } else {
                        mensajeError = "Seleccione un cliente";
                    }
                    Toast.makeText(getApplicationContext(), mensajeError, Toast.LENGTH_LONG).show();
                }
            }
        });
        progressDialog = ProgressDialog.show(NuevoPedidoActivity.this, "", "Cargando Datos..", true);
        cargarClientes();
        Button mButton = (Button) findViewById(R.id.bt_agregar_linea);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaLineaPedido = true;
                FragmentManager fm = getFragmentManager();
                NuevaLineaDialogFragment dialogFragment = new NuevaLineaDialogFragment();
                dialogFragment.show(fm, "Nueva Línea de Pedido");
            }
        });
        Button mButton2 = (Button) findViewById(R.id.button_actualizar_clientes);
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarClientes();
            }
        });
    }


    public void mostrarLineaPedido(LineaPedidoDTO linea) {
        nuevaLineaPedido = false;
        lineaPedidoSeleccionada = linea;
        FragmentManager fm = getFragmentManager();
        NuevaLineaDialogFragment dialogFragment = new NuevaLineaDialogFragment();
        dialogFragment.show(fm, "Editar Línea de Pedido");
    }

    private void cargarClientes() {
        Toast.makeText(getApplicationContext(), "Actualizando lista de clientes.", Toast.LENGTH_SHORT).show();
        PedidosAPI.getInstance(this.getApplicationContext()).getClientes(getApplicationContext(), new GetClientesServerCallback(){
            @Override
            public void onSuccess(List<ClienteDTO> listaClientes) {
                if (listaClientes!=null) {
                    PedidosAPI.getInstance(getApplicationContext()).saveClientes();
                    items.clear();
                    descripcionesItems.clear();
                    for (int i = 0; i < listaClientes.size(); i++) {
                        items.add(listaClientes.get(i));
                        descripcionesItems.add(listaClientes.get(i).getDescripcion());
                        if (edicion && (listaClientes.get(i).getId().equals(pedidoEditar.getIdCliente()))) {
                            posicionCliente = i;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                if (!edicion) {
                    if (progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                } else {
                    spinnerClientes.setSelection(posicionCliente);
                    if (progressDialog!=null) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private boolean esPedidoValido() {
        boolean clienteVacio = false;
        if (spinnerClientes.getSelectedItem() == null) {
            clienteVacio = true;
        }
        if (listaLineaPedidos.isEmpty() || (clienteVacio)) {
            return false;
        } else {
            return true;
        }
    }

    public void guardarLineaPedido(LineaPedidoDTO linea) {
        //Chequear si es nueva la linea de pedido
        if (esNuevaLineaPedido()) {
            listaLineaPedidos.add(linea);
            adbLineasPedido.notifyDataSetChanged();
        } else {
            //borrar la linea de pedido
            for (int i = 0; i < listaLineaPedidos.size(); ++i) {
                if (listaLineaPedidos.get(i).equals(linea)) {
                    listaLineaPedidos.remove(i);
                    break;
                }
            }
            listaLineaPedidos.add(linea);
            adbLineasPedido.notifyDataSetChanged();
            //agregar la linea de pedido
        }
    }

    public LineaPedidoDTO getLineaPedidoSeleccionada() {
        return this.lineaPedidoSeleccionada;
    }

    public boolean esNuevaLineaPedido() {
        return this.nuevaLineaPedido;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
