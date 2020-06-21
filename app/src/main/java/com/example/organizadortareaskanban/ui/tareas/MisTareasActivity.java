package com.example.organizadortareaskanban.ui.tareas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.BusquedaTareasAdapter;
import com.example.organizadortareaskanban.adaptadores.TareasAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Tarea;

import java.util.ArrayList;

public class MisTareasActivity extends AppCompatActivity implements  BusquedaTareasAdapter.ListClick {
    RecyclerView recyclerMisTareas;
    ArrayList<Tarea> misTareas;
    private String usuariosharepreference;
    private String proyectoSharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tareas);
        recyclerMisTareas=(RecyclerView)findViewById(R.id.recyclerMisTarea);
        recyclerMisTareas.setLayoutManager(new LinearLayoutManager(this));
        buscarMisTareas();
        BusquedaTareasAdapter adapter=new BusquedaTareasAdapter(misTareas,this);
        recyclerMisTareas.setAdapter(adapter);
    }

    private void buscarMisTareas() {
        misTareas=new ArrayList<Tarea>();
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");
        SharedPreferences preferences2 = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        proyectoSharePreference = preferences2.getString("id","No existe la informacion");
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();

        String [] proyeccion = {Utilidades.CAMPO_ID, Utilidades.CAMPO_TITULO, Utilidades.CAMPO_FECHACREACION,Utilidades.CAMPO_PRIORIDAD,
                Utilidades.CAMPO_ESTADO,Utilidades.CAMPO_REALIZADOPOR,Utilidades.CAMPO_REVISADOPOR};
        String selection = Utilidades.CAMPO_PROYECTO + " = ?"; //
        String[] selectionArg = {proyectoSharePreference}; //se va a buscar tareas del proycto en sharepreferenc
        try {

            Cursor c = db.query(Utilidades.TABLA_TAREAS, proyeccion,
                    selection, selectionArg, null, null, null);
            c.moveToFirst();
            if(c.getString(5).equals(usuariosharepreference)){
                misTareas.add(new Tarea(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4)));
            }
            if(c.getString(6).equals(usuariosharepreference)){
                misTareas.add(new Tarea(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4)));
            }
            while (c.moveToNext()){
                if(c.getString(5).equals(usuariosharepreference)){
                    misTareas.add(new Tarea(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4)));
                }
                if(c.getString(6).equals(usuariosharepreference)){
                    misTareas.add(new Tarea(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),c.getString(4)));
                }
            }

        }catch (Exception e){

        }

    }

    @Override
    public void onListClick(Integer id, String nombre) {
        Intent ver = new Intent(this, VerTareaActivity.class);
        SharedPreferences preferences=getSharedPreferences("tarea", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("id",id);
        editor.commit();
        startActivity(ver);
    }
}
