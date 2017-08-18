package com.proyecto.geobus.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.proyecto.geobus.R;
import com.proyecto.geobus.models.LineaPedidoDTO;

import java.util.ArrayList;

/**
 * Created by alexis on 20/03/17.
 */

public class LineaPedidoAdapter extends ArrayAdapter<LineaPedidoDTO> {

    private Activity activity;
    private ArrayList<LineaPedidoDTO> lPedidos;
    private static LayoutInflater inflater = null;

    public LineaPedidoAdapter (Activity activity, int textViewResourceId,ArrayList<LineaPedidoDTO> _lPedidos) {
        super(activity, textViewResourceId);
        try {
            this.activity = activity;
            this.lPedidos = _lPedidos;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lPedidos.size();
    }

    public long getItemId(int position) {
        return lPedidos.get(position).getId();
    }

    public static class ViewHolder {
        public TextView display_producto;
        public TextView display_cantidad;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.list_lineas_pedidos_element, null);
                holder = new ViewHolder();
                holder.display_producto = (TextView) vi.findViewById(R.id.firstLine);
                holder.display_cantidad = (TextView) vi.findViewById(R.id.secondLine);
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            holder.display_producto.setText("Producto: " + lPedidos.get(position).getIdProducto());
            holder.display_cantidad.setText("Cantidad: " + String.valueOf(lPedidos.get(position).getCantidad()));

            Button deleteBtn = (Button) vi.findViewById(R.id.delete_btn_lineas);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    lPedidos.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
        }
        return vi;
    }
}