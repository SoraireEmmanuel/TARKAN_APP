package com.example.organizadortareaskanban.database;

import java.util.Date;

public class Utilidades {

    //constantes campos de la tabla de usuarios
    public  static  final String TABLA_USUARIOS="usuarios";
    public  static  final String CAMPO_USUARIO="usuario";
    public  static  final String CAMPO_CONTRASENIA="contrasenia";
    public  static  final String CAMPO_EQUIPO="equipo";
    public  static  final String CAMPO_TELEFONO="telefono";
    public  static  final String CAMPO_FOTO="foto";


    public  static  final String CREAR_TABLA_USUARIO="CREATE TABLE "+TABLA_USUARIOS+" ("+CAMPO_USUARIO+" STRING, "+CAMPO_CONTRASENIA+" STRING, "+
            CAMPO_EQUIPO+" STRING, "+CAMPO_TELEFONO+" INTEGER, "+CAMPO_FOTO+" IMAGEVIEW)";

    public  static  final String TABLA_TAREAS="tareas";
    public  static  final String CAMPO_ID="id";
    public  static  final String CAMPO_DESCRIPCION="descripcion";
    public  static  final String CAMPO_FECHACREACION="fechaCreacion";
    public  static  final String CAMPO_PRIORIDAD="prioridad";
    public  static  final String CAMPO_TITULO="titulo";
    public  static  final String CAMPO_REALIZADOPOR="realizadoPor";
    public  static  final String CAMPO_REVISADOPOR="revisadoPor";
    public  static  final String CAMPO_CREADOPOR="creadoPor";
    public  static  final String CAMPO_ESTADO="estado";
    public  static  final String CAMPO_PROYECTO="idproyecto";

    public  static  final String CREAR_TABLA_TAREA="CREATE TABLE "+TABLA_TAREAS+" ("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_DESCRIPCION+" STRING, "+
            CAMPO_FECHACREACION+" STRING, "+CAMPO_PRIORIDAD+" STRING, "+CAMPO_TITULO+" STRING, "+CAMPO_REALIZADOPOR+" STRING, "+
            CAMPO_REVISADOPOR+" STRING, "+CAMPO_CREADOPOR+" STRING, "+CAMPO_ESTADO+" STRING,"+CAMPO_PROYECTO+" STRING)";

    public  static  final String TABLA_RELACION_PROYECTO="relacion";
    public  static  final String CAMPO_ID_USUARIO="usuario";
    public  static  final String CAMPO_ID_PROYECTO="proyecto";

    public  static  final String CREAR_TABLA_RELACION_PROYECTO="CREATE TABLE "+TABLA_RELACION_PROYECTO+" ("+CAMPO_ID_PROYECTO+" STRING, "+CAMPO_ID_USUARIO+" STRING)";

    public  static  final String TABLA_PROYECTOS="proyectos";
    public  static  final String CAMPO_FECHA_PROYECTO="fecha";
    public  static  final String CAMPO_NOMBRE_PROYECTO="nombre";

    public  static  final String CREAR_TABLA_PROYECTO="CREATE TABLE "+TABLA_PROYECTOS+" ("+CAMPO_ID_PROYECTO+" STRING, "+CAMPO_NOMBRE_PROYECTO+" STRING, "+CAMPO_FECHA_PROYECTO +" STRING)";
}