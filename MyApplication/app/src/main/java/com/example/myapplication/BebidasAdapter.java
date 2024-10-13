package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class BebidasAdapter extends ArrayAdapter<Clase_Pedido> {
    private final Context context;
    private final ArrayList<Clase_Pedido> bebidasList;
    private final boolean[] selectedItems;

    public BebidasAdapter(Context context, ArrayList<Clase_Pedido> bebidasList) {
        super(context, R.layout.item_bebida, bebidasList);
        this.context = context;
        this.bebidasList = bebidasList;
        this.selectedItems = new boolean[bebidasList.size()];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_bebida, parent, false);
        }

        ImageView imgBebida = convertView.findViewById(R.id.imgBebida);
        TextView textViewNombre = convertView.findViewById(R.id.txtNombreBebida);
        TextView textViewPrecio = convertView.findViewById(R.id.txtPrecioBebida);
        TextInputEditText cantidadText = convertView.findViewById(R.id.cantidad);
        ImageButton incrementButton = convertView.findViewById(R.id.increment_button);
        ImageButton decrementButton = convertView.findViewById(R.id.decrement_button);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        // Obtener la bebida actual
        Clase_Pedido bebida = bebidasList.get(position);

        // Configurar el nombre y precio de la bebida
        textViewNombre.setText(bebida.getNombre());
        textViewPrecio.setText("Bs " + bebida.getPrecio());
        cantidadText.setText(String.valueOf(bebida.getCantidad()));

        // Cargar imágenes cíclicamente
        int[] imageResources = {R.drawable.ic_bebida1, R.drawable.ic_bebida2, R.drawable.ic_bebida3};
        imgBebida.setImageResource(imageResources[position % imageResources.length]);

        // Configurar el estado del checkbox
        checkBox.setChecked(selectedItems[position]);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selectedItems[position] = isChecked);

        // Configurar los botones de incremento y decremento
        incrementButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(cantidadText.getText().toString());
            currentQuantity++;
            cantidadText.setText(String.valueOf(currentQuantity));
            bebida.setCantidad(currentQuantity);  // Actualizar la cantidad en el objeto
        });

        decrementButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(cantidadText.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                cantidadText.setText(String.valueOf(currentQuantity));
                bebida.setCantidad(currentQuantity);  // Actualizar la cantidad en el objeto
            }
        });

        return convertView;
    }

    // Método para obtener los elementos seleccionados
    public ArrayList<Clase_Pedido> getSelectedItems() {
        ArrayList<Clase_Pedido> selectedBebidas = new ArrayList<>();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                selectedBebidas.add(bebidasList.get(i));
            }
        }
        return selectedBebidas;
    }
}
