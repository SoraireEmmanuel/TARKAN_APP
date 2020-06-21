package com.example.organizadortareaskanban.ui.proyectos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.organizadortareaskanban.MainActivity;
import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.ProyectosAdapter;
import com.example.organizadortareaskanban.adaptadores.TareasAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.ui.tareas.TareasActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BuscarProyectoActivity extends AppCompatActivity implements ProyectosAdapter.ListClick, SearchView.OnQueryTextListener{

    private Cursor c1;
    ArrayList<Proyecto> todosLosProyectos, vacio;
    RecyclerView recyclerIniciada;
    ProyectosAdapter adapter;
    private String usuariosharepreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_proyecto);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        recyclerIniciada = findViewById(R.id.recyclerBusquedaProyecto);
        recyclerIniciada.setLayoutManager(new LinearLayoutManager(this));
        vertodosLosProyectos();
        adapter=new ProyectosAdapter(todosLosProyectos,BuscarProyectoActivity.this);
        recyclerIniciada.setAdapter(adapter);



    }

    private void vertodosLosProyectos() {
        todosLosProyectos=new ArrayList<Proyecto>();
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(BuscarProyectoActivity.this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        c1 = db.query(Utilidades.TABLA_PROYECTOS, null,null,null,
                null,null,null);
        c1.moveToFirst();
        todosLosProyectos.add(new Proyecto(c1.getString(0),c1.getString(1), c1.getString(2)));
        while(c1.moveToNext()) {
            todosLosProyectos.add(new Proyecto(c1.getString(0),c1.getString(1), c1.getString(2)));
        }
    }

    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.buscarproycto, mimenu);
        final MenuItem searchItems= mimenu.findItem(R.id.buscarMenu1);
        final SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItems);

       // searchView.setOnQueryTextListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int length = newText.length();
                Toast.makeText(BuscarProyectoActivity.this, String.valueOf(length),Toast.LENGTH_SHORT).show();

                if(length>0) {
                    ArrayList<Proyecto> listaFiltrada=new ArrayList<Proyecto>();
                    try {
                        newText=newText.toLowerCase();
                        for (Proyecto proyecto:todosLosProyectos) {
                        String nombre=proyecto.getNombre().toLowerCase();
                        if(nombre.contains(newText)){
                            listaFiltrada.add(new Proyecto(proyecto.getId(),proyecto.getNombre(),proyecto.getFecha()));
                        }
                        }
                        adapter=new ProyectosAdapter(listaFiltrada,BuscarProyectoActivity.this);
                        recyclerIniciada.setAdapter(adapter);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    // instruccion cuando es cero la logitud de la busquda
                    vacio=new ArrayList<Proyecto>();
                    adapter=new ProyectosAdapter(todosLosProyectos,BuscarProyectoActivity.this);
                    recyclerIniciada.setAdapter(adapter);
                }

                return true;
            }
        });
        return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        return super.onOptionsItemSelected(opcion_menu);
    }



    @Override
    public void onListClick(final String identificador, String nombre) {
        if(verificarSiEstaEnProyecto(identificador)){

        }
        else {

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante");
            dialogo1.setMessage("¿Desea Unirse al Proyecto?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    unirse(identificador);
                    Intent intent = new Intent(BuscarProyectoActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();
        }
    }

    private boolean verificarSiEstaEnProyecto(String idProycto) {
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");
        String [] proyeccion = {Utilidades.CAMPO_ID_USUARIO}; //repornar los proyectos
        String selection = Utilidades.CAMPO_ID_PROYECTO + " = ?"; //
        String[] selectionArg = {idProycto};

        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        try{

            Cursor c = db.query(Utilidades.TABLA_RELACION_PROYECTO, proyeccion,
                    selection, selectionArg, null, null, null);
            c.moveToFirst();
            if(c.getString(0).equals(usuariosharepreference)) {
                return true;
            }
            while (c.moveToNext()){
                if(c.getString(0).equals(usuariosharepreference)) {
                    return true;
                }
            }
        }catch (Exception e){

        }
        return false;
    }

    public void unirse(String id){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(BuscarProyectoActivity.this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();

        ContentValues values8=new ContentValues();
        values8.put(Utilidades.CAMPO_ID_PROYECTO,id);
        values8.put(Utilidades.CAMPO_ID_USUARIO,usuariosharepreference);
        db.insert(Utilidades.TABLA_RELACION_PROYECTO,Utilidades.CAMPO_ID_PROYECTO,values8);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try{

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private ArrayList<Proyecto> filter  (ArrayList<Proyecto> proyectos,String texto){
        ArrayList<Proyecto> listaFiltrada=new ArrayList<Proyecto>();
        try {
            texto=texto.toLowerCase();
            for (Proyecto proyecto:proyectos)
                return  proyectos;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  proyectos;
    }
}
