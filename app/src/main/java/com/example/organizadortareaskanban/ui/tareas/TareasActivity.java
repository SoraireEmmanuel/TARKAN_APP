package com.example.organizadortareaskanban.ui.tareas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.adaptadores.BusquedaTareasAdapter;
import com.example.organizadortareaskanban.adaptadores.ProyectosAdapter;
import com.example.organizadortareaskanban.adaptadores.TareasAdapter;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;
import com.example.organizadortareaskanban.entidades.Proyecto;
import com.example.organizadortareaskanban.entidades.Tarea;
import com.example.organizadortareaskanban.ui.proyectos.BuscarProyectoActivity;

import java.util.ArrayList;

public class TareasActivity extends AppCompatActivity implements TareasAdapter.ListClick, BusquedaTareasAdapter.ListClick {
    private LinearLayout layouttareas, layoutResultadoBuqueda;
    ArrayList<Tarea> todasLasTareas, listDatos, ejecutando, esperando, revisando, finalizado;
    String proyectoSharePreference;
    RecyclerView recyclerBusqueda, recyclerIniciada, recyclerEjecutando, recyclerParaRevisar, recyclerRevisando, recyclerFinalizada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        layouttareas=(LinearLayout)findViewById(R.id.todaslasTareas);
        layoutResultadoBuqueda=(LinearLayout)findViewById(R.id.resultadosBusqueda);
        layoutResultadoBuqueda.setVisibility(View.GONE);

        recyclerBusqueda=findViewById(R.id.recyclerBusquedaTarea);
        recyclerBusqueda.setLayoutManager(new LinearLayoutManager(this));

        recyclerIniciada = findViewById(R.id.recyclerTareasIniciadas);
        recyclerIniciada.setLayoutManager(new LinearLayoutManager(this));
        recyclerEjecutando = findViewById(R.id.recyclerTareasProcesando);
        recyclerEjecutando.setLayoutManager(new LinearLayoutManager(this));
        recyclerParaRevisar = findViewById(R.id.recyclerTareasEsperando);
        recyclerParaRevisar.setLayoutManager(new LinearLayoutManager(this));
        recyclerRevisando=findViewById(R.id.recyclerTareasRevisando);
        recyclerRevisando.setLayoutManager(new LinearLayoutManager(this));
        recyclerFinalizada=findViewById(R.id.recyclerTareasFinalizada);
        recyclerFinalizada.setLayoutManager(new LinearLayoutManager(this));
        cargarTareas();
        TareasAdapter adapter=new TareasAdapter(listDatos,this);
        TareasAdapter adapter1=new TareasAdapter(ejecutando,this);
        TareasAdapter adapter2=new TareasAdapter(esperando,this);
        TareasAdapter adapter3=new TareasAdapter(revisando,this);
        TareasAdapter adapter4=new TareasAdapter(finalizado,this);
        recyclerIniciada.setAdapter(adapter);
        recyclerEjecutando.setAdapter(adapter1);
        recyclerParaRevisar.setAdapter(adapter2);
        recyclerRevisando.setAdapter(adapter3);
        recyclerFinalizada.setAdapter(adapter4);
    }

    private void cargarTareas() {
        listDatos=new ArrayList<Tarea>();
        ejecutando=new ArrayList<Tarea>();
        esperando=new ArrayList<Tarea>();
        revisando=new ArrayList<Tarea>();
        finalizado=new ArrayList<Tarea>();
        todasLasTareas=new ArrayList<Tarea>();
        final ConexionSQLiteHelper conex=new ConexionSQLiteHelper(this, "bd_usuario", null,1);
        SQLiteDatabase db = conex.getReadableDatabase();
        SharedPreferences preferences = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        proyectoSharePreference = preferences.getString("id","No existe la informacion");

        String [] proyeccion = {Utilidades.CAMPO_ID, Utilidades.CAMPO_TITULO, Utilidades.CAMPO_FECHACREACION,Utilidades.CAMPO_PRIORIDAD,Utilidades.CAMPO_ESTADO};
        String selection = Utilidades.CAMPO_PROYECTO + " = ?"; //
        String[] selectionArg = {proyectoSharePreference}; //se va a buscar tareas del proycto en sharepreferenc
        try{
            Cursor c = db.query(Utilidades.TABLA_TAREAS, proyeccion,
                    selection, selectionArg, null, null, null);
            c.moveToFirst();
            todasLasTareas.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));

            switch (c.getString(4)){
                case "Iniciada":
                    listDatos.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                break;
                case "Ejecutada":
                    ejecutando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                    break;
                case "Esperada":
                    esperando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                    break;
                case "Revisada":
                    revisando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                    break;
                case "Finalizada":
                    finalizado.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                    break;
            }

            while(c.moveToNext()){
                todasLasTareas.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                switch (c.getString(4)){
                    case "Iniciada":
                        listDatos.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                        break;
                    case "Ejecutada":
                        ejecutando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                        break;
                    case "Esperada":
                        esperando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                        break;
                    case "Revisada":
                        revisando.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                        break;
                    case "Finalizada":
                        finalizado.add(new Tarea(c.getInt(0),c.getString(1), c.getString(2),c.getString(3),c.getString(4)));
                        break;
                }

                  }

        }
        catch (Exception e){

        }

    }

    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.tareas, mimenu);
        final MenuItem searchItems= mimenu.findItem(R.id.buscarMenu);
        final SearchView searchView=(SearchView) MenuItemCompat.getActionView(searchItems);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int length = newText.length();
                Toast.makeText(TareasActivity.this, String.valueOf(length),Toast.LENGTH_SHORT).show();
                if(length>0){
                   // animar(true);
                    layouttareas.setVisibility(View.GONE);
                    layoutResultadoBuqueda.setVisibility(View.VISIBLE);

                    SharedPreferences preferences = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
                    proyectoSharePreference = preferences.getString("id","No existe la informacion");
                    ArrayList<Tarea> listaFiltrada=new ArrayList<Tarea>();
                    try {
                        newText = newText.toLowerCase();
                        for (Tarea tarea : todasLasTareas) {
                            String titulo = tarea.getTitulo().toLowerCase();
                            String proyecto = tarea.getIdproyecto();
                            if (titulo.contains(newText)) {

                                    listaFiltrada.add(new Tarea(tarea.getId(), tarea.getTitulo(), tarea.getFechaCreacion(), tarea.getPrioridad(), tarea.getEstado()));

                            }
                        }
                        BusquedaTareasAdapter adapterBusqueda = new BusquedaTareasAdapter(listaFiltrada, TareasActivity.this);
                        recyclerBusqueda.setAdapter(adapterBusqueda);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                   // animar(false);
                    layoutResultadoBuqueda.setVisibility(View.GONE);
                    layouttareas.setVisibility(View.VISIBLE);
                }

                return true;
            }
        });
        return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.agregarMenu) {
            irAgregar();
            return true;
        }
        if(id==R.id.todosLosIntegrantes){
            irTodosLosIntegrantes();
        }
        if(id==R.id.misTareas){
            irMisTareas();
        }
        return super.onOptionsItemSelected(opcion_menu);

    }

    private void irMisTareas() {
        Intent intent = new Intent(this,MisTareasActivity.class);

        startActivity(intent);
    }

    private void irTodosLosIntegrantes() {
    }


    //metodos para moverse a las distintas aplicaciones
    private void irBuscar(){
//        Intent buscar = new Intent(this, BuscarActivity.class);
 //       startActivity(buscar);
    }

    private void irAgregar(){
        Intent informacion = new Intent(this, AgregarTareaActivity.class);
        startActivity(informacion);
    }


    private void animar(boolean mostrar)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        //layoutAnimado.setLayoutAnimation(controller);
        //layoutAnimado.startAnimation(animation);
    }

    @Override
    public void onListClick(Integer id) {
        Intent ver = new Intent(this, VerTareaActivity.class);
        SharedPreferences preferences=getSharedPreferences("tarea", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putInt("id",id);
        editor.commit();
        startActivity(ver);

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
