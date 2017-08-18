package com.proyecto.geobus;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.proyecto.geobus.menuActivities.NuevoPedidoActivity;
import com.proyecto.geobus.models.LineaPedidoDTO;

/**
 * Created by alexis on 19/03/17.
 */

public class NuevaLineaDialogFragment extends DialogFragment {

    private AlertDialog mDialog;
    private NuevoPedidoActivity activity;
    private LineaPedidoDTO lPedido = new LineaPedidoDTO();
    private View view;
    private EditText tProducto;
    private EditText tCantidad;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_signin, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = (NuevoPedidoActivity) getActivity();
        if (!activity.esNuevaLineaPedido()) {
            lPedido = activity.getLineaPedidoSeleccionada();
            tProducto = (EditText) view.findViewById(R.id.producto);
            tProducto.setText(lPedido.getIdProducto());
            tCantidad = (EditText) view.findViewById(R.id.cantidad);
            tCantidad.setText(String.valueOf(lPedido.getCantidad()));
        }
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        tProducto = (EditText) view.findViewById(R.id.producto);
        tCantidad = (EditText) view.findViewById(R.id.cantidad);

        tProducto.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valProducto = !tProducto.getText().toString().equals("");
                boolean valCantidad = true;
                if (tCantidad.getText().toString().equals("") || tCantidad.getText().toString().equals(".")) {
                    valCantidad = false;
                }
                if(valProducto && valCantidad) {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        tCantidad.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valProducto = !tProducto.getText().toString().equals("");
                boolean valCantidad = true;
                if (tCantidad.getText().toString().equals("") || tCantidad.getText().toString().equals(".")) {
                    valCantidad = false;
                }
                if(valProducto && valCantidad) {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }
                if(valProducto && valCantidad) {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Se agrega o edita la linea del pedido..
                        boolean valProducto = !tProducto.getText().toString().equals("");
                        boolean valCantidad = !tCantidad.getText().toString().equals("");
                        if (valProducto && valCantidad) {
                            lPedido.setIdProducto(tProducto.getText().toString());
                            lPedido.setCantidad(Float.parseFloat(tCantidad.getText().toString()));
                            activity.guardarLineaPedido(lPedido);
                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } else if (!valProducto && !valCantidad) {
                            //Toast.makeText(mDialog.getContext(), "Complete el producto y la cantidad", Toast.LENGTH_SHORT);
                        } else if (valProducto & !valCantidad){
                            //Toast.makeText(mDialog.getContext(), "Complete la cantidad", Toast.LENGTH_SHORT);
                        } else {
                            //Toast.makeText(mDialog.getContext(), "Complete el producto", Toast.LENGTH_SHORT);
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        mDialog.cancel();
                    }
                });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        tProducto.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return mDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
    }
}