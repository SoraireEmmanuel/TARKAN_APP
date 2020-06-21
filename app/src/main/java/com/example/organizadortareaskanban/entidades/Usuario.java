package com.example.organizadortareaskanban.entidades;

public class Usuario {
    String usuario;
    String nombre;
    Integer telefono;
    byte[]  foto;

    public Usuario(String usuario, String nombre, Integer telefono, byte[] foto) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.foto = foto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
