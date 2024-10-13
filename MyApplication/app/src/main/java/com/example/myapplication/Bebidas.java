package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Bebidas extends AppCompatActivity {
    ListView listViewBebidas;
    ArrayList<Clase_Pedido> bebidasList;
    BebidasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bebidas);  // Seteando el layout correcto

        // Inicializar las vistas
        listViewBebidas = findViewById(R.id.listViewBebidas);
        bebidasList = new ArrayList<>();
        TextView buttonTerminar = findViewById(R.id.buttonTerminar); // Cambiado a Button

        // Cargar las bebidas desde el servidor
        cargarBebidas();

        // Acción del botón "Terminar"
        buttonTerminar.setOnClickListener(view -> {
            SharedPreferences prefs = getSharedPreferences("carrito", MODE_PRIVATE);
            String jsonPlatos = prefs.getString("platosSeleccionados", null);

            ArrayList<Clase_Pedido> platosList = new ArrayList<>();
            if (jsonPlatos != null) {
                Type type = new TypeToken<ArrayList<Clase_Pedido>>() {}.getType();
                platosList = new Gson().fromJson(jsonPlatos, type);
            }

            // Combinar los platos seleccionados con las bebidas seleccionadas
            ArrayList<Clase_Pedido> carritoTotal = new ArrayList<>(platosList);
            carritoTotal.addAll(adapter.getSelectedItems());

            // Enviar el carrito completo a la actividad siguiente
            Intent intent = new Intent(Bebidas.this, Carrito_Pedidos.class);
            intent.putExtra("carritoTotal", new Gson().toJson(carritoTotal));
            startActivity(intent);
        });
    }

    // Método para cargar las bebidas desde un servidor usando Volley
    private void cargarBebidas() {
        String url = "http://192.168.56.1:80/developeru/ListaBebidas.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject bebida = response.getJSONObject(i);
                            String nombre = bebida.getString("nombre");
                            String precio = bebida.getString("precio")
                                    .replace("$", "")
                                    .replace("€", "")
                                    .replace(",", ".")
                                    .trim();
                            double precioDouble = Double.parseDouble(precio);

                            bebidasList.add(new Clase_Pedido(nombre, precioDouble, 1, "bebida"));  // Cantidad predeterminada 1
                        }

                        // Inicializar el adaptador
                        adapter = new BebidasAdapter(Bebidas.this, bebidasList);
                        listViewBebidas.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(Bebidas.this, "Error al cargar las bebidas", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String message = error.getMessage() != null ? error.getMessage() : "Error desconocido";
                    Toast.makeText(Bebidas.this, "Error de red: " + message, Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
