package com.gonzalo.pruebafinal;

// Ejemplo: Producto.java
public class Producto {
    private String id; // Para almacenar el ID del documento de Firestore
    private String nombre;
    private double precio; // Representación de Firestore 'Number' (con decimales) en Java
    private String descripcion;

    // Constructor vacío requerido por Firebase
    public Producto() {}

    // Constructor con campos
    public Producto(String nombre, double precio, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    // Getters y Setters para todos los campos

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}