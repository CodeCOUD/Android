package com.example.myapplication;

public class Pedido {
    private String nombreCompleto;
    private String nombre_pedido;
    private String platos;
    private String bebidas;
    private int cantidad_platos;
    private int cantidad_bebidas;
    private double precio_total;
    private String fechaRegistro;

    // Getters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getNombrePedido() {
        return nombre_pedido;
    }

    public String getPlatos() {
        return platos;
    }

    public String getBebidas() {
        return bebidas;
    }

    public int getCantidadPlatos() {
        return cantidad_platos;
    }

    public int getCantidadBebidas() {
        return cantidad_bebidas;
    }

    public double getPrecioTotal() {
        return precio_total;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }
}
