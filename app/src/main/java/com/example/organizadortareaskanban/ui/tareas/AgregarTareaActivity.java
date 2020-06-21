package com.example.organizadortareaskanban.ui.tareas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.organizadortareaskanban.R;
import com.example.organizadortareaskanban.database.ConexionSQLiteHelper;
import com.example.organizadortareaskanban.database.Utilidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarTareaActivity extends AppCompatActivity {

    private TextView fecha;
    private String proyectoSharePreference;
    private String newDateStr;
    private Spinner spiner;
    EditText titulo, descripcion;
    private String usuariosharepreference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //fecha del sistema
        Date date = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", date.getTime());
        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy");
        newDateStr = postFormater.format(date);
        fecha = (TextView) findViewById(R.id.fechanuevatarea);
        fecha.setText(newDateStr);

        //spinner prioridadd
        spiner = (Spinner) findViewById(R.id.spinnerPrioridadNuevatarea);
        String [] opciones = {"Alta","Media","Baja"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item_prioridad, opciones);
        spiner.setPrompt("Prioridad de la Tarea");
        spiner.setAdapter(adapter);

        titulo=(EditText)findViewById(R.id.tituloAgregarTarea);
        descripcion=(EditText)findViewById(R.id.descripcionNuevaTarea);

    }

    @Override public  boolean onCreateOptionsMenu(Menu mimenu){
        getMenuInflater().inflate(R.menu.crearobjeto, mimenu);
return true;
    }
    //opciones de los items del menu
    @Override public  boolean onOptionsItemSelected(MenuItem opcion_menu){
        int id=opcion_menu.getItemId();
        if(id==R.id.okMenu) {
            nuevaTareaConfirm();
            return true;
        }
        return super.onOptionsItemSelected(opcion_menu);
    }

    private void nuevaTareaConfirm() {
        ConexionSQLiteHelper conex = new ConexionSQLiteHelper(this, "bd_usuario", null, 1);
        SQLiteDatabase db=conex.getWritableDatabase();

        SharedPreferences preferences = getSharedPreferences("proyecto", Context.MODE_PRIVATE);
        proyectoSharePreference = preferences.getString("id","No existe la informacion");

        SharedPreferences preferences2 = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        usuariosharepreference2 = preferences2.getString("usuario","No existe la informacion");

        ContentValues values21=new ContentValues();
        values21.put(Utilidades.CAMPO_DESCRIPCION,descripcion.getText().toString());
        values21.put(Utilidades.CAMPO_FECHACREACION,newDateStr);
        values21.put(Utilidades.CAMPO_PRIORIDAD,(String) spiner.getSelectedItem());
        values21.put(Utilidades.CAMPO_TITULO,titulo.getText().toString());
        values21.put(Utilidades.CAMPO_REALIZADOPOR,"None");
        values21.put(Utilidades.CAMPO_REVISADOPOR,"None");
        values21.put(Utilidades.CAMPO_CREADOPOR,usuariosharepreference2);
        values21.put(Utilidades.CAMPO_ESTADO,"Iniciada");
        values21.put(Utilidades.CAMPO_PROYECTO,proyectoSharePreference);
        db.insert(Utilidades.TABLA_TAREAS,Utilidades.CAMPO_ID,values21);

        Intent intent=new Intent(this,TareasActivity.class);
        startActivity(intent);

    }
}
