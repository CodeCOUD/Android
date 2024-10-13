package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ListaPedidosAdapter extends RecyclerView.Adapter<ListaPedidosAdapter.PedidoViewHolder> {

    private Context context;
    private ArrayList<Pedido> pedidosList;  // Lista de pedidos

    public ListaPedidosAdapter(Context context, ArrayList<Pedido> pedidosList) {
        this.context = context;
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout item_listapedidos
        View view = LayoutInflater.from(context).inflate(R.layout.item_listapedidos, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        // Obtener el pedido actual
        Pedido pedido = pedidosList.get(position);

        // Asignar los datos a los TextViews
        holder.nombreCompleto.setText(pedido.getNombreCompleto());
        holder.nombrePedido.setText(pedido.getNombrePedido());
        holder.platos.setText(pedido.getPlatos());
        holder.bebidas.setText(pedido.getBebidas());
        holder.cantidadPlatos.setText(String.valueOf(pedido.getCantidadPlatos()));
        holder.cantidadBebidas.setText(String.valueOf(pedido.getCantidadBebidas()));
        holder.precioTotal.setText(String.valueOf(pedido.getPrecioTotal()));
        holder.fechaRegistro.setText(pedido.getFechaRegistro());
    }

    @Override
    public int getItemCount() {
        return pedidosList.size(); // Retornar el tamaño de la lista
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        // Definir TextViews según el layout de item_listapedidos.xml
        TextView nombreCompleto, nombrePedido, platos, bebidas, cantidadPlatos, cantidadBebidas, precioTotal, fechaRegistro;
        CardView cardView;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Asignar los TextViews a las vistas correspondientes del layout
            nombreCompleto = itemView.findViewById(R.id.txtNombreCompleto);
            nombrePedido = itemView.findViewById(R.id.txtNombrePedido);
            platos = itemView.findViewById(R.id.txtPlatos);
            bebidas = itemView.findViewById(R.id.txtBebidas);
            cantidadPlatos = itemView.findViewById(R.id.txtCantidadPlatos);
            cantidadBebidas = itemView.findViewById(R.id.txtCantidadBebidas);
            precioTotal = itemView.findViewById(R.id.txtPrecioTotal);
            fechaRegistro = itemView.findViewById(R.id.txtFechaRegistro);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
