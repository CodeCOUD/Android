package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Lista_Pedidos extends AppCompatActivity {

    RecyclerView recyclerViewPedidos;
    ArrayList<Pedido> pedidosList;
    ListaPedidosAdapter pedidosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_pedidos);

        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        pedidosList = new ArrayList<>();

        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        cargarPedidos(); // Cargar los pedidos de la base de datos
    }

    private void cargarPedidos() {
        String url = "http://192.168.56.1:80/developeru/ListaPedidos.php";  // URL del servidor para obtener la lista de pedidos

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("Respuesta", response);  // Log para depuración

                    try {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Pedido>>() {}.getType();
                        pedidosList = gson.fromJson(response, listType);

                        pedidosAdapter = new ListaPedidosAdapter(Lista_Pedidos.this, pedidosList);
                        recyclerViewPedidos.setAdapter(pedidosAdapter);

                    } catch (Exception e) {
                        Toast.makeText(Lista_Pedidos.this, "Error al procesar la respuesta JSON", Toast.LENGTH_SHORT).show();
                        Log.e("Error", e.toString());
                    }

                }, error -> {
            Toast.makeText(Lista_Pedidos.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
