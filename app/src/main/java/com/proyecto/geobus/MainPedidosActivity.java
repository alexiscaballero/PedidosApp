package com.proyecto.geobus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.proyecto.geobus.api.PedidosAPI;
import com.proyecto.geobus.menuActivities.ConfiguracionActivity;
import com.proyecto.geobus.menuActivities.ListPedidosActivity;
import com.proyecto.geobus.menuActivities.NuevoPedidoActivity;
import com.proyecto.geobus.models.ClienteDTO;
import com.proyecto.geobus.models.MensajeDTO;
import com.proyecto.geobus.models.PedidoDTO;
import com.proyecto.geobus.util.GetClientesServerCallback;
import com.proyecto.geobus.util.PedidoAdapter;
import com.proyecto.geobus.util.SavePedidosServerCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by alexis on 19/03/17.
 */

public class MainPedidosActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listview;
    private PedidoAdapter adbPedidos;
    private ArrayList<PedidoDTO> listaPedidos = new ArrayList<PedidoDTO>();
    private ProgressDialog progressDialog = null;
    private ProgressDialog progressDialogEnviar = null;
    private boolean primeraCarga = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = ProgressDialog.show(MainPedidosActivity.this, "", "Cargando Datos..", true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), NuevoPedidoActivity.class);
                startActivityForResult(i, 1);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarPedidos();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listview = (ListView) findViewById(R.id.listView3);
        adbPedidos = new PedidoAdapter(this, 0, listaPedidos);
        listview.setAdapter(adbPedidos);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarPedido(listaPedidos.get(position));
            }
        });
        //cargarClientes();
        cargarPedidos();
    }

    public void mostrarPedido(PedidoDTO pedido) {
        Intent i = new Intent(getApplicationContext(),NuevoPedidoActivity.class);
        i.putExtra("EDIT","TRUE");
        i.putExtra("Pedido",pedido);
        startActivity(i);
    }

    public void cargarPedidos() {
        listaPedidos.addAll(PedidosAPI.getInstance(this.getApplicationContext()).getPedidos());
        adbPedidos.notifyDataSetChanged();
        if (progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    private void cargarClientes() {
        PedidosAPI.getInstance(this.getApplicationContext()).getClientes(getApplicationContext(), new GetClientesServerCallback(){
            @Override
            public void onSuccess(List<ClienteDTO> listaClientes) {
                PedidosAPI.getInstance(getApplicationContext()).saveClientes();
                cargarPedidos();
            }
        });
    }

    private void enviarPedidos() {
        if (!listaPedidos.isEmpty()) {
            progressDialogEnviar = ProgressDialog.show(MainPedidosActivity.this, "", "Enviando Pedidos..", true);
            progressDialogEnviar.setCancelable(false);
            progressDialogEnviar.setCanceledOnTouchOutside(false);
            PedidosAPI.getInstance(this.getApplicationContext()).sendPedidos(new SavePedidosServerCallback(){
                @Override
                public void onSuccess(MensajeDTO mensaje) {
                    if (mensaje.getEstado()==1) {
                        listaPedidos.clear();
                        adbPedidos.notifyDataSetChanged();
                    }
                    progressDialogEnviar.dismiss();
                    Toast.makeText(getApplicationContext(), mensaje.getMensaje(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Cargue al menos un pedido.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;
        switch (id) {
            case R.id.nav_configuracion:
                i = new Intent(this, ConfiguracionActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.nav_nuevo_pedido:
                i = new Intent(this, NuevoPedidoActivity.class);
                startActivityForResult(i, 1);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!primeraCarga) {
            listaPedidos.clear();
            adbPedidos.notifyDataSetChanged();
            //cargarClientes();
            cargarPedidos();
        } else {
            primeraCarga = false;
        }
    }
}
