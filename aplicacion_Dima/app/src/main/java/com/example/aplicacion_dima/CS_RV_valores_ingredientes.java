package com.example.aplicacion_dima;

public class CS_RV_valores_ingredientes {
   private String nombre, descripcion;
   private int id;
   private byte[] imagenBytes;

    // Constructor con todos los atributos, incluido el nuevo atributo id
    public CS_RV_valores_ingredientes() {}

    public CS_RV_valores_ingredientes(int id, String nombre, String descripcion, byte[] imagenBytes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenBytes = imagenBytes;
    }


    // Getters y setters para el nuevo atributo id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public byte[] getImagenBytes() {
        return imagenBytes;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setImagenBytes(byte[] imagenBytes) {
        this.imagenBytes = imagenBytes;
    }

}
