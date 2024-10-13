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

public class Crear_Usuario extends AppCompatActivity {
    EditText etUsuario, etContraseña, etNombreCompleto, etDireccion;
    TextView btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.crear_usuario);

        // Configurar los márgenes para los sistemas de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar los campos de entrada y el botón
        etUsuario = findViewById(R.id.etUsuario);
        etContraseña = findViewById(R.id.etContraseña);
        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etDireccion = findViewById(R.id.etDireccion);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Configurar el evento de clic del botón
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metodoRegistrar();
            }
        });
    }

    private String validarCampos() {
        if (etUsuario.getText().toString().isEmpty()) {
            return "Usuario";
        }
        if (etContraseña.getText().toString().isEmpty()) {
            return "Contraseña";
        }
        if (etNombreCompleto.getText().toString().isEmpty()) {
            return "Nombre Completo";
        }
        if (etDireccion.getText().toString().isEmpty()) {
            return "Dirección";
        }
        return null; // Todos los campos están llenos
    }

    private void metodoRegistrar() {
        String campoVacio = validarCampos();
        if (campoVacio == null) {
            registrarUsuario("http://192.168.56.1:80/developeru/CrearUsuario.php");
        } else {
            Toast.makeText(this, "Por favor, completa el campo: " + campoVacio, Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Respuesta del servidor", response); // Imprime la respuesta en el Logcat

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(Crear_Usuario.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos después de un registro exitoso
                        etUsuario.setText("");
                        etContraseña.setText("");
                        etNombreCompleto.setText("");
                        etDireccion.setText("");
                    } else {
                        String message = jsonResponse.optString("message", "Error al registrar el usuario");
                        Toast.makeText(Crear_Usuario.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Crear_Usuario.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Crear_Usuario.this, "Error de red: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", etUsuario.getText().toString());
                parametros.put("contrasena", etContraseña.getText().toString());
                parametros.put("nombreCompleto", etNombreCompleto.getText().toString());
                parametros.put("direccion", etDireccion.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
