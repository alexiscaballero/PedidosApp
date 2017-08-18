package com.proyecto.geobus.menuActivities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.proyecto.geobus.R;
import com.proyecto.geobus.api.PedidosAPI;
import com.proyecto.geobus.models.PedidoDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexis on 19/03/17.
 */
public class ListPedidosActivity extends ListActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ArrayList<String> idItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView listView = getListView();
        items = new ArrayList<String>();
        idItems = new ArrayList<String>();
        adapter = new ArrayAdapter(this, R.layout.list_pedidos,R.id.tv_pedidos,items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idPedidoSeleccionado = idItems.get(position);
                //Cargar Pantalla Para Editar Pedido
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog progressDialog = ProgressDialog.show(ListPedidosActivity.this, "", "Buscando Pedidos..", true);
        List<PedidoDTO> pedidos = PedidosAPI.getInstance(this).getPedidos();
        for (int i = 0; i < pedidos.size(); i++) {
            items.add(pedidos.get(i).getIdCliente());
            idItems.add(String.valueOf(pedidos.get(i).getId()));
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}
