package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.util.Log;

public class Agregar_Bebida extends AppCompatActivity {
    EditText etnombre, etprecio, etcantidad; // Añadido etcantidad
    TextView btnregistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.agregar_bebida);

        // Configurar los márgenes para los sistemas de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar los campos de entrada y el botón
        etnombre = findViewById(R.id.etNombre);
        etprecio = findViewById(R.id.etPrecio);
        etcantidad = findViewById(R.id.etCantidad); // Inicializando el campo de cantidad
        btnregistrar = findViewById(R.id.btnRegistrar);

        // Configurar el evento de clic del botón
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metodoRegistrar();
            }
        });
    }

    private String validarCampos() {
        if (etnombre.getText().toString().isEmpty()) {
            return "Nombre";
        }
        if (etprecio.getText().toString().isEmpty()) {
            return "Precio";
        }
        if (etcantidad.getText().toString().isEmpty()) { // Validación para cantidad
            return "Cantidad";
        }
        return null; // Todos los campos están llenos
    }

    private void metodoRegistrar() {
        String campoVacio = validarCampos();
        if (campoVacio == null) {
            registrarBebida("http://192.168.56.1:80/developeru/AgregarBebida.php");
        } else {
            Toast.makeText(this, "Por favor, completa el campo: " + campoVacio, Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarBebida(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respuesta del servidor", response); // Imprime la respuesta en el Logcat

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(Agregar_Bebida.this, "Bebida registrada exitosamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos después de un registro exitoso
                        etnombre.setText("");
                        etprecio.setText("");
                        etcantidad.setText(""); // Limpiar el campo de cantidad

                    } else {
                        String message = jsonResponse.optString("message", "Error al registrar la bebida");
                        Toast.makeText(Agregar_Bebida.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Agregar_Bebida.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Agregar_Bebida.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", etnombre.getText().toString());
                parametros.put("precio", etprecio.getText().toString());
                parametros.put("cantidad", etcantidad.getText().toString()); // Enviar cantidad
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
