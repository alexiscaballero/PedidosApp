package com.proyecto.geobus.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.proyecto.geobus.R;
import com.proyecto.geobus.api.PedidosAPI;
import com.proyecto.geobus.models.LineaPedidoDTO;
import com.proyecto.geobus.models.PedidoDTO;

import java.util.ArrayList;

/**
 * Created by alexis on 20/03/17.
 */

public class PedidoAdapter extends ArrayAdapter<PedidoDTO> {

    private Activity activity;
    private ArrayList<PedidoDTO> pedidos;
    private static LayoutInflater inflater = null;

    public PedidoAdapter(Activity activity, int textViewResourceId, ArrayList<PedidoDTO> _pedidos) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            this.pedidos = _pedidos;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
        }
    }

    public int getCount() {
        return pedidos.size();
    }

    public long getItemId(int position) {
        return -1;
    }

    public static class ViewHolder {
        public TextView display_cliente;
        public TextView display_detalle;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_pedidos_element, null);
                holder = new ViewHolder();
                holder.display_cliente = (TextView) vi.findViewById(R.id.primeraLinea);
                holder.display_detalle = (TextView) vi.findViewById(R.id.segundaLinea);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            String nombreCliente = PedidosAPI.getInstance(getContext()).getClienteNombre(pedidos.get(position).getIdCliente());
            holder.display_cliente.setText("Cliente: " + nombreCliente);
            holder.display_detalle.setText("Líneas de Pedido: " + String.valueOf(pedidos.get(position).getPedidos().size()));

            Button deleteBtn = (Button) vi.findViewById(R.id.delete_btn_pedidos);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    mostrarAlert(position);
                }
            });
        } catch (Exception e) {
        }
        return vi;
    }

    public void mostrarAlert(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());
        // set title
        alertDialogBuilder.setTitle("Eliminar Pedido");
        // set dialog message
        alertDialogBuilder
                .setMessage("¿Está seguro de eliminar el pedido seleccionado?")
                .setCancelable(false)
                .setPositiveButton("Si",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        PedidosAPI.getInstance(getContext()).eliminarPedido(pedidos.get(position).getId());
                        pedidos.remove(position); //or some other task
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

}