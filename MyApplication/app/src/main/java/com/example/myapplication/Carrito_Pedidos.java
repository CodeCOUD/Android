package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Carrito_Pedidos extends AppCompatActivity {
    ListView listViewCarrito;
    ArrayList<Clase_Pedido> carritoList;
    TextView textViewTotal;
    TextView btnCompletarPedido;
    TextView btnCancelarPedido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito_pedidos);

        // Inicializar las vistas
        listViewCarrito = findViewById(R.id.listViewCarrito);
        textViewTotal = findViewById(R.id.textViewTotal);
        btnCompletarPedido = findViewById(R.id.btnCompletarPedido); // Botón de completar pedido
        btnCancelarPedido=findViewById(R.id.btnCancelarPedido);
        carritoList = new ArrayList<>();

        // Recibir los datos enviados desde el activity anterior
        recibirDatos();

        // Mostrar el carrito en el ListView con el adaptador personalizado
        mostrarCarrito();

        // Calcular el total del carrito y mostrarlo
        calcularTotal();

        // Enviar el pedido cuando se presione el botón de completar pedido
        btnCompletarPedido.setOnClickListener(v -> completarPedido());
    }
    public void CancelarPedido(View v) {
        Intent intent = new Intent(Carrito_Pedidos.this, Platos.class);
        startActivity(intent);

    }
    // Método para recibir los datos del carrito desde el Intent
    private void recibirDatos() {
        Intent intent = getIntent();
        String jsonCarrito = intent.getStringExtra("carritoTotal");

        if (jsonCarrito != null) {
            Log.d("RecibirDatos", jsonCarrito);  // Verificar el JSON recibido
            Type type = new TypeToken<ArrayList<Clase_Pedido>>() {}.getType();
            carritoList = new Gson().fromJson(jsonCarrito, type);
        } else {
            Log.e("RecibirDatos", "No se recibieron datos del carrito.");  // Log de error si no llegan datos
            Toast.makeText(this, "No se recibieron datos del carrito.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mostrar los datos del carrito en un ListView
    private void mostrarCarrito() {
        if (carritoList != null && !carritoList.isEmpty()) {
            // Utilizar el adaptador personalizado PedidoAdapter
            PedidoAdapter adapter = new PedidoAdapter(this, carritoList);
            listViewCarrito.setAdapter(adapter);
        } else {
            Log.e("MostrarCarrito", "El carrito está vacío.");
            Toast.makeText(this, "No hay pedidos en el carrito", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para calcular y mostrar el total del carrito
    private void calcularTotal() {
        double total = 0;
        for (Clase_Pedido pedido : carritoList) {
            total += pedido.getPrecio() * pedido.getCantidad();  // Calcular el costo total
        }
        textViewTotal.setText("Total: $" + total);
    }

    // Método para enviar el pedido al servidor
    private void completarPedido() {
        if (carritoList == null || carritoList.isEmpty()) {
            Log.e("CompletarPedido", "El carrito está vacío o no fue recibido correctamente.");
            Toast.makeText(this, "El carrito está vacío o no se recibieron los datos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir el carrito a JSON
        String jsonCarrito = new Gson().toJson(carritoList);
        Log.d("JSONCarrito", jsonCarrito);  // Verificar los datos que se están enviando

        String url = "http://192.168.56.1:80/developeru/RegistrarPedido.php";

        // Enviar los datos al servidor usando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("Response", response);  // Imprimir respuesta del servidor
                    Toast.makeText(Carrito_Pedidos.this, "Pedido completado", Toast.LENGTH_SHORT).show();
                    carritoList.clear();  // Limpiar el carrito después de completar el pedido
                },
                error -> {
                    String message = (error.getMessage() != null) ? error.getMessage() : "Error desconocido";
                    Log.e("VolleyError", message);  // Mostrar el error en el log
                    Toast.makeText(Carrito_Pedidos.this, "Error al completar el pedido: " + message, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                Log.d("BodyContent", jsonCarrito);  // Verificar el contenido del cuerpo que se envía
                return jsonCarrito.getBytes();  // Enviar el JSON como cuerpo de la solicitud
            }

            @Override
            public String getBodyContentType() {
                return "application/json";  // Asegurarse de que el tipo de contenido sea JSON
            }
        };

        // Añadir la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
