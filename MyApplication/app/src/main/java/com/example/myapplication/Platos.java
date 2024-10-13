package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Platos extends AppCompatActivity {
    ListView listViewPlatos;
    ArrayList<Clase_Pedido> platosList;
    PlatosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.platos);

        listViewPlatos = findViewById(R.id.listViewPlatos);
        platosList = new ArrayList<>();

        cargarPlatos();

        TextView buttonTerminar = findViewById(R.id.buttonTerminar);
        buttonTerminar.setOnClickListener(view -> {
            ArrayList<Clase_Pedido> selectedPlatos = adapter.getSelectedItems();
            if (selectedPlatos.isEmpty()) {
                Toast.makeText(Platos.this, "No has seleccionado ningún plato.", Toast.LENGTH_SHORT).show();
            } else {
                // Guardar los platos seleccionados en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("carrito", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                String jsonPlatos = new Gson().toJson(selectedPlatos);
                editor.putString("platosSeleccionados", jsonPlatos);
                editor.apply();

                Intent intent = new Intent(Platos.this, Bebidas.class);
                startActivity(intent);
            }
        });
    }

    private void cargarPlatos() {
        String url = "http://192.168.56.1:80/developeru/ListaPlatos.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject plato = response.getJSONObject(i);
                            String nombre = plato.getString("nombre");
                            String precio = plato.getString("precio").replace("$", "").replace("€", "").replace(",", ".").trim();

                            try {
                                double precioDouble = Double.parseDouble(precio);
                                platosList.add(new Clase_Pedido(nombre, precioDouble, 1, "plato"));  // Usamos una cantidad predeterminada de 1 y tipo plato
                                // Cantidad predeterminada de 1

                            } catch (NumberFormatException e) {
                                Toast.makeText(Platos.this, "Error al convertir el precio: " + precio, Toast.LENGTH_SHORT).show();
                            }
                        }

                        adapter = new PlatosAdapter(Platos.this, platosList);
                        listViewPlatos.setAdapter(adapter);

                    } catch (JSONException e) {
                        Toast.makeText(Platos.this, "Error al cargar los platos", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String message = error.getMessage() != null ? error.getMessage() : "Error desconocido";
                    Toast.makeText(Platos.this, "Error de red: " + message, Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}



