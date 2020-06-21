package com.example.organizadortareaskanban.ui.usuarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.ProyectosAdapter;
import com.example.organizadortareaskanban.adaptadores.TareasAdapter;
import com.example.organizadortareaskanban.adaptadores.UsuarioAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.entidades.Usuario;

import java.util.ArrayList;

public class UsuarioActivity extends AppCompatActivity implements UsuarioAdapter.ListClick {

    RecyclerView recycler;
    ArrayList<Usuario> listDatos;
    private String proyectoSharePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);



        recycler=findViewById(R.id.recycleUsuario);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        consultarUsuarioProyecto();
        UsuarioAdapter adapter=new UsuarioAdapter(listDatos,this);
        recycler.setAdapter(adapter);



    }

    private void consultarUsuarioProyecto() {
        listDatos=new ArrayList<Usuario>();
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        SharedPreferences preferences = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        proyectoSharePreference = preferences.getString("id","No existe la informacion");
    }

    @Override
    public void onListClick(Integer id) {

    }
}
