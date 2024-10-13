package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.VolleyError;

public class Pantalla_Administrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantalla_administrador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void MetodoAgregarPlatos(View v) {
        Intent intent = new Intent(getApplicationContext(), Agregar_Plato.class);
        startActivity(intent);
    }
    public void MetodoAgregarBebidas(View v) {
        Intent intent = new Intent(getApplicationContext(), Agregar_Bebida.class);
        startActivity(intent);
    }
    public void MetodoMostrarPedidos(View v) {
        Intent intent = new Intent(getApplicationContext(), Lista_Pedidos.class);
        startActivity(intent);
    }
}