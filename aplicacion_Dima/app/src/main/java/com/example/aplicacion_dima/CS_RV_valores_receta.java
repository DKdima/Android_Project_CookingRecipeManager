package com.example.aplicacion_dima;

public class CS_RV_valores_receta {

    private String nombre, descripcion, cantidad;
    private int id;
    private byte[] imagenBytes;

    // Constructor con todos los atributos, incluido el nuevo atributo id
    public CS_RV_valores_receta() {}

    public CS_RV_valores_receta(int id, String nombre, String descripcion, byte[] imagenBytes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenBytes = imagenBytes;
    }


    // Getters y setters para el nuevo atributo id
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getCantidad() {
        return cantidad;
    }
    public byte[] getImagenBytes() {
        return imagenBytes;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public void setImagenBytes(byte[] imagenBytes) {
        this.imagenBytes = imagenBytes;
    }

}
