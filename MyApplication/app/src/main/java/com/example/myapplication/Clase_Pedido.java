package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Clase_Pedido implements Parcelable {
    private String nombre;
    private double precio;
    private int cantidad;  // Campo para almacenar la cantidad
    private String tipo;   // Nuevo campo para identificar si es plato o bebida

    // Constructor actualizado para incluir 'tipo'
    public Clase_Pedido(String nombre, double precio, int cantidad, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    // Constructor para manejar 'Parcel'
    protected Clase_Pedido(Parcel in) {
        nombre = in.readString();
        precio = in.readDouble();
        cantidad = in.readInt();
        tipo = in.readString();  // Leer el tipo del Parcel
    }

    // Implementación de Parcelable para crear la clase desde un Parcel
    public static final Creator<Clase_Pedido> CREATOR = new Creator<Clase_Pedido>() {
        @Override
        public Clase_Pedido createFromParcel(Parcel in) {
            return new Clase_Pedido(in);
        }

        @Override
        public Clase_Pedido[] newArray(int size) {
            return new Clase_Pedido[size];
        }
    };
    public void setCantidad(int cantidad) {
        if (cantidad >= 0) {
            this.cantidad = cantidad;
        }
    }

    // Getters para obtener los valores de la clase
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTipo() {
        return tipo;  // Método para obtener el tipo (plato o bebida)
    }

    // Métodos de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeDouble(precio);
        dest.writeInt(cantidad);
        dest.writeString(tipo);  // Escribir el tipo en el Parcel
    }
}
