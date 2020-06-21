package com.example.organizadortareaskanban.ui.proyectos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.ProyectosAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.ui.tareas.TareasActivity;

import java.util.ArrayList;

public class ProyectoFragment extends Fragment implements ProyectosAdapter.ListClick {
    RecyclerView recycler;
    ArrayList<Proyecto> listDatos;
    String usuariosharepreference;
    private ProyectoViewModel proyectoViewModel;

    CardView proyecto;
    View vista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        proyectoViewModel =
                ViewModelProviders.of(this).get(ProyectoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_proyecto, container, false);


        /*logica de todos los usuarios*/
        recycler=root.findViewById(R.id.recyclerProyecto);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cargarProyecto();
        ProyectosAdapter adapter=new ProyectosAdapter(listDatos,this);
        recycler.setAdapter(adapter);


        //final TextView textView = root.findViewById(R.id.text_home);
        proyectoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
          //      textView.setText(s);
            }
        });
        return root;
    }

    private void cargarProyecto() {
        listDatos=new ArrayList<Proyecto>();
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(getContext(), "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        //obtengo el usuario cargado en el sharenpreferen
        SharedPreferences preferences = this.getActivity().getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference = preferences.getString("usuario","No existe la informacion");


        //se obtiene el listado de id de proyectos a los que tiene acceso el usuario
        String [] proyeccion = {Utilidades.CAMPO_ID_PROYECTO}; //repornar los proyectos
        String selection = Utilidades.CAMPO_ID_USUARIO + " = ?"; //
        String[] selectionArg = {usuariosharepreference}; //se va a buscar por usuario logueado
        //String[] selectionArg = {"LuciaVit@gmail.com"}; //se va a buscar por usuario logueado

        //parametros para la consulta a la tabla proyectos
        String [] proyeccion2 = {Utilidades.CAMPO_NOMBRE_PROYECTO,Utilidades.CAMPO_FECHA_PROYECTO};
        String selection2 = Utilidades.CAMPO_ID_PROYECTO + " = ?";

        try {
            Cursor c = db.query(Utilidades.TABLA_RELACION_PROYECTO, proyeccion,
                    selection, selectionArg, null, null, null);
            c.moveToFirst();
            String[] selectionArg2 = {c.getString(0)};
            Cursor c1 = db.query(Utilidades.TABLA_PROYECTOS, proyeccion2,selection2,selectionArg2,
                    null,null,null);
            c1.moveToFirst();
            listDatos.add(new Proyecto(String.valueOf(c.getString(0)),c1.getString(0), c1.getString(1)));

            while(c.moveToNext()) {
                String[] selectionArg3 = {c.getString(0)};
                //Toast.makeText(getContext(), "El numero de proyecto en while" + c.getString(0), Toast.LENGTH_SHORT).show();
                Cursor c2 = db.query(Utilidades.TABLA_PROYECTOS, proyeccion2,selection2,selectionArg3,null,null,null);
                c2.moveToFirst();
                listDatos.add(new Proyecto(String.valueOf(c.getString(0)),c2.getString(0), c2.getString(1)));
            }
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "No tiene Proyectos" , Toast.LENGTH_SHORT).show();
        }
}

    @Override
    public void onListClick(String id, String nombre) {
        Intent ver = new Intent(getContext(), TareasActivity.class);
        SharedPreferences preferences=this.getActivity().getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("id",id);
        editor.putString("nombre", nombre);
        editor.commit();
        startActivity(ver);
    }
}
