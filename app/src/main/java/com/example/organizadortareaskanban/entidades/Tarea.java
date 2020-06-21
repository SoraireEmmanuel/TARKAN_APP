package com.example.organizadortareaskanban.entidades;

public class Tarea {

    Integer id;
    String descripcion;
    String fechaCreacion;
    String prioridad;
    String titulo;
    String realizadoPor;
    String revisadoPor;
    String creadoPor;
    String estado;
    String idproyecto;

    public Tarea(Integer id, String descripcion, String fechaCreacion, String prioridad, String titulo, String realizadoPor, String revisadoPor, String creadoPor, String estado, String idproyecto) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.titulo = titulo;
        this.realizadoPor = realizadoPor;
        this.revisadoPor = revisadoPor;
        this.creadoPor = creadoPor;
        this.estado = estado;
        this.idproyecto = idproyecto;
    }

    public Tarea(String fechaCreacion, String prioridad, String titulo) {
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.titulo = titulo;
    }

    public Tarea(Integer id, String titulo,String fechaCreacion, String prioridad,  String estado) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.titulo = titulo;
        this.estado = estado;
    }
    public Tarea(Integer id, String titulo,String fechaCreacion, String prioridad,  String estado, String realizadoPor, String revisadoPor) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.titulo = titulo;
        this.estado = estado;
        this.realizadoPor=realizadoPor;
        this.revisadoPor=revisadoPor;
    }

    public Tarea(Integer id, String titulo, String fechaCreacion, String prioridad) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.prioridad = prioridad;
        this.titulo = titulo;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRealizadoPor() {
        return realizadoPor;
    }

    public void setRealizadoPor(String realizadoPor) {
        this.realizadoPor = realizadoPor;
    }

    public String getRevisadoPor() {
        return revisadoPor;
    }

    public void setRevisadoPor(String revisadoPor) {
        this.revisadoPor = revisadoPor;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdproyecto() {
        return idproyecto;
    }

    public void setIdproyecto(String idproyecto) {
        this.idproyecto = idproyecto;
    }
}
