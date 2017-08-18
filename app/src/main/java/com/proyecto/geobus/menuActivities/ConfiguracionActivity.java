package com.proyecto.geobus.menuActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.proyecto.geobus.R;
import com.proyecto.geobus.api.PedidosAPI;

/**
 * Created by alexis on 28/03/17.
 */

public class ConfiguracionActivity extends AppCompatActivity {

    private ProgressDialog progressDialog = null;
    private EditText ed_host;
    private EditText ed_clientes;
    private EditText ed_pedidos;
    private ConfiguracionActivity mIntancia = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        progressDialog = ProgressDialog.show(ConfiguracionActivity.this, "", "Cargando Datos..", true);

        Button guardar = (Button) findViewById(R.id.button_guardar_config);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean exito1 = PedidosAPI.getInstance(getApplicationContext()).setConfigHost(ed_host.getText().toString());
                boolean exito2 = PedidosAPI.getInstance(getApplicationContext()).setConfigClientes(ed_clientes.getText().toString());
                boolean exito3 = PedidosAPI.getInstance(getApplicationContext()).setConfigPedidos(ed_pedidos.getText().toString());
                if (exito1 && exito2 && exito3) {
                    Toast.makeText(getApplicationContext(), "Se guardo con éxito la configuración.", Toast.LENGTH_SHORT).show();
                    mIntancia.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Se produjo un error al guardar la configuración.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button restablecer = (Button) findViewById(R.id.button_restablecer_config);
        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_host.setText(getApplicationContext().getResources().getString(R.string.host));
                ed_clientes.setText(getApplicationContext().getResources().getString(R.string.getClientesEndPoint));
                ed_pedidos.setText(getApplicationContext().getResources().getString(R.string.savePedidosEndPoint));
            }
        });
        TextView tw_device_id = (TextView) findViewById(R.id.textView_device_id);
        final TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        tw_device_id.setText("" + tm.getDeviceId());
        ed_host = (EditText) findViewById(R.id.editText_Host);
        ed_clientes = (EditText) findViewById(R.id.editText_Clientes);
        ed_pedidos = (EditText) findViewById(R.id.editText_Pedidos);

        ed_host.setText(PedidosAPI.getInstance(getApplicationContext()).getConfig().getHost());
        ed_clientes.setText(PedidosAPI.getInstance(getApplicationContext()).getConfig().getClientes());
        ed_pedidos.setText(PedidosAPI.getInstance(getApplicationContext()).getConfig().getPedidos());
        mIntancia = this;
        progressDialog.dismiss();
    }
}