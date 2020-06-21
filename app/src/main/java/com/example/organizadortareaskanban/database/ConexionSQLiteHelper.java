package com.example.organizadortareaskanban.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {



    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
        db.execSQL(Utilidades.CREAR_TABLA_TAREA);
        db.execSQL(Utilidades.CREAR_TABLA_PROYECTO);
        db.execSQL(Utilidades.CREAR_TABLA_RELACION_PROYECTO);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionVieja, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }


}