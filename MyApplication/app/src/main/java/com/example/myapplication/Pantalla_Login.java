package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Pantalla_Login extends AppCompatActivity {
    EditText usuario, passw;
    TextView btnIng,btnCrUsuario,btnAdm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantalla_login);

        // Inicializa los elementos de la UI
        usuario = findViewById(R.id.edtUsuario);
        passw = findViewById(R.id.edtPassword);
        btnIng = findViewById(R.id.btnLogin);
        btnCrUsuario = findViewById(R.id.btnCancelarPedido);
        btnAdm = findViewById(R.id.btnAdministrador);


        // Ajusta el padding para evitar solapamiento con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void metodoCrearCuenta(View v) {
        Intent intent = new Intent(getApplicationContext(), Crear_Usuario.class);
        startActivity(intent);
    }
    public void metodoAdministrador(View v) {
        if (validarCampos()) {
            validarAdministrador("http://192.168.56.1:80/developeru/validarLoginAdmin.php");
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para validar que el usuario es administrador
    private void validarAdministrador(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Verificar si el rol es administrador
                        String rol = jsonResponse.getString("rol");
                        if ("administrador".equals(rol)) {
                            // Autenticación exitosa y el rol es administrador
                            Intent intent = new Intent(getApplicationContext(), Pantalla_Administrador.class);
                            startActivity(intent);
                        } else {
                            // No tiene rol de administrador
                            Toast.makeText(Pantalla_Login.this, "Acceso denegado. No eres administrador.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Autenticación fallida
                        Toast.makeText(Pantalla_Login.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Pantalla_Login.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Pantalla_Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("contrasena", passw.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void metodoIngresar(View v) {
        if (validarCampos()) {
            validarLogin("http://192.168.56.1:80/developeru/validarLogin.php");
        } else {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        return !usuario.getText().toString().isEmpty() && !passw.getText().toString().isEmpty();
    }

    private void validarLogin(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Autenticación exitosa
                        Intent intent = new Intent(getApplicationContext(), Pedidos.class);
                        startActivity(intent);
                    } else {
                        // Autenticación fallida
                        Toast.makeText(Pantalla_Login.this, "Usuario o Contraseña Incorrectos", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Pantalla_Login.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Pantalla_Login.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("usuario", usuario.getText().toString());
                parametros.put("contrasena", passw.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
