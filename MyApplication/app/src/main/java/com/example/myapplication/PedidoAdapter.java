package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PedidoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Clase_Pedido> pedidosList;

    public PedidoAdapter(Context context, ArrayList<Clase_Pedido> pedidosList) {
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @Override
    public int getCount() {
        return pedidosList.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_pedido, parent, false);
        }

        // Obtener las referencias de las vistas del layout item_pedido.xml
        TextView nombrePlatoBebida = convertView.findViewById(R.id.txtNombrePlatoBebida);
        TextView precioPlatoBebida = convertView.findViewById(R.id.txtPrecioPlatoBebida);
        TextView cantidadText = convertView.findViewById(R.id.cantidad);

        // Obtener el pedido actual de la lista
        Clase_Pedido pedido = pedidosList.get(position);

        // Asignar valores a las vistas
        nombrePlatoBebida.setText(pedido.getNombre());
        precioPlatoBebida.setText("Bs" + pedido.getPrecio());
        cantidadText.setText(String.valueOf(pedido.getCantidad()));

        return convertView;
    }
}
