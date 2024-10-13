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

public class PlatosAdapter extends ArrayAdapter<Clase_Pedido> {
    private final Context context;
    private final ArrayList<Clase_Pedido> platosList;
    private final boolean[] selectedItems; // Array para rastrear qué elementos están seleccionados

    public PlatosAdapter(Context context, ArrayList<Clase_Pedido> platosList) {
        super(context, R.layout.item_plato, platosList);
        this.context = context;
        this.platosList = platosList;
        this.selectedItems = new boolean[platosList.size()]; // Inicializa el array de selecciones
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_plato, parent, false);
        }

        // Obtiene las vistas del layout inflado
        ImageView imgPlato = convertView.findViewById(R.id.imgPlato);
        TextView textViewNombre = convertView.findViewById(R.id.txtNombrePlato);
        TextView textViewPrecio = convertView.findViewById(R.id.txtPrecioPlato);
        TextInputEditText cantidadText = convertView.findViewById(R.id.cantidad);
        ImageButton incrementButton = convertView.findViewById(R.id.increment_button);
        ImageButton decrementButton = convertView.findViewById(R.id.decrement_button);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        // Obtiene el objeto Clase_Pedido correspondiente a esta posición
        Clase_Pedido plato = platosList.get(position);

        // Configura el nombre y precio del plato
        textViewNombre.setText(plato.getNombre());
        textViewPrecio.setText("Bs " + plato.getPrecio());
        cantidadText.setText(String.valueOf(plato.getCantidad()));

        // Cargar las imágenes cíclicamente
        int[] imageResources = {R.drawable.ic_plato1, R.drawable.ic_plato2, R.drawable.ic_plato3};
        imgPlato.setImageResource(imageResources[position % imageResources.length]);

        // Configura el estado del checkbox
        checkBox.setChecked(selectedItems[position]);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> selectedItems[position] = isChecked);

        // Configura los botones de incremento y decremento
        incrementButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(cantidadText.getText().toString());
            currentQuantity++;
            cantidadText.setText(String.valueOf(currentQuantity));
            plato.setCantidad(currentQuantity);  // Actualizar la cantidad en el objeto
        });

        decrementButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(cantidadText.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                cantidadText.setText(String.valueOf(currentQuantity));
                plato.setCantidad(currentQuantity);  // Actualizar la cantidad en el objeto
            }
        });

        return convertView;
    }

    // Método para obtener los elementos seleccionados
    public ArrayList<Clase_Pedido> getSelectedItems() {
        ArrayList<Clase_Pedido> selectedPlatos = new ArrayList<>();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                selectedPlatos.add(platosList.get(i)); // Agrega los elementos seleccionados a la lista
            }
        }
        return selectedPlatos;
    }
}
